package com.cloudstorage.mapper;

import com.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    Note[] getNotesForUser(Integer userId);


    @Select("SELECT * FROM NOTES WHERE noteId = #{noteId}")
    Note findNote(Integer noteId);

    @Select("SELECT * FROM NOTES")
    List<Note> findAllNotes();

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES (#{notetitle}, #{notedescription}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    Integer insert(Note note);

   /* @Update("UPDATE NOTES SET notetitle = #{notetitle}, notedescription = #{notedescription}, userid = #{userid} WHERE noteId = #{noteId}")
    void update(Note note);*/

    @Update("UPDATE NOTES SET notetitle = #{title}, notedescription = #{description} WHERE noteid = #{noteId}")
    void updateNote(Integer noteId, String title, String description);

    @Delete("DELETE FROM NOTES WHERE noteId = #{noteId}")
    void delete(Integer noteId);


}
