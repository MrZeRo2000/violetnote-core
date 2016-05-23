package com.romanpulov.violetnotecore.Model;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created on 16.01.2016.
 */
public class PassNote extends PassDataItem {
    public final static String ATTR_NODE_NOTE = "node";

    public final static String ATTR_SYSTEM = "system";
    public final static String ATTR_USER = "user";
    public final static String ATTR_PASSWORD = "password";
    public final static String ATTR_COMMENTS = "comments";
    public final static String ATTR_CUSTOM = "custom";
    public final static String ATTR_INFO = "info";

    private PassCategory passCategory;
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

    /**
     * Constructor for PassNote
     * @param passCategory
     * @param system
     * @param user
     * @param password
     * @param comments
     * @param custom
     * @param info
     */
    public PassNote(PassCategory passCategory, String system, String user, String password, String comments, String custom, String info) {
        this();
        this.passCategory = passCategory;
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

        if (passCategory == null)
            res += "passCategory=null,";
        else
            res += "passCategory=" + passCategory.toString() + ",";
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

    public PassCategory getPassCategory() {
        return passCategory;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PassNote passNote = (PassNote) o;

        if (passCategory != null ? !passCategory.equals(passNote.passCategory) : passNote.passCategory != null)
            return false;
        if (system != null ? !system.equals(passNote.system) : passNote.system != null) return false;
        if (user != null ? !user.equals(passNote.user) : passNote.user != null) return false;
        if (password != null ? !password.equals(passNote.password) : passNote.password != null) return false;
        if (comments != null ? !comments.equals(passNote.comments) : passNote.comments != null) return false;
        if (custom != null ? !custom.equals(passNote.custom) : passNote.custom != null) return false;
        return !(info != null ? !info.equals(passNote.info) : passNote.info != null);
    }

    @Override
    public int hashCode() {
        int result = passCategory != null ? passCategory.hashCode() : 0;
        result = 31 * result + (system != null ? system.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + (custom != null ? custom.hashCode() : 0);
        result = 31 * result + (info != null ? info.hashCode() : 0);
        return result;
    }

    public Map<String, String> getNoteAttr() {
        Map<String, String> result = new LinkedHashMap<>();
        result.put(ATTR_SYSTEM, system);
        result.put(ATTR_USER, user);
        result.put(ATTR_PASSWORD, password);
        result.put(ATTR_COMMENTS, comments);
        result.put(ATTR_CUSTOM, custom);
        result.put(ATTR_INFO, info);
        return result;
    }
}
