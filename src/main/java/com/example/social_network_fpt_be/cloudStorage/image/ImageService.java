package com.example.social_network_fpt_be.cloudStorage.image;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public List<Image> getImageList() {
        return imageRepository.findAll();
    }
}
