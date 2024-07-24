package com.cloudstorage.services;

import com.cloudstorage.mapper.FileMapper;
import com.cloudstorage.mapper.UserMapper;
import com.cloudstorage.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private UserMapper userMapper;

    public String[] getFileListings(Integer userId) {
        return fileMapper.getFileForUser(userId);
    }


    public File getAllFilebyid(Integer fileId) {
        return fileMapper.findFile(fileId);
    }


    public List<File> getAllFiles() {
        return fileMapper.findAllFiles();
    }


    public void saveFile(MultipartFile fileUpload, String userName) throws IOException {
        File existingFileByName = fileMapper.findFileByName(fileUpload.getOriginalFilename());
        if (existingFileByName != null) {
            throw new IOException("File already exists");
        }
        Integer userId = userMapper.findUser(userName).getUserid();

        File file = new File();
        file.setFilename(fileUpload.getOriginalFilename());
        file.setContenttype(fileUpload.getContentType());
        file.setFilesize(String.valueOf(fileUpload.getSize()));
        file.setFiledata(fileUpload.getInputStream());
        file.setUserid(userId);
        fileMapper.insert(file);
    }


    public void deleteFile(Integer fileId) {
        fileMapper.delete(fileId);
    }
}






