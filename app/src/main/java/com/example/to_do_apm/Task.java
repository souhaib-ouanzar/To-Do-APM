package com.example.to_do_apm;

public class Task {
    private String title;
    private String description;
    private String date;
    private boolean isChecked;

    public Task(String title, String description, String date) {
        this.title = title;
        this.description = description;
        this.date = date;
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}