package com.arasoftwares.fileupload.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.arasoftwares.fileupload.entity.FileUpload;
import com.arasoftwares.fileupload.repository.FileRepository;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.function.ServerRequest.Headers;

import jdk.jfr.ContentType;

@RestController
public class FileController {

    

    @Autowired
    private FileRepository fileRepo;

    @Value("${file.upload-dir}")
    String FILE_DIRECTORY;

    @GetMapping("/file_uploads")
    public List<FileUpload> list() {
        List<FileUpload> fileUploads = fileRepo.findAll();
        return fileUploads;
    }

    @GetMapping("/file_uploads/{id}")
    public FileUpload get(@PathVariable Long id) {
        Optional<FileUpload> fileUploadOptional = fileRepo.findById(id);
        if (fileUploadOptional.isPresent()) {
            return fileUploadOptional.get();
        } else {
            throw new RuntimeException("File not found for the id " + id);
        }
    }

    @PostMapping("/file_uploads")

    public ResponseEntity<Object> fileUpload(@RequestParam("File") MultipartFile multiPartFile) throws IOException {

        String actualFileName = multiPartFile.getOriginalFilename();
        File aFile = new File(FILE_DIRECTORY + actualFileName);
        aFile.getAbsolutePath();

        aFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(aFile);
        fos.write(multiPartFile.getBytes());
        fos.close();

        FileUpload fileUpload = new FileUpload();
        fileUpload.fileName = actualFileName;
        fileUpload.fileSize = aFile.length();

        FileUpload savedFileUpload = fileRepo.save(fileUpload);
        return new ResponseEntity<>(savedFileUpload, HttpStatus.OK);

    }

    @PutMapping("/file_uploads/{id}")
    public ResponseEntity<Object> updatefileUpload(@RequestParam("File") MultipartFile multiPartFile,
            @PathVariable Long id) throws IOException {
        Optional<FileUpload> fOptional = fileRepo.findById(id);
        if (fOptional.isPresent()) {
            FileUpload fileUpload = fOptional.get();
            File deleteFile = new File(FILE_DIRECTORY + fileUpload.fileName);
            deleteFile.delete();
        } else {
            throw new RuntimeException("File not found for the id " + id);
        }
        String actualFileName = multiPartFile.getOriginalFilename();
        File aFile = new File(FILE_DIRECTORY + actualFileName);
        aFile.getAbsolutePath();

        aFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(aFile);
        fos.write(multiPartFile.getBytes());
        fos.close();

        FileUpload saveFileUpload = new FileUpload();
        saveFileUpload.id = id;
        saveFileUpload.fileName = actualFileName;
        saveFileUpload.fileSize = aFile.length();

        FileUpload savedFileUpload = fileRepo.save(saveFileUpload);
        return new ResponseEntity<>(savedFileUpload, HttpStatus.OK);

    }

// @RequestMapping(path = "/download", method = RequestMethod.GET)
// public ResponseEntity<Resource> download(String param) throws IOException {
//     File file = new File(FILE_DIRECTORY+File.separator);

//     HttpHeaders header = new HttpHeaders();
//     header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = img.png");
//     header.add("Cache-Control", "no-cache, no-store, must-revalidate");
//     header.add("Pragma", "no-cache");
//     header.add("Expires", "0");

// Path path = Paths.get(file.getAbsolutePath());
// ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

// return ResponseEntity.ok();
// .headers(header)
// .contentLength(file.length())
// .contentType(MediaType.parseMediaType("application/octet-stream"))
// .body(resource);

// }

// @GetMapping("/file_downloads/{id}")
// public ResponseEntity downloadFile(@PathVariable Long id) {
//     FileUpload fileUpload = fileRepo.findById(id);
//     return ResponseEntity.ok()
//     .contentType(MediaType.parseMediaType(ContentType))
//     .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
//     .body(fileUpload.getFile());
// }

//private static final strin

    @DeleteMapping("/file_uploads/{id}")
    public String delete(@PathVariable Long id) {
        Optional<FileUpload> fileUploadOptional = fileRepo.findById(id);
        if (fileUploadOptional.isPresent()) {
            FileUpload fileUpload = fileUploadOptional.get();
            File deleteFile = new File(FILE_DIRECTORY + fileUpload.fileName);
            deleteFile.delete();
            fileRepo.delete(fileUploadOptional.get());
            return "File is deleted with id" + id;
        } else {
            throw new RuntimeException("File not found for the id " + id);

        }

    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource
}