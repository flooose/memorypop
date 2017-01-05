package de.jjff.flooose.memorypop;

/**
 * Created by chris on 02.01.17.
 */
public class DictionaryEntry {
    public String mWord;
    public String mDefinition;

    public DictionaryEntry() {    }
    public DictionaryEntry(String word, String definition) {
        this.mWord = word;
        this.mDefinition = definition;
    }

    public void setmWord(String word) {
        this.mWord = word;
    }

    public void setmDefinition(String definition) {
        this.mDefinition = definition;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DictionaryEntry && ((DictionaryEntry) obj).mWord.equalsIgnoreCase(mWord);
    }
}
