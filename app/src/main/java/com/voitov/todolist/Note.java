package com.voitov.todolist;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String text;
    private Priority priority;

    @Ignore
    public Note(String text, Priority priority) {
        this(text, priority, 0);
    }

    public Note(String text, Priority priority, int id) {
        this.id = id;
        this.text = text;
        this.priority = priority;
    }

    public String getText() {
        return text;
    }

    public Priority getPriority() {
        return priority;
    }

    public int getId() {
        return id;
    }
}
