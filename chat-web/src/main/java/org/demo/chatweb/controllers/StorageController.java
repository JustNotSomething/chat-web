package org.demo.chatweb.controllers;

import jakarta.servlet.ServletException;
import org.demo.chatweb.config.JWTUtil;
import org.demo.chatweb.models.User;
import org.demo.chatweb.services.StorageService;
import org.demo.chatweb.services.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/file")
public class StorageController {

    private final StorageService storageService;
    private final UsersService usersService;
    private final JWTUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(StorageController.class);


    @Autowired
    public StorageController(StorageService storageService, UsersService usersService, JWTUtil jwtUtil) {
        this.storageService = storageService;
        this.usersService = usersService;
        this.jwtUtil = jwtUtil;
    }
    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestParam(value = "file") MultipartFile file, @RequestHeader(value = "Authorization") String authHeader){
        try
        {
            String username = jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader);
            User user = usersService.getByUsername(username);
            storageService.uploadFile(file, user.getId());
            usersService.saveAndSetAvatar(user, "avatar_" + user.getId());

            logger.info("User - " + username + " uploaded avatar");
            return new ResponseEntity(HttpStatus.OK);

        } catch (ServletException e) {

            logger.error("Error while uploading user's avatar");
            throw new RuntimeException(e);

        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) throws IOException {
        byte [] data = storageService.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/octet-stream" ).header("Content-disposition", "attachment; filename=\""+ fileName + "\"").body(resource);
    }

    @DeleteMapping("/delete/{fileName}")

    public ResponseEntity<String> deleteFile(@PathVariable String fileName)
    {
        return new ResponseEntity<>(storageService.deleteFile(fileName), HttpStatus.OK);
    }


    @GetMapping("/get/avatar")
    public ResponseEntity<ByteArrayResource> getFile(@RequestHeader(value = "Authorization") String authHeader) throws IOException {
        try{
            String username = jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader);
            User user = usersService.getByUsername(username);


            if (user.getAvatar().equals("NULL")) {
                return ResponseEntity.notFound().build();
            }


            String filePath = "avatars/" + user.getAvatar();
            byte[] data = storageService.downloadFile(filePath);
            ByteArrayResource resource = new ByteArrayResource(data);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(data.length);
            headers.setContentDispositionFormData("inline", "avatar_" + user.getId());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            String errorMessage = "Failed to retrieve avatar.";
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            ByteArrayResource errorResource = new ByteArrayResource(errorMessage.getBytes());

            return ResponseEntity.status(status)
                    .body(errorResource);
        }

    }
}

