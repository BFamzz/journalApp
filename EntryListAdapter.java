package com.example.android.journalapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.journalapp.R;
import com.example.android.journalapp.database.DatabaseHandler;
import com.example.android.journalapp.model.Entry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class EntryListAdapter extends RecyclerView.Adapter<EntryListAdapter.ViewHolder> {

    private ArrayList<Entry> entryList;
    private Context context;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;


    public EntryListAdapter(ArrayList<Entry> entryList, Context context) {
        this.entryList = entryList;
        this.context = context;
    }

    @NonNull
    @Override
    public EntryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_row, parent, false);
        EntryListAdapter.ViewHolder holder = new EntryListAdapter.ViewHolder(view, context, entryList);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EntryListAdapter.ViewHolder holder, int position) {

        holder.bindView(entryList.get(position));

        // TODO ---> Continue here
//        mAuth = FirebaseAuth.getInstance();
//        mCurrentUser = mAuth.getCurrentUser();
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//
//        String userId = mDatabase.getRef().getKey();

    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Context mContext;
        ArrayList<Entry> mList;
        ImageView entryPic;
        TextView entrySubject;
        TextView entryMessageSnippet;
        Button entryEditButton;
        Button entryDeleteButton;
        TextView entryDate;

        public ViewHolder(View itemView, Context context, ArrayList<Entry> list) {
            super(itemView);

            mContext = context;
            mList = list;

            // Map the views
            entryPic = itemView.findViewById(R.id.imageView);
            entrySubject = itemView.findViewById(R.id.list_row_entry_subject);
            entryMessageSnippet = itemView.findViewById(R.id.list_row_entry_message);
            entryEditButton = itemView.findViewById(R.id.list_row_edit_button);
            entryDeleteButton = itemView.findViewById(R.id.list_row_delete_button);
            entryDate = itemView.findViewById(R.id.list_row_entry_date);
        }

        public void bindView(Entry entryList) {
            entrySubject.setText(entryList.getSubject());
            entryMessageSnippet.setText(entryList.getMessage());
            entryDate.setText(entryList.getDateEntered());

            entryDeleteButton.setOnClickListener(this);
            entryEditButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int mPosition = getAdapterPosition();
            Entry entry = mList.get(mPosition);
            switch (v.getId()) {
                case R.id.list_row_delete_button:
                    // TODO ----> Confirm if the line below is correct
                    deleteChore(entry.getId());
                    mList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    break;

                case R.id.list_row_edit_button:
                    editChore(entry);
                    break;

            }
        }

        public void deleteChore(int id) {
            DatabaseHandler db = new DatabaseHandler(mContext);
            db.deleteEntry(id);
        }

        public void editChore(final Entry entry) {
            AlertDialog.Builder dialogBuilder;
            final AlertDialog dialog;
            final DatabaseHandler dbHandler = new DatabaseHandler(context);

            View view = LayoutInflater.from(context).inflate(R.layout.pop_up, null);
            final TextView popEntryHeader = view.findViewById(R.id.dialog_pop_entry_header);
            final EditText popEntrySubject = view.findViewById(R.id.dialog_pop_entry_subject);
            final EditText popEntryMessage = view.findViewById(R.id.dialog_pop_entry_message);
            Button popSaveButton = view.findViewById(R.id.dialog_pop_save_button);

            dialogBuilder = new AlertDialog.Builder(context).setView(view);
            dialog = dialogBuilder.create();
            dialog.show();

            popSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String entryHeader = popEntryHeader.getText().toString().trim();
                    String entrySubject = popEntrySubject.getText().toString().trim();
                    String entryMessage = popEntryMessage.getText().toString().trim();

                    // check if the values are not empty
                    if (!TextUtils.isEmpty(entryHeader) && !TextUtils.isEmpty(entrySubject) &&
                            !TextUtils.isEmpty(entryMessage)) {

                        entry.setSubject(entrySubject);
                        entry.setMessage(entryMessage);

                        dbHandler.updateEntry(entry);
                        notifyItemChanged(getAdapterPosition(), entry);
                        dialog.dismiss();
                    }
                }
            });
        }
    }
}
