package uz.exemple.notesapp_realm_java;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import uz.exemple.notesapp_realm_java.adapter.NotesAdapter;
import uz.exemple.notesapp_realm_java.managers.RealmManager;
import uz.exemple.notesapp_realm_java.model.Notes;

public class MainActivity extends AppCompatActivity {

    FrameLayout btn_add;
    RecyclerView recyclerView;
    EditText et_note;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    void initViews() {
        context = this;
        btn_add = findViewById(R.id.btn_add);
        recyclerView = findViewById(R.id.recyclerview);
        et_note = findViewById(R.id.et_note);

        GridLayoutManager manager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(manager);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlert();
            }
        });
        NotesAdapter adapter = new NotesAdapter(this,getNotes());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        Log.d("@@@","Posittion - "+position);
                        Notes item = getNotes().get(position);
                        updateAlert(item.getId(),item.getNote());
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

    }



    void openAlert(){
        EditText editText = new EditText(this);
        editText.setHint("Enter Your Note");
        editText.setHintTextColor(Color.parseColor("#C6C6C6"));
        editText.setPadding(32,0,16,32);
        editText.setHeight(100);
        editText.setCursorVisible(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            editText.setTextCursorDrawable(R.drawable.ic_cursor);
        }
        TextView titleView = new TextView(context);
        titleView.setText("New Note");
        titleView.setGravity(Gravity.LEFT);
        titleView.setPadding(20, 20, 20, 5);
        titleView.setTextSize(20F);
        titleView.setTypeface(Typeface.DEFAULT_BOLD);
        //titleView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        titleView.setTextColor(Color.parseColor("#00C6AE"));
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCustomTitle(titleView)
                .setView(editText)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String note = editText.getText().toString().trim();
                        if (!note.isEmpty()){
                            saveNotes(note);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                        }

                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#00C6AE"));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00C6AE"));
            }
        });
        dialog.show();
    }

    void saveNotes(String noteT) {
        int newId = getUniqueId();
        Notes note = new Notes(newId, noteT,getTime());
        RealmManager.getInstance().saveNotes(note);
    }

    void updateAlert(int id, String note){
        EditText editText = new EditText(this);
        editText.setText(note);
        editText.setCursorVisible(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            editText.setTextCursorDrawable(R.drawable.ic_cursor);
        }
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Update Note")
                .setView(editText)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String note = editText.getText().toString();
                        updateNote(id,note);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        RealmManager.getInstance().deleteNote(id);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                })
                .setNeutralButton("Cancel", null)
                .create();
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#00C6AE"));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00C6AE"));
                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#00C6AE"));
            }
        });
        dialog.show();
    }

    void updateNote(int id,String noteT) {
        int newId = id;
        Notes note = new Notes(newId, noteT,getTime());
        RealmManager.getInstance().saveNotes(note);
    }


    ArrayList<Notes> getNotes(){
        ArrayList<Notes> notesR = RealmManager.getInstance().loadNotes();
        ArrayList<Notes> notes = new ArrayList<>();
        for (Notes n:notesR){
            notes.add(n);
        }
        return notes;
    }
    String getTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("LLL dd");
        String dateT = simpleDateFormat.format(calendar.getTime()).toString();
        return dateT;
    }

    public int getUniqueId(){
        ArrayList<Notes> notes = getNotes();
        if (notes.isEmpty() || notes == null){
            return 1;
        }
        Notes item = notes.get(notes.size()-1);
        return item.getId() + 1;

    }
    /*class UserAsyncTask extends AsyncTask<User, Void, List<User>> {
        UserRepository repository;

        UserAsyncTask(UserRepository repository) {
            this.repository = repository;
        }

        @Override
        protected List<User> doInBackground(User... users) {
            repository.saveUser(users[0]);
            return repository.getUsers();
        }

        @Override
        protected void onPostExecute(List<User> users) {
            super.onPostExecute(users);
            tv_size.setText("Room DB`s users size is " + users.size());
        }
    }*/
}