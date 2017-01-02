package de.jjff.flooose.memorypop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 29.12.16.
 */
public class StaticDictionaryService {
    private VocabDictionary defaultDictionary;

    public static VocabDictionary getDefaultDictionary() {
        return new VocabDictionary();
    }

    public static class VocabDictionary {
        public List<DictionaryEntry> dictionaryEntries = new ArrayList<>();

        public VocabDictionary() {
            dictionaryEntries.add(new DictionaryEntry("das Beil", "axe; hatchet"));
            dictionaryEntries.add(new DictionaryEntry("hineingeraten", "to blunder into sth."));
            dictionaryEntries.add(new DictionaryEntry("Weite Kreise ziehen", "sich ausbreiten; immer mehr Menschen betreffen"));
            dictionaryEntries.add(new DictionaryEntry("die Attrappe", "mock; dummy; mockup"));
        }

        public void toJson() {

        }
    }

}
