package uz.exemple.notesapp_realm_java.model;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Notes extends RealmObject {
    @PrimaryKey()
    public int id;

    public String note;

    public String date;

    public Notes() {}

    public Notes(int id, String note, String date) {
        this.id = id;
        this.note = note;
        this.date = date;
    }


    public int getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    public String getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
