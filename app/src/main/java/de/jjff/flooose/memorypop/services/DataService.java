package de.jjff.flooose.memorypop.services;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import de.jjff.flooose.memorypop.DictionaryEntry;


public interface DataService {
    List<DictionaryEntry> getDictionaryData();

    void setValue(List<DictionaryEntry> dictionaryEntries);

    void stop();
}
