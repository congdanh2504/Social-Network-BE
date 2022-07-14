package com.example.social_network_fpt_be.service;


import com.example.social_network_fpt_be.model.Image;
import com.example.social_network_fpt_be.repository.ImageRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static com.example.social_network_fpt_be.config.Setting.*;

@Service
@Transactional
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;


    public List<Image> getImageList() {
        return imageRepository.findAll();
    }

    public Image getImageById(Integer id_image) {
        return imageRepository.findById(id_image).orElse(null);
    }

    public Image createImage(MultipartFile imageFile, String type, Integer id) throws IOException {
        String url = uploadImage(imageFile);
        Image image = new Image();
        image.setUrl(url);
        image.setCreate_date(LocalDateTime.now());
        image.setType(type);
        image.setId(id);
        imageRepository.save(image);
        return image;
    }

    public Image updateImage(Integer id_image, MultipartFile newImageFile, String newType, Integer newId) throws IOException {
        String url = uploadImage(newImageFile);
        Image newImage = new Image();
        return imageRepository.findById(id_image)
                    .map(Image -> {
                        Image.setUrl(url);
                        Image.setCreate_date(LocalDateTime.now());
                        Image.setType(newType);
                        Image.setId(newId);
                        return imageRepository.save(Image);
                    })
                    .orElse(null);
    }

    public String deleteImage(Integer id_image) {
        try{
            imageRepository.deleteById(id_image);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    public List<Object> findImageForPost(String type, Integer id) {
        return imageRepository.findImageByTypeAndId(type, id);
    }

    public void deleteImageByTypeAndId(String type, Integer id) {
        try{
            List<Object> image = imageRepository.findImageByTypeAndId(type, id);
            for (Object img: image) {
                deleteImage((Integer) ((Object[]) img)[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String setRandomFileName() {
        return UUID.randomUUID().toString();
    }

    private String uploadImage(MultipartFile imageFile) throws IOException {
        String name = setRandomFileName();
        // Upload file to Cloud Storage
        StorageOptions storageOptions = StorageOptions.newBuilder()
                    .setProjectId("my-project")
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource("ServiceAccountKey.json").getInputStream()))
                    .build();
        Storage storage = storageOptions.getService();
        BlobId blobId = BlobId.of(BUCKET_NAME, name);
        //
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(imageFile.getContentType()).build();
        Blob blob = storage.create(blobInfo, imageFile.getInputStream());

        // Get URL of imageFile upload into firebase
        String result = PATH_PREFIX + BUCKET_NAME + "/" + name;
        return result;
    }

    public Hashtable<String, Object> checkFile(MultipartFile imageFile) {
        Hashtable<String, Object> postList = new Hashtable<>();
        if (imageFile.isEmpty()) {
            postList.put("status", 0);
            postList.put("message", "Image file is empty");
        }else if (imageFile.getSize() > MAX_SIZE) {
            postList.put("status", 0);
            postList.put("message", "Image file is too large");
        } else if (Objects.equals(imageFile.getContentType(), "image/png")
                || Objects.equals(imageFile.getContentType(), "image/jpeg")
                || Objects.equals(imageFile.getContentType(), "image/jpg")) {
            postList.put("status", 1);
            postList.put("message", "Success");
        } else {
            postList.put("status", 0);
            postList.put("message", "Image file is not an image");
        }
        return postList;
    }
}
