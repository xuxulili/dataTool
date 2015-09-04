package com.xuxu.datatool.Model;

/**
 * Created by Administrator on 2015/8/20.
 */
public class GeekNews {
    public String jk_title;
    public String jk_url;
    public String jk_label;
    public String jk_userName;
    public String jk_userNameImg;
    public String jk_time;
    public int jk_type;

    public String toString() {
        return jk_title + jk_url + jk_label + jk_userName + jk_userNameImg + jk_type;
    }
    public void setJk_title(String jk_title) {
        this.jk_title = jk_title;
    }

    public void setJk_url(String jk_url) {
        this.jk_url = jk_url;
    }

    public void setJk_label(String jk_label) {
        this.jk_label = jk_label;
    }

    public void setJk_userName(String jk_userName) {
        this.jk_userName = jk_userName;
    }

    public void setJk_userNameImg(String jk_userNameImg) {
        this.jk_userNameImg = jk_userNameImg;
    }

    public void setJk_time(String jk_time) {
        this.jk_time = jk_time;
    }

    public void setJk_type(int jk_type) {
        this.jk_type = jk_type;
    }

    public String getJk_title() {
        return jk_title;
    }

    public String getJk_url() {
        return jk_url;
    }

    public String getJk_label() {
        return jk_label;
    }

    public String getJk_userName() {
        return jk_userName;
    }

    public String getJk_userNameImg() {
        return jk_userNameImg;
    }

    public String getJk_time() {
        return jk_time;
    }

    public int getJk_type() {
        return jk_type;
    }
}
