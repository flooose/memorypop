package de.jjff.flooose.memorypop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

@RunWith(JUnit4.class)
public class DictionaryEntryTest {

    @Test
    public void equals_returnsTrueWhen_DictionaryEntriesAreComparedAnd_mWordIsTheSame() {
        DictionaryEntry entry1 = new DictionaryEntry("Word", null);
        DictionaryEntry entry2 = new DictionaryEntry("Word", "Definition");

        assertTrue(entry1.equals(entry2));
    }

    @Test
    public void equals_ignoresCase_WhenComparingWords() {
        DictionaryEntry entry1 = new DictionaryEntry("Word", null);
        DictionaryEntry entry2 = new DictionaryEntry("word", "Definition");

        assertTrue(entry1.equals(entry2));
    }

    @Test
    public void equals_returnsFalse_WhenObj_isNotADictionaryEntry() {
        DictionaryEntry entry1 = new DictionaryEntry("Word", null);
        String entry2 = "A String";

        assertFalse(entry1.equals(entry2));
    }

    @Test
    public void dictionaryEntryLists_returnADifferentInstance_thatEqualsTheFirstInstance() {
        DictionaryEntry entry1 = new DictionaryEntry("Word", null);
        DictionaryEntry entry2 = new DictionaryEntry("woord", "Definition");
        DictionaryEntry entry3 = new DictionaryEntry("wooord", "Definition");
        DictionaryEntry entry4 = new DictionaryEntry("woooord", "Definition");
        DictionaryEntry notInList = new DictionaryEntry("word", "Definition");

        ArrayList<DictionaryEntry> dictionaryEntries = new ArrayList<>();
        dictionaryEntries.add(entry1);
        dictionaryEntries.add(entry2);
        dictionaryEntries.add(entry3);
        dictionaryEntries.add(entry4);

        dictionaryEntries.contains(notInList);
    }

}
