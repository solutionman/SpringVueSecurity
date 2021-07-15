package com.backvuebato.controller;

import com.backvuebato.repository.FilesRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FilesController {
    private final FilesRepository filesRepository;

    FilesController(FilesRepository filesRepository) {
        this.filesRepository = filesRepository;
    }

    @PostMapping("storeFile")
    public Map<String, Object> storeFile(@RequestParam("file") MultipartFile file) {
        String debug = "";

        return new HashMap<>();
    }
}
