package com.xuxu.datatool.Model;

/**
 * Created by Administrator on 2015/8/28.
 */
public class JobNews {
    public String job_title;
    public String  job_time;
    public String job_url;
    public String toString() {
        return job_title + job_time + job_url;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public void setJob_time(String job_time) {
        this.job_time = job_time;
    }

    public void setJob_url(String job_url) {
        this.job_url = job_url;
    }

    public String getJob_title() {
        return job_title;
    }

    public String getJob_time() {
        return job_time;
    }

    public String getJob_url() {
        return job_url;
    }
}
