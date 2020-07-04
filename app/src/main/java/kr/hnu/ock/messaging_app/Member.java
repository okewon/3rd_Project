package kr.hnu.ock.messaging_app;

import java.io.Serializable;

public class Member implements Serializable {
    private String id;
    private String pw;
    private String name;
    private String major;
    private String img_src;

    public Member() {
    }

    public Member(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Member(String id, String pw, String name, String major, String img_src) {
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.major = major;
        this.img_src = img_src;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getImg_src() {
        return img_src;
    }

    public void setImg_src(String img_src) {
        this.img_src = img_src;
    }
}
