package com.xuxu.datatool.Model;

/**
 * Created by Administrator on 2015/9/23.
 */
public class AppStoreWeather {
    private  String cityName;
    private String date;
    private String temperature;
    private String weather;
    private String wind;
    private String sun;
    private String longitude_latitude;
    private String altitude;
    public String toString() {
        return cityName + date + weather+temperature+wind+sun+longitude_latitude+altitude;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public void setSun(String sun) {
        this.sun = sun;
    }

    public void setLongitude_latitude(String longitude_latitude) {
        this.longitude_latitude = longitude_latitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getCityName() {
        return cityName;
    }

    public String getDate() {
        return date;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getWeather() {
        return weather;
    }

    public String getWind() {
        return wind;
    }

    public String getSun() {
        return sun;
    }

    public String getLongitude_latitude() {
        return longitude_latitude;
    }

    public String getAltitude() {
        return altitude;
    }
}
