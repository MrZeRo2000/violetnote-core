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
