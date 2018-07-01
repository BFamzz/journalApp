package com.example.android.journalapp.activities;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.android.journalapp.R;
import com.example.android.journalapp.adapter.EntryListAdapter;
import com.example.android.journalapp.database.DatabaseHandler;
import com.example.android.journalapp.model.Entry;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Iterator;

public class EntryListActivity extends AppCompatActivity {

    private EntryListAdapter adapter;
    private ArrayList<Entry> entryList;
    private ArrayList<Entry> entryListItem;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseHandler dbHandler;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_list);

        dbHandler = new DatabaseHandler(this);
        entryList = new ArrayList<>();
        entryListItem = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        adapter = new EntryListAdapter(entryListItem, this);

        // Set up the recyclerview
        recyclerView = findViewById(R.id.entryListRecyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // Read all entries
        entryList = dbHandler.readAllEntries();

        for (Entry entry : entryList) {
            Entry entry1 = new Entry();
            entry1.setSubject(entry.getSubject());
            entry1.setMessage(entry.getMessage());
            entry.setId(entry.getId());

            Log.d("Entry", entry.getSubject());

            entryListItem.add(entry1);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //super.onOptionsItemSelected(item);
        if (item != null) {
            if (item.getItemId() == R.id.add_entry) {
                startActivity(new Intent(this, EntryyActivity.class));
            }

            if (item.getItemId() == R.id.log_outId) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }

        return true;
    }

}
