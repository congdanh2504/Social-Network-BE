package com.example.social_network_fpt_be.service;

import com.example.social_network_fpt_be.models.Video;
import com.example.social_network_fpt_be.repository.VideoRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.example.social_network_fpt_be.config.Setting.*;

@Service
@Transactional
public class VideoService {
    @Autowired
    private VideoRepository videoRepository;

    public List<Video> getVideoList() {
        return videoRepository.findAll();
    }

    public Video getVideoById(Long id_video) {
        return videoRepository.findById(id_video).orElse(null);
    }

    public Video createVideo(MultipartFile videoFile) throws IOException {
        String url = uploadVideo(videoFile);
        Video video = new Video();
        video.setUrl(url);
        video.setCreate_date(LocalDateTime.now());
        videoRepository.save(video);
        return video;
    }

    public Video updateVideo(Long id_video, MultipartFile newVideoFile) throws IOException {
        String url = uploadVideo(newVideoFile);
        Video newVideo = new Video();
        return videoRepository.findById(id_video)
                    .map(Video -> {
                        Video.setUrl(url);
                        Video.setCreate_date(LocalDateTime.now());
                        return videoRepository.save(Video);
                    })
                    .orElse(null);
    }

    public String deleteVideo(Long id_video) {
        try{
            videoRepository.deleteById(id_video);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    private String setRandomFileName() {
        return UUID.randomUUID().toString();
    }

    private String uploadVideo(MultipartFile videoFile) throws IOException {
        String result;
        String name = setRandomFileName();

        // Upload file to Cloud Storage
        StorageOptions storageOptions = StorageOptions.newBuilder()
                    .setProjectId("my-project")
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource("ServiceAccountKey.json").getInputStream()))
                    .build();
        Storage storage = storageOptions.getService();
        BlobId blobId = BlobId.of(BUCKET_NAME, name);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(videoFile.getContentType()).build();
        Blob blob = storage.create(blobInfo, videoFile.getInputStream());

        // Get URL of imageFile upload into firebase
        result = PATH_PREFIX + BUCKET_NAME + "/" + name;
        return result;
    }

    public Hashtable<String, Object> checkFile(MultipartFile videoFile) {
        Hashtable<String, Object> postList = new Hashtable<>();
        if (videoFile.isEmpty()) {
            postList.put("status", 0);
            postList.put("message", "Video file is empty");
        }else if (videoFile.getSize() > MAX_SIZE) {
            postList.put("status", 0);
            postList.put("message", "Video file is too large");
        } else if (Objects.equals(videoFile.getContentType(), "video/mp4")) {
            postList.put("status", 1);
            postList.put("message", "Success");
        } else {
            postList.put("status", 0);
            postList.put("message", "Video file is not an video");
        }
        return postList;
    }
}
