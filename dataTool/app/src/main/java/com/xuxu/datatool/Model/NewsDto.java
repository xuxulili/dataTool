package com.xuxu.datatool.Model;

import java.util.List;

/**
 * Created by Administrator on 2015/7/28.
 */
public class NewsDto
{
    private List<News> newses;
    private String nextPageUrl ;
    public List<News> getNewses()
    {
        return newses;
    }
    public void setNewses(List<News> newses)
    {
        this.newses = newses;
    }
    public String getNextPageUrl()
    {
        return nextPageUrl;
    }
    public void setNextPageUrl(String nextPageUrl)
    {
        this.nextPageUrl = nextPageUrl;
    }


}