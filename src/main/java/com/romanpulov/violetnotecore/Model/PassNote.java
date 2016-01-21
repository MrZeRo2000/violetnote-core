package com.romanpulov.violetnotecore.Model;

import java.util.Date;

/**
 * Created on 16.01.2016.
 */
public class PassNote extends PassDataItem {
    public final static String XML_TAG_NAME = "node";

    public final static String XML_TAG_ATTR_SYSTEM = "system";
    public final static String XML_TAG_ATTR_USER = "user";
    public final static String XML_TAG_ATTR_PASSWORD = "password";
    public final static String XML_TAG_ATTR_COMMENTS = "comments";
    public final static String XML_TAG_ATTR_CUSTOM = "custom";
    public final static String XML_TAG_ATTR_INFO = "info";

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
}
