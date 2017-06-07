package de.jjff.flooose.memorypop.daggerstuff;

import android.view.View;

import com.google.firebase.database.DataSnapshot;

import java.util.List;

import de.jjff.flooose.memorypop.DictionaryEntry;


public interface Blub {
    void signInOrUp();

    void resetFrom();

    void cancelNewWord(View view);

    void displayRandomWord();

    void setDictionaryData(DataSnapshot de);

    boolean isEditing();

    void setEditing(boolean b);
}
