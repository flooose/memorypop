package de.jjff.flooose.memorypop.services;

import android.app.Activity;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import de.jjff.flooose.memorypop.DictionaryEntry;


public interface DataService {
    DataSnapshot getDictionaryData();

    void setValue(List<DictionaryEntry> dictionaryEntries);

    void stop();

    void addValue(DictionaryEntry dictionaryEntry);

    void updateValue(DictionaryEntry dictionaryEntry);

    DictionaryEntry getRandomWord();
}
