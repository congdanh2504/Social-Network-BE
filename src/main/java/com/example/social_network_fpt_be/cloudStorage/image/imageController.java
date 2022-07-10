package com.example.social_network_fpt_be.cloudStorage.image;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.example.social_network_fpt_be.cloudStorage.setting.MAX_SIZE;

@RestController
@RequestMapping(path = "/api/v1/image/")
public class imageController {

    @Autowired
    private ImageService imageService;

    @GetMapping(path = "")
    public ResponseEntity<List<Image>> getImageList() {
        return ResponseEntity.status(HttpStatus.OK).body(imageService.getImageList());
    }

    @GetMapping(path = "{id_image}")
    public ResponseEntity<Image> getImageById(@PathVariable Long id_image) {
        return ResponseEntity.status(HttpStatus.OK).body(imageService.getImageById(id_image));
    }

    @PostMapping(path = "", consumes = "multipart/form-data")
    public ResponseEntity<Object> createImage(@RequestPart("imageFile") MultipartFile imageFile,
                                      @RequestParam("type") String type,
                                      @RequestParam("id") int id) throws IOException {
        // try to use modelAtribute
        String result;
        if (imageFile.isEmpty()) {
            result = "File is empty";
        }else if (imageFile.getSize() > MAX_SIZE) {
            result = "File is too large";
        } else if (Objects.equals(imageFile.getContentType(), "image/png")
                || Objects.equals(imageFile.getContentType(), "image/jpeg")
                || Objects.equals(imageFile.getContentType(), "image/jpg")) {
            return ResponseEntity.status(HttpStatus.OK).body(imageService.createImage(imageFile, type, id));
        } else {
            result = "File is not an image";
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @PutMapping(path = "{id_image}")
    public ResponseEntity<Object> updateImage(@PathVariable Long id_image,
                             @RequestPart("imageFile") MultipartFile imageFile,
                             @RequestParam("type") String type,
                             @RequestParam("id") int id) throws IOException {
        String result;
        if (imageFile.isEmpty()) {
            result = "File is empty";
        }else if (imageFile.getSize() > MAX_SIZE) {
            result = "File is too large";
        } else if (Objects.equals(imageFile.getContentType(), "image/png")
                || Objects.equals(imageFile.getContentType(), "image/jpeg")
                || Objects.equals(imageFile.getContentType(), "image/jpg")) {
            return ResponseEntity.status(HttpStatus.OK).body(imageService.updateImage(id_image, imageFile, type, id));
        } else {
            result = "File is not an image";
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @DeleteMapping(path = "{id_image}")
    public ResponseEntity<String> deleteImage(@PathVariable Long id_image) {
        String result = imageService.deleteImage(id_image);
        if (result.equals("success")) {
            return ResponseEntity.status(HttpStatus.OK).body("Delete image success");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Delete image fail");
        }
    }
}
