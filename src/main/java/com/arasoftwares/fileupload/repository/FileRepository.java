package com.arasoftwares.fileupload.repository;


import com.arasoftwares.fileupload.entity.FileUpload;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileUpload, Long>{
    
}
