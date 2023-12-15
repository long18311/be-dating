package com.example.datingbe.service;

import com.example.datingbe.dto.ImagesDto;
import com.example.datingbe.entity.Images;
import com.example.datingbe.repository.ImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service
public class ImagesService {
    @Autowired
    ImagesRepository imagesRepository;
    public ImagesDto Save (Images image){
        imagesRepository.save(image);
        return new ImagesDto(image);
    }

}
