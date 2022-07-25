package com.example.social_network_fpt_be.controller;

import com.example.social_network_fpt_be.models.Video;
import com.example.social_network_fpt_be.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

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
    public ResponseEntity<Video> getVideoById(@PathVariable int id_video) {
        return ResponseEntity.status(HttpStatus.OK).body(videoService.getVideoById(id_video));
    }

    @PostMapping(path = "", consumes = "multipart/form-data")
    public ResponseEntity<Object> createVideo(@RequestPart("videoFile") MultipartFile videoFile) throws IOException {
        // try to use modelAtribute
        Hashtable<String, Object> result = videoService.checkFile(videoFile);
        if (result.get("status").equals(0)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.get("message"));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(videoService.createVideo(videoFile));
        }
    }

    @PutMapping(path = "/{id_video}")
    public ResponseEntity<Object> updateVideo(@PathVariable int id_video,
                              @RequestPart("videoFile") MultipartFile videoFile) throws IOException {
        Hashtable<String, Object> result = videoService.checkFile(videoFile);
        if (result.get("status").equals(0)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.get("message"));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(videoService.updateVideo(id_video, videoFile));
        }
    }

    @DeleteMapping(path = "/{id_video}")
    public ResponseEntity<String> deleteVideo(@PathVariable int id_video) {
        String result = videoService.deleteVideo(id_video);
        if (result.equals("success")) {
            return ResponseEntity.status(HttpStatus.OK).body("Delete video success");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Delete video fail");
        }
    }

}
