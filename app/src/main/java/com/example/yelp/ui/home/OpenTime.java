package com.example.yelp.ui.home;

import java.io.Serializable;

public class OpenTime implements Serializable {
    private boolean is_overnight;
    private String start;
    private String end;
    private int day;

    public boolean isIs_overnight() {
        return is_overnight;
    }

    public void setIs_overnight(boolean is_overnight) {
        this.is_overnight = is_overnight;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}