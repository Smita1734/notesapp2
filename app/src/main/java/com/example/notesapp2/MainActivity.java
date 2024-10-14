package com.example.notesapp2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private EditText noteTitleInput, noteContentInput;
    private Button saveButton;
    private LinearLayout notesListLayout;
    private com.example.notesapp2.NotesDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteTitleInput = findViewById(R.id.note_input);
        noteContentInput = findViewById(R.id.note_input1);
        saveButton = findViewById(R.id.save_button);
        notesListLayout = findViewById(R.id.notes_list);

        dbHelper = new com.example.notesapp2.NotesDatabaseHelper(this);

        loadNotes();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = noteTitleInput.getText().toString().trim();
                String content = noteContentInput.getText().toString().trim();

                if (!title.isEmpty() && !content.isEmpty()) {
                    boolean isSaved = dbHelper.saveNote(title, content);
                    if (isSaved) {
                        Toast.makeText(MainActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                        noteTitleInput.setText("");
                        noteContentInput.setText("");
                        loadNotes();
                    } else {
                        Toast.makeText(MainActivity.this, "Error saving note", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please enter both title and content", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Load notes from database and display them in the scrollable list
    private void loadNotes() {
        notesListLayout.removeAllViews();
        ArrayList<HashMap<String, String>> notesList = dbHelper.getAllNotes();

        for (HashMap<String, String> note : notesList) {
            View noteView = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, null);

            TextView titleView = noteView.findViewById(android.R.id.text1);
            TextView contentView = noteView.findViewById(android.R.id.text2);

            titleView.setText(note.get("title"));
            contentView.setText(note.get("content"));

            notesListLayout.addView(noteView);
        }
    }
}
