package com.voitov.todolist;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DatabaseFake {
    private ArrayList<Note> notes;

    private static DatabaseFake databaseFake;

    public static DatabaseFake getInstance() {
        if (databaseFake == null) {
            databaseFake = new DatabaseFake();
        }
        return databaseFake;
    }

    private DatabaseFake() {
        notes = generateRandomNotes(10);
    }

    private ArrayList<Note> generateRandomNotes(int count) {
        ArrayList<Note> notes = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            notes.add(new Note("someText " + i, Priority.values()[rand.nextInt(3)]));
        }

        return notes;
    }

    public void add(Note note) {
        notes.add(note);
    }

    public void remove(int id) {
        for (Note note : notes) {
            if (note.getId() == id) {
                notes.remove(note);
                break;
            }
        }

        Log.d("Database", getCount() + "");
    }

    public List<Note> getNotes() {
        return new ArrayList<>(notes);
    }

    public int getCount() {
        return notes.size();
    }
}
