package com.joma.geektech.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Homework implements Serializable {
    private String id;
    private String lesson;
    private String group;
    private List<String> tasks = new ArrayList<>();

    public Homework() {
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void addTask(String task){
        this.tasks.add(task);
    }
}
