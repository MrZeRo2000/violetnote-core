package com.romanpulov.violetnotecore.Model;

import java.util.Date;
import java.util.Objects;

public class PassNote2 {

    public final static String ATTR_SYSTEM = "system";
    public final static String ATTR_USER = "user";
    public final static String ATTR_PASSWORD = "password";
    public final static String ATTR_URL = "url";
    public final static String ATTR_INFO = "info";
    public final static String ATTR_CREATED_DATE = "created date";
    public final static String ATTR_MODIFIED_DATE = "modified date";

    private String system;

    private String user;

    private String password;

    private String url;

    private String info;

    private Date createdDate;

    private Date modifiedDate;

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public static PassNote2 createEmpty() {
        return new PassNote2(
                null,
                null,
                null,
                null,
                null,
                new Date(),
                new Date()
        );
    }

    public PassNote2() {}

    public PassNote2(String system, String user, String password, String url, String info, Date createdDate, Date modifiedDate) {
        this.system = system;
        this.user = user;
        this.password = password;
        this.url = url;
        this.info = info;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PassNote2 passNote2 = (PassNote2) o;
        return system.equals(passNote2.system) &&
                user.equals(passNote2.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(system, user);
    }

    @Override
    public String toString() {
        return "PassNote2{" +
                "system='" + system + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", url='" + url + '\'' +
                ", info='" + info + '\'' +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                '}';
    }

}
