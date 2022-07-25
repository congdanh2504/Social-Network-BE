package com.example.social_network_fpt_be.controller;


import com.example.social_network_fpt_be.models.Image;
import com.example.social_network_fpt_be.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping(path = "")
    public ResponseEntity<List<Image>> getImageList() {
        return ResponseEntity.status(HttpStatus.OK).body(imageService.getImageList());
    }

    @GetMapping(path = "/{id_image}")
    public ResponseEntity<Image> getImageById(@PathVariable int id_image) {
        return ResponseEntity.status(HttpStatus.OK).body(imageService.getImageById(id_image));
    }

    @PostMapping(path = "", consumes = "multipart/form-data")
    public ResponseEntity<Object> createImage(@RequestPart("imageFile") List<MultipartFile> imageFile,
                                              @RequestParam("type") String type,
                                              @RequestParam("id") int id) throws IOException {
        List<Image> images = new ArrayList<>();
        for (MultipartFile multipartFile: imageFile){
            Hashtable<String, Object> result = imageService.checkFile(multipartFile);
            if (result.get("status").equals(0)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.get("message"));
            } else {
                images.add(imageService.createImage(multipartFile, type, id));
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(images);
    }

    @PutMapping(path = "/{id_image}")
    public ResponseEntity<Object> updateImage(@PathVariable int id_image,
                             @RequestPart("imageFile") MultipartFile imageFile,
                             @RequestParam("type") String type,
                             @RequestParam("id") int id) throws IOException {
        Hashtable<String, Object> result = imageService.checkFile(imageFile);
        if (result.get("status").equals(0)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.get("message"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(imageService.updateImage(id_image, imageFile, type, id));
        }
    }

    @DeleteMapping(path = "/{id_image}")
    public ResponseEntity<String> deleteImage(@PathVariable int id_image) {
        String result = imageService.deleteImage(id_image);
        if (result.equals("success")) {
            return ResponseEntity.status(HttpStatus.OK).body("Delete image success");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Delete image fail");
        }
    }
}
