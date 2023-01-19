package com.voitov.todolist;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface NotesDAO {
    @Query("SELECT * FROM notes")
    public LiveData<List<Note>> getNotes();

    @Insert(onConflict = REPLACE)
    public Completable add(Note note);

    @Query("DELETE FROM notes WHERE id = :id")
    public Completable remove(int id);
}
