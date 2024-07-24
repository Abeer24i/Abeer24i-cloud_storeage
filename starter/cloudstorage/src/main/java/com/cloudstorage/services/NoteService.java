package com.cloudstorage.services;

import com.cloudstorage.mapper.NoteMapper;
import com.cloudstorage.mapper.UserMapper;
import com.cloudstorage.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    @Autowired
    private NoteMapper noteMapper;

    @Autowired
    private UserMapper userMapper;


    public Note[] getNoteListings(Integer userId) {
        return noteMapper.getNotesForUser(userId);
    }

    public Note getNoteById(Integer noteId) {
        return noteMapper.findNote(noteId);
    }


    public List<Note> getAllNotes() {
        return noteMapper.findAllNotes();
    }


  /*  public void addNote(Note note) throws IOException {
        noteMapper.insert(note);
    }

   */

    public void addNote(String title, String description, String userName) {
        Integer userId = userMapper.findUser(userName).getUserid();
        Note note = new Note(0, title, description, userId);
        noteMapper.insert(note);
    }

    public void updateNote(Integer noteId, String title, String description) {
        noteMapper.updateNote(noteId, title, description);
    }




    public void deleteNote(Integer noteId) {
        noteMapper.delete(noteId);
    }
}
