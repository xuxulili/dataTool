package com.xuxu.datatool.Model;

import com.andexert.library.RippleView;

/**
 * Created by Administrator on 2015/9/22.
 */
public class BWeather {
    private String cityName;
    private String weather;
    private String wind;
    private String temperature;
    private String date;
    private String iconDay;
    private String iconNight;
    public String toString(){
        return cityName+weather+wind+temperature+date+iconDay+iconNight;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setIconDay(String iconDay) {
        this.iconDay = iconDay;
    }

    public void setIconNight(String iconNight) {
        this.iconNight = iconNight;
    }

    public String getCityName() {
        return cityName;
    }

    public String getWeather() {
        return weather;
    }

    public String getWind() {
        return wind;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getDate() {
        return date;
    }

    public String getIconDay() {
        return iconDay;
    }

    public String getIconNight() {
        return iconNight;
    }
}
