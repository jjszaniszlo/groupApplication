package com.ateam.onpoint.gui.content;

import com.ateam.onpoint.core.TaskManager;
import com.ateam.onpoint.gui.components.TaskList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.Objects;

/**
 * The task view is responsible for the nodes used to display tasks.
 */
public class TaskView implements IContent {
    private final TaskList taskList;

    public TaskView() {
        this.taskList = new TaskList();
        this.taskList.setCellFactory(p -> new TaskList.TaskCell());
        this.taskList.setPrefWidth(400);

        // listen to when the TaskManager adds a new task.
        TaskManager.getInstance().tasksListAddListener(change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    int st = change.getFrom();
                    int ed = change.getTo();
                    ObservableList<TaskManager.Task> tasks = (ObservableList<TaskManager.Task>) change.getList();

                    for (int i = st; i < ed; i++) {
                        TaskList.TaskRecord rec = new TaskList.TaskRecord(i, true);
                        rec.description = tasks.get(i).getDescription();
                        rec.completed = tasks.get(i).getCompleted();

                        this.taskList.getItems().add(rec);
                    }
                }
            }
        });

        // copy and interpret data from the TaskManager
        ObservableList<TaskList.TaskRecord> tasks = FXCollections.observableArrayList();

        for (int i = 0; i < TaskManager.getInstance().getInboxTaskList().size(); i++) {
            TaskList.TaskRecord rec = new TaskList.TaskRecord(i);
            rec.description = TaskManager.getInstance().getInboxTaskList().get(i).getDescription();
            rec.completed = TaskManager.getInstance().getInboxTaskList().get(i).getCompleted();

            tasks.add(rec);
        }

        this.taskList.setItems(tasks);
    }
    /**
     * build and return the content view for the task system
     * @return the parent node for the task system
     */
    @Override
    public Parent getContentView() {
        VBox parent = new VBox();
        parent.setPadding(new Insets(0, 0, 0, 10));

        ContentHeader header = new ContentHeader("Tasks");

        ToolBar toolbar = new ToolBar();
        toolbar.setPrefWidth(400);
        toolbar.setPrefHeight(40);

        Button newButton = new Button();
        Image plusIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/plus_white_32.png")));
        ImageView plusIconView = new ImageView(plusIcon);
        newButton.setGraphic(plusIconView);
        newButton.setPrefSize(24, 24);
        newButton.setPadding(new Insets(1, 1, 1, 1));

        EventHandler<ActionEvent> addTaskButtonEventHandler = e -> {
            TaskManager.getInstance().addTask(new TaskManager.Task());
            this.taskList.getSelectionModel().selectLast();
        };

        newButton.setOnAction(addTaskButtonEventHandler);

        Button archiveButton = new Button();
        Image archiveIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/archive_white_24.png")));
        ImageView archiveIconView = new ImageView(archiveIcon);
        archiveButton.setGraphic(archiveIconView);
        archiveButton.setPrefSize(24, 24);
        archiveButton.setPadding(new Insets(1, 1, 1 , 1));

        toolbar.getItems().addAll(newButton, archiveButton);

        parent.getChildren().addAll(header, toolbar, this.taskList);
        return parent;
    }
}
