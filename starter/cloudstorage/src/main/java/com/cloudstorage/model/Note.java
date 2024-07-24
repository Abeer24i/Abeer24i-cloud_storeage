package com.cloudstorage.model;

public class Note {

    private Integer noteId;
    private String notetitle;
    private String notedescription;
    private Integer userid;

    public Note(Integer noteId, String notetitle, String notedescription, Integer userid) {
        this.noteId = noteId;
        this.notetitle = notetitle;
        this.notedescription = notedescription;
        this.userid = userid;
    }

    public String getNoteIdstr() {
        return ""+noteId;
    }

    public Integer getNoteId() {
        return noteId;
    }

    public void setNoteId(Integer noteid) {
        this.noteId = noteid;
    }

    public String getNotetitle() {
        return notetitle;
    }

    public void setNotetitle(String notetitle) {
        this.notetitle = notetitle;
    }

    public String getNotedescription() {
        return notedescription;
    }

    public void setNotedescription(String notedescription) {
        this.notedescription = notedescription;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}
