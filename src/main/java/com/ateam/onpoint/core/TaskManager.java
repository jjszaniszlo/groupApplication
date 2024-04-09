package com.ateam.onpoint.core;

import javafx.beans.property.ReadOnlyObjectWrapper;

import java.util.ArrayList;

public class TaskManager {
    private final ArrayList<Task> tasks = new ArrayList<>();
    public TaskManager() {}

    private static class Task {
        private String description;
        public Task(String desc) {
            this.description = desc;
        }
    }
}
