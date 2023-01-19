package com.voitov.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class AddNoteActivity extends AppCompatActivity {
    private EditText editTextNote;
    private RadioButton radioButtonLow;
    private RadioButton radioButtonMedium;
    private Button buttonSave;

    //private DatabaseFake databaseFake = DatabaseFake.getInstance();
    private NoteDatabase database;
    //private Handler handler = new Handler(Looper.getMainLooper());
    private AddNoteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        initializeViews();

        viewModel = new ViewModelProvider(this).get(AddNoteViewModel.class);

        viewModel.getShouldCloseActivity().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean shouldClose) {
                if (shouldClose) {
                    finish();
                }
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
    }

    private void initializeViews() {
        editTextNote = findViewById(R.id.editTextNote);
        radioButtonLow = findViewById(R.id.radioButtonLow);
        radioButtonMedium = findViewById(R.id.radioButtonMedium);
        buttonSave = findViewById(R.id.buttonSave);
    }

    private void saveNote() {
        String noteTitle = editTextNote.getText().toString();
        Priority priority = getPriority();
        viewModel.add(new Note(noteTitle, priority));
    }

    private Priority getPriority() {
        Priority priority;
        if (radioButtonLow.isChecked()) {
            priority = Priority.LOW;
        } else if (radioButtonMedium.isChecked()) {
            priority = Priority.MEDIUM;
        } else {
            priority = Priority.HIGH;
        }
        return priority;
    }

    public static Intent newIntent(MainActivity context) {
        return new Intent(context, AddNoteActivity.class);
    }
}