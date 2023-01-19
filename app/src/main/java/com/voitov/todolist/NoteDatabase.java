package com.voitov.todolist;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {
    public static final String DB_NAME = "testDatabase.db";
    private static NoteDatabase instance = null;

    public static NoteDatabase getInstance(Application application) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            application,
                            NoteDatabase.class,
                            DB_NAME
                    )
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract NotesDAO getNotesDAO();
}
