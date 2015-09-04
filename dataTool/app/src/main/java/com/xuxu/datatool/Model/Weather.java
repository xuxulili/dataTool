package com.xuxu.datatool.Model;

/**
 * Created by Administrator on 2015/8/15.
 */
public class Weather  {
    public String date;
    public String weatherIcon;
    public String wdesc;
    public String temp;
    public String WeatherCityName;
    public String toString() {
        return date + weatherIcon + wdesc + temp + WeatherCityName;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public void setWdesc(String wdesc) {
        this.wdesc = wdesc;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void setWeatherCityName(String weatherCityName) {
        WeatherCityName = weatherCityName;
    }

    public String getDate() {
        return date;
    }

    public String getWeatherCityName() {
        return WeatherCityName;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public String getWdesc() {
        return wdesc;
    }

    public String getTemp() {
        return temp;
    }
}
