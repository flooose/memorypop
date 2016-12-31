package de.jjff.flooose.memorypop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Random;

public class NewEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        StaticDictionaryService staticJsonService = new StaticDictionaryService();
        StaticDictionaryService.VocabDictionary dictionary = StaticDictionaryService.getDefaultDictionary();

        int currentEntryIndex = new Random().nextInt(dictionary.dictionaryEntries.length);

        StaticDictionaryService.DictionaryEntry currentEntry = dictionary.dictionaryEntries[currentEntryIndex];

        TextView wordView = (TextView) findViewById(R.id.entry_word);
        wordView.setText(currentEntry.mWord);
        TextView definitionView = (TextView) findViewById(R.id.entry_definition);
        definitionView.setText(currentEntry.mDefinition);
    }
}
