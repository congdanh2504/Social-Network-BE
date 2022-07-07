package com.example.social_network_fpt_be.cloudStorage.image;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/image")
public class imageController {

    @Autowired
    private ImageService imageService;

    @GetMapping(path = "/")
    public List<Image> getImageList() {
        return imageService.getImageList();
    }
}
