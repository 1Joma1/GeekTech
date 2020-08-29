package com.joma.geektech.model;

import java.util.List;

public class Homework {
    private String lesson;
    private List<String> tasks;

    public Homework() {
    }

    public Homework(String lesson, List<String> tasks) {
        this.lesson = lesson;
        this.tasks = tasks;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public List<String> getTasks() {
        return tasks;
    }

    public void setTasks(List<String> tasks) {
        this.tasks = tasks;
    }
}
