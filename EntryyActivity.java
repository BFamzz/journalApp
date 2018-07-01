package com.example.android.journalapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.journalapp.R;
import com.example.android.journalapp.database.DatabaseHandler;
import com.example.android.journalapp.model.Entry;

public class EntryyActivity extends AppCompatActivity {

    private TextView entryHeader;
    private DatabaseHandler dbHandler;
    private Button saveButton;
    private EditText entrySubject;
    private EditText entryMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entryy);
        dbHandler = new DatabaseHandler(this);
        saveButton = findViewById(R.id.dialog_pop_save_button);
        entrySubject = findViewById(R.id.dialog_pop_entry_subject);
        entryMessage = findViewById(R.id.dialog_pop_entry_message);


        // set the onclick listener of the button to save the entry
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(entrySubject.getText().toString()) &&
                        !TextUtils.isEmpty(entryMessage.getText().toString())) {
                    Entry entry = new Entry();
                    entry.setId(entry.getId());
                    entry.setSubject(entrySubject.getText().toString());
                    entry.setMessage(entryMessage.getText().toString());
                    saveToDatabase(entry);
                }

                startActivity(new Intent(getApplicationContext(), EntryListActivity.class));
                finish();
            }
        });
    }

    public void saveToDatabase(Entry entry) {
        dbHandler.createEntry(entry);
    }

}
