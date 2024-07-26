package com.cloudstorage.mapper;

import com.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> getFileForUser(Integer userId);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File findFile(Integer fileId);

    @Select("SELECT * FROM FILES")
    List<File> findAllFiles();

    @Select("SELECT * FROM FILES WHERE filename = #{filename}")
    File findFileByName(String filename);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES (#{filename}, #{contenttype}, #{filesize}, #{userid}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    Integer insert(File file);

    @Update("UPDATE FILES SET filename = #{filename}, contenttype = #{contenttype}, filesize = #{filesize}, userid = #{userid}, filedata = #{filedata} WHERE fileId = #{fileId}")
    void update(File file);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    void delete(Integer fileId);
}
