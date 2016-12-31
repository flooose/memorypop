package de.jjff.flooose.memorypop;

/**
 * Created by chris on 29.12.16.
 */
public class StaticDictionaryService {
    private VocabDictionary defaultDictionary;

    public static VocabDictionary getDefaultDictionary() {
        return new VocabDictionary();
    }

    public static class VocabDictionary {
        public DictionaryEntry[] dictionaryEntries = {
                new DictionaryEntry("das Beil", "axe; hatchet"),
                new DictionaryEntry("hineingeraten", "to blunder into sth."),
                new DictionaryEntry("Weite Kreise ziehen", "sich ausbreiten; immer mehr Menschen betreffen"),
                new DictionaryEntry("die Attrappe", "mock; dummy; mockup")
        };

        public void toJson() {

        }
    }

    public static class DictionaryEntry {
        public String mWord;
        public String mDefinition;

        public DictionaryEntry(String word, String definition) {
            this.mWord = word;
            this.mDefinition = definition;
        }
    }
}
