package com.andela.dairyapp.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.andela.dairyapp.activities.auth.AuthActivity;
import com.andela.dairyapp.adapters.NotesAdapter;
import com.andela.dairyapp.models.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andela.dairyapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {
    FloatingActionButton fab;
    NotesAdapter notesAdapter;
    List<Note> noteList = new ArrayList<>();
    RecyclerView notesRecyclerView;
    TextView emptyTV;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Note note = new Note();
        note.set_id(new Random().nextInt(100));
        note.setColor_code(generateColor());
        note.setCreated_at(createAt());
        note.setNote_name("Demo note");
        note.setNote_description("Welcome to DiaryApp, you can check me out.");
        noteList.add(note);

        notesAdapter = new NotesAdapter(noteList);
        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        notesRecyclerView.setHasFixedSize(true);
        notesRecyclerView.setLayoutManager(linearLayoutManager);
        notesRecyclerView.setAdapter(notesAdapter);
        emptyTV = findViewById(R.id.empty_dairy);

        fab = findViewById(R.id.fab_action_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //create an instance of AlertDialog.Builder
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
                dialogBuilder.setCancelable(false);
                LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
                //set a customized view for the display
                View layoutView = layoutInflater.inflate(R.layout.input_new_layout, null);
                dialogBuilder.setView(layoutView);

                final EditText eventName = layoutView.findViewById(R.id.diary_name);
                final EditText eventDesc = layoutView.findViewById(R.id.diary_description);
                final Button cancelBtn = layoutView.findViewById(R.id.action_cancel);
                final Button saveBtn = layoutView.findViewById(R.id.action_save);

                final Dialog dialog = dialogBuilder.create();
                //make sure the dialog is opaque
                Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String event_name = eventName.getText().toString();
                        String event_desc = eventDesc.getText().toString();

                        //TODO, should be fixed

                        if (TextUtils.isEmpty(event_name) || TextUtils.isEmpty(event_desc)) {
                            Snackbar.make(v, "Sorry one or more fields are empty", Snackbar.LENGTH_LONG).show();
                        } else {
                            //TODO, save the information in a database or file :-)
                            Note note = new Note();
                            note.set_id(new Random().nextInt(100));
                            note.setNote_name(event_name);
                            note.setNote_description(event_desc);

                            note.setColor_code(generateColor());

                            note.setCreated_at(createAt());
                            boolean success = notesAdapter.addNote(note);
                            if (success) {
                                dialog.dismiss();
                                Toast.makeText(HomeActivity.this, "Note Saved!!!", Toast.LENGTH_LONG).show();
                                emptyTV.setVisibility(View.GONE);
                            } else {
                                Snackbar.make(v, "An error occured", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });
    }


    private void logout(){
        mFirebaseAuth.signOut();
        Intent loginIntent = new Intent(this, AuthActivity.class);
        startActivity(loginIntent);
        finish();
    }
    private String createAt() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
         return dateFormat.format(date);
    }

    private int generateColor() {
        Random random = new Random();
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                //create an instance of AlertDialog.Builder
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setCancelable(false);
                LayoutInflater layoutInflater = LayoutInflater.from(this);
                //set a customized view for the display
                View layoutView = layoutInflater.inflate(R.layout.input_new_layout, null);
                dialogBuilder.setView(layoutView);

                final EditText eventName = layoutView.findViewById(R.id.diary_name);
                final EditText eventDesc = layoutView.findViewById(R.id.diary_description);
                final Button cancelBtn = layoutView.findViewById(R.id.action_cancel);
                final Button saveBtn = layoutView.findViewById(R.id.action_save);

                final Dialog dialog = dialogBuilder.create();
                //make sure the dialog is opaque
                Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String event_name = eventName.getText().toString();
                        String event_desc = eventDesc.getText().toString();

                        //TODO, should be fixed

                        if (TextUtils.isEmpty(event_name) || TextUtils.isEmpty(event_desc)) {
                            Snackbar.make(v, "Sorry one or more fields are empty", Snackbar.LENGTH_LONG).show();
                        } else {
                            //TODO, save the information in a database or file :-)
                            Note note = new Note();
                            note.setNote_name(event_name);
                            note.setNote_description(event_desc);
                            Random random = new Random();
                            int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
                            note.setColor_code(color);
                            Date date = new Date();
                            DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                            String date_created = dateFormat.format(date);
                            note.setCreated_at(date_created);
                            boolean success = notesAdapter.addNote(note);
                            if (success) {
                                dialog.dismiss();
                                Toast.makeText(HomeActivity.this, "Note Saved!!!", Toast.LENGTH_LONG).show();
                                emptyTV.setVisibility(View.GONE);
                            } else {
                                Snackbar.make(v, "An error occured", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }
                });
                break;
            case R.id.sort_by_date:
                Snackbar.make(notesRecyclerView, "Sorting by Date...", Snackbar.LENGTH_LONG).show();
                break;
            case R.id.sort_by_order:
                Snackbar.make(notesRecyclerView, "Sorting by A - Z...", Snackbar.LENGTH_LONG).show();

                break;
            case R.id.sort_by_order_z_a:
                Snackbar.make(notesRecyclerView, "Sorting by Z - A...", Snackbar.LENGTH_LONG).show();
                break;
            case R.id.action_about:
                Snackbar.make(notesRecyclerView, "About", Snackbar.LENGTH_LONG).show();
                break;
            case R.id.logout:
                Snackbar.make(notesRecyclerView, "Signing Out", Snackbar.LENGTH_SHORT).show();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
