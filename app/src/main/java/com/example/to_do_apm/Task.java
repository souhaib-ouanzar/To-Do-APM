package com.example.to_do_apm;

import java.io.Serializable;

public class Task implements Serializable {
    private String title;
    private String description;
    private String date;
    private String priority;
    private boolean isChecked;

    public Task(String title, String description, String date, String priority) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.priority = priority;
        this.isChecked = false;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getPriority() {
        return priority;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}