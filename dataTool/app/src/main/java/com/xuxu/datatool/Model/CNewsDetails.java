package com.xuxu.datatool.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/13.
 */
public class CNewsDetails implements Serializable {
    public String CNewsDetails_title;
    public String CNewsDetails_details;
    public String CNewsDetails_content;
    public String CNewsDetails_imageUrl;
    public int CNewsDetails_type;

    public static interface CNewsDetailsType{
        public static final int TITLE = 1;
        public static final int DETAILS = 2;
        public static final int CONTENT = 3;
        public static final int IMG_URL = 4;
    }

    public String getCNewsDetails_title() {
        return CNewsDetails_title;
    }

    public String getCNewsDetails_details() {
        return CNewsDetails_details;
    }

    public String getCNewsDetails_content() {
        return CNewsDetails_content;
    }

    public String getCNewsDetails_imageUrl() {
        return CNewsDetails_imageUrl;
    }

    public int getCNewsDetails_type() {
        return CNewsDetails_type;
    }

    public void setCNewsDetails_type(int CNewsDetails_type) {
        this.CNewsDetails_type = CNewsDetails_type;
    }

    public void setCNewsDetails_imageUrl(String CNewsDetails_imageUrl) {
        this.CNewsDetails_imageUrl = CNewsDetails_imageUrl;
    }

    public void setCNewsDetails_content(String CNewsDetails_content) {
        this.CNewsDetails_content = CNewsDetails_content;
    }

    public void setCNewsDetails_details(String CNewsDetails_details) {
        this.CNewsDetails_details = CNewsDetails_details;
    }

    public void setCNewsDetails_title(String CNewsDetails_title) {
        this.CNewsDetails_title = CNewsDetails_title;
    }
public String toString(){
    String str=CNewsDetails_title+CNewsDetails_details+CNewsDetails_content+CNewsDetails_imageUrl;
  return str;
}



}
