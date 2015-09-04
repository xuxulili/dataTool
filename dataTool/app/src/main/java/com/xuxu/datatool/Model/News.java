package com.xuxu.datatool.Model;

/**
 * Created by Administrator on 2015/7/28.
 */
public class News {

    public static interface NewsType {
        public static final int TITLE = 1;
        public static final int SUMMARY = 2;
        public static final int CONTENT = 3;
        public static final int IMG = 4;
    }

    /**
     * 标题
     */
    private String title;
    /**
     * 摘要
     */
    private String littleTitle;
    /**
     * 内容
     */
    private String content;

    /**
     * 图片链接
     */
    private String imageLink;

    /**
     * 类型
     */
    private int type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.type = NewsType.TITLE;
    }

    public String getlittleTitle() {
        return littleTitle;
    }

    public void setlittleTitle(String littleTitle) {
        this.littleTitle = littleTitle;
        this.type = NewsType.SUMMARY;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.type = NewsType.CONTENT;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
        this.type = NewsType.IMG;

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "News [cnews_details_title=" + title + ", littleTitle=" + littleTitle + ", content=" + content + ", imageLink=" + imageLink
                + ", type=" + type + "]";
    }


}