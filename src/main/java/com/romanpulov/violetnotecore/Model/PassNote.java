package com.romanpulov.violetnotecore.Model;

import java.util.Date;

/**
 * Created on 16.01.2016.
 */
public class PassNote {
    private NoteCategory noteCategory;
    private String system;
    private String user;
    private String password;
    private String comments;
    private String custom;
    private String info;
    private Date createdDate;
    private Date modifiedDate;
    private boolean isActive;

    public PassNote() {
        this.createdDate = new Date();
        this.modifiedDate = new Date();
    }

    public PassNote(NoteCategory noteCategory, String system, String user, String password, String comments, String custom, String info) {
        this();
        this.noteCategory = noteCategory;
        this.system = system;
        this.user = user;
        this.password = password;
        this.comments = comments;
        this.custom = custom;
        this.info = info;
    }

    @Override
    public String toString() {
        String res = "{";

        if (noteCategory == null)
            res += "noteCategory=null,";
        else
            res += "noteCategory=" + noteCategory.toString() + ",";
        res += "system=" + system + ",";
        res += "user=" + user + ",";
        res += "password=" + password + ",";
        res += "comments=" + comments + ",";
        res += "custom=" + custom + ",";
        res += "info=" + info;

        res += "}";
        return res;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public NoteCategory getNoteCategory() {
        return noteCategory;
    }

    public String getSystem() {
        return system;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getComments() {
        return comments;
    }

    public String getCustom() {
        return custom;
    }

    public String getInfo() {
        return info;
    }
}
