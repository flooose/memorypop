package de.jjff.flooose.memorypop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class NewEntryActivity extends AppCompatActivity {

    private StaticDictionaryService.DictionaryEntry currentEntry;
    private StaticDictionaryService.VocabDictionary currentDictionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        currentDictionary = StaticDictionaryService.getDefaultDictionary();

        displayRandomWord();
    }

    public void toggleDefinition(View view) {
        TextView wordView = (TextView) findViewById(R.id.entry_word);
        Button button = (Button) findViewById(R.id.reveal_definition);
        if(button.getText().equals(getString(R.string.reveal_definition))) {
            button.setText(getString(R.string.ok));
            wordView.setText(currentEntry.mDefinition);
        } else {
            button.setText(getString(R.string.reveal_definition));
            wordView.setText(currentEntry.mWord);

        }
    }

    public void nextWord(View view) {
        displayRandomWord();
    }

    private void displayRandomWord() {
        int currentEntryIndex = new Random().nextInt(currentDictionary.dictionaryEntries.length);
        currentEntry = currentDictionary.dictionaryEntries[currentEntryIndex];

        TextView wordView = (TextView) findViewById(R.id.entry_word);
        wordView.setText(currentEntry.mWord);
    }
}
