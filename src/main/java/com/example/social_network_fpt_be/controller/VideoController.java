package com.example.social_network_fpt_be.controller;

import com.example.social_network_fpt_be.model.Video;
import com.example.social_network_fpt_be.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.example.social_network_fpt_be.config.Setting.MAX_SIZE;

@RestController
@RequestMapping(path = "/api/v1/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping(path = "")
    public ResponseEntity<List<Video>> getVideoList() {
        return ResponseEntity.status(HttpStatus.OK).body(videoService.getVideoList());
    }

    @GetMapping(path = "/{id_video}")
    public ResponseEntity<Video> getVideoById(@PathVariable Long id_video) {
        return ResponseEntity.status(HttpStatus.OK).body(videoService.getVideoById(id_video));
    }

    @PostMapping(path = "", consumes = "multipart/form-data")
    public ResponseEntity<Object> createVideo(@RequestPart("videoFile") MultipartFile videoFile) throws IOException {
        // try to use modelAtribute
        String result;
        if (videoFile.isEmpty()) {
            result = "File is empty";
        }else if (videoFile.getSize() > MAX_SIZE) {
            result = "File is too large";
        } else if (Objects.equals(videoFile.getContentType(), "video/mp4")) {
            return ResponseEntity.status(HttpStatus.OK).body(videoService.createVideo(videoFile));
        } else {
            result = "File is not an video";
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @PutMapping(path = "/{id_video}")
    public ResponseEntity<Object> updateVideo(@PathVariable Long id_video,
                              @RequestPart("videoFile") MultipartFile videoFile) throws IOException {
        String result;
        if (videoFile.isEmpty()) {
            result = "File is empty";
        }else if (videoFile.getSize() > MAX_SIZE) {
            result = "File is too large";
        } else if (Objects.equals(videoFile.getContentType(), "video/mp4")) {
            return ResponseEntity.status(HttpStatus.OK).body(videoService.updateVideo(id_video, videoFile));
        } else {
            result = "File is not an video";
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @DeleteMapping(path = "/{id_video}")
    public ResponseEntity<String> deleteVideo(@PathVariable Long id_video) {
        String result = videoService.deleteVideo(id_video);
        if (result.equals("success")) {
            return ResponseEntity.status(HttpStatus.OK).body("Delete video success");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Delete video fail");
        }
    }

}
