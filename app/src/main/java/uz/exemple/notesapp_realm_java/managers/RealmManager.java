package uz.exemple.notesapp_realm_java.managers;

import android.content.Context;
import android.util.Log;


import org.bson.types.ObjectId;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import uz.exemple.notesapp_realm_java.model.Notes;

public class RealmManager {
    public static final String TAG = RealmManager.class.getSimpleName();
    private static RealmManager realmManager;
    private static Realm realm;

    public static RealmManager getInstance() {
        if (realmManager == null) {
            realmManager = new RealmManager();
        }
        return realmManager;
    }

    public RealmManager() {
        realm = Realm.getDefaultInstance();
    }

    public void saveNotes(Notes note) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(note);
        realm.commitTransaction();
    }

    public ArrayList<Notes> loadNotes() {
        ArrayList<Notes> notes = new ArrayList<>();
        RealmResults<Notes> results = realm.where(Notes.class).findAll();
        for (Notes note : results) {
            notes.add(note);
        }
        return notes;
    }

    public void deleteNote(int id){
        realm.executeTransaction( transactionRealm -> {
            Notes item = transactionRealm.where(Notes.class).equalTo("id", id).findFirst();
            item.deleteFromRealm();
        });
    }




}
