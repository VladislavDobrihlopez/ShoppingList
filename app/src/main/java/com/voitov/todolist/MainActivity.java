package com.voitov.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private RecyclerView recyclerViewNotes;
    private FloatingActionButton buttonAdd;
    private DatabaseFake databaseFake = DatabaseFake.getInstance();
    private NotesAdapter notesAdapter = new NotesAdapter();
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        viewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                notesAdapter.setNotes(notes);
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddNoteActivity.newIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        viewModel.getCountOfPushes().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer pushes) {
                //TODO create a screen for editing a note
            }
        });

        notesAdapter.setOnNoteClickListener(new NotesAdapter.OnNoteClickListener() {
            @Override
            public void OnNoteClick(int id) {
//                database.remove(id);
//                notesAdapter.setNotes(database.getNotes());
                viewModel.markAsPushed();
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(
                        0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
                ) {
                    @Override
                    public boolean onMove(
                            @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            @NonNull RecyclerView.ViewHolder target
                    ) {
                        return false;
                    }

                    @Override
                    public void onSwiped(
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            int direction
                    ) {
                        int elementIndex = viewHolder.getAdapterPosition();
                        Note noteToBeDeleted = notesAdapter.getNotes().get(elementIndex);
                        viewModel.remove(noteToBeDeleted.getId());
                    }
                });

        itemTouchHelper.attachToRecyclerView(recyclerViewNotes);
        recyclerViewNotes.setAdapter(notesAdapter);
    }

    private void initializeViews() {
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        buttonAdd = findViewById(R.id.floatingActionButtonAdd);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}