package com.backvuebato.controller;

import com.backvuebato.entity.Files;
import com.backvuebato.entity.Users;
import com.backvuebato.repository.FilesRepository;
import com.backvuebato.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static java.lang.Integer.parseInt;

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

    @PostMapping("downloadFile")
    public byte[] downloadFile(@RequestBody Map<String, Object> data) {
        Map<String, Object> formFile = (Map<String, Object>) data.get("file");
        Files file = filesRepository.findById(Integer.parseInt(formFile.get("id").toString()));
        return file.getData();
    }

    @PostMapping("deleteFile")
    public Map<String, Object> deleteFile(@RequestBody Map<String, Object> data){
        Object file =  data.get("file");
        try{
            Map<String, Object> toDelete = (Map<String, Object>) file;
            String strId = toDelete.get("id").toString();
            int id = Integer.parseInt(strId);
            filesRepository.deleteById(id);
        }catch (Exception e){
            java.lang.System.out.println("Error while deleting file");
        }
        return new HashMap<>();
    }

    @PostMapping("getFiles")
    public Map<String, Object> getFiles(@RequestBody Map<String, Object> data){
        String sortDesc = data.get("sortDesc").toString();
        int itemsPerPage = parseInt(data.get("itemsPerPage").toString());
        String orderBy = data.get("sortBy").toString();
        String forPageable;
        switch (orderBy) {
            case "[name]":
                forPageable = "name";
                break;
            case "[whoInserted]":
                forPageable = "whoInserted";
                break;
            case "[insertDate]":
                forPageable = "insertDate";
                break;
            default:
                forPageable = "id";
        }
        int currPage = parseInt(data.get("page").toString()) - 1;
        Pageable pageable = PageRequest.of(currPage, itemsPerPage, Sort.by(sortDesc.equals("[true]") ? Sort.Direction.DESC : Sort.Direction.ASC, forPageable));
        Page<Files> page;
        String search = null == data.get("search") ? "" : data.get("search").toString();
        if (search.isEmpty()) {
            page = filesRepository.findAll(pageable);
        } else {
            // TODO search
            page = filesRepository.findAll(pageable);
        }
        List<Files> filesList = page.getContent();

        List<Map<String, Object>> filesToDisplay = new ArrayList<>();
        for (Files file : filesList) {
            Map<String, Object> currentFile = new HashMap<>();

            currentFile.put("id", file.getId());
            currentFile.put("name", file.getName());
            currentFile.put("insertDate", file.getInsertDate());
            Users user = userRepository.findById(file.getWhoInserted());
            currentFile.put("whoInserted", user.getUsername());
            filesToDisplay.add(currentFile);
        }
        long totalFiles = page.getTotalElements();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("files", filesToDisplay);
        resultMap.put("totalFiles", totalFiles);
        return resultMap;
    }
}
