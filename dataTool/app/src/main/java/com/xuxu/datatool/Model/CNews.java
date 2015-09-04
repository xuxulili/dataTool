package com.xuxu.datatool.Model;

/**
 * Created by Administrator on 2015/8/6.
 */
public class CNews {
    private String cTitle;
    private String cUrl;
    private String cTime;

    public String toString() {
        return cTitle + cTime + cUrl;
    }
    public void setcTitle(String cTitle) {
        this.cTitle = cTitle;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime;
    }

    public void setcUrl(String cUrl) {
        this.cUrl = cUrl;
    }

    public String getcTitle() {

        return cTitle;
    }

    public String getcUrl() {
        return cUrl;
    }

    public String getcTime() {
        return cTime;
    }
}
