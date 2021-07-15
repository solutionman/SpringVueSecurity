package com.backvuebato.controller;

import com.backvuebato.entity.Files;
import com.backvuebato.repository.FilesRepository;
import com.backvuebato.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;

    FilesController(FilesRepository filesRepository, UserRepository userRepository) {
        this.filesRepository = filesRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("storeFile")
    public Map<String, Object> storeFile(@RequestParam("file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        int currentUserId = userRepository.findByUsername(name).getId();
        Files files = new Files();
        try {
            files.setName(file.getOriginalFilename());
            files.setData(file.getBytes());
            files.setInsertDate(new java.sql.Date(System.currentTimeMillis()));
            files.setWhoInserted(currentUserId);
            filesRepository.save(files);
        } catch (Exception e){
            java.lang.System.out.println("Exception while saving file " + e.getMessage());
        }

        Files check = filesRepository.findById(files.getId());
        String debug = "";

        return new HashMap<>();
    }
}
