package de.jjff.flooose.memorypop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import de.jjff.flooose.memorypop.daggerstuff.ActivityComponent;
import de.jjff.flooose.memorypop.daggerstuff.ActivityModule;
import de.jjff.flooose.memorypop.daggerstuff.DaggerActivityComponent;
import de.jjff.flooose.memorypop.services.DataService;


public class NewEntryActivity extends AppCompatActivity {

    private DictionaryEntry currentEntry;
    public EditText newWord;
    public EditText newWordDefinition;
    private Button submitNewWord;
    private View playLayout;
    private View newWordLayout;
    private View startLayout;
    private View playButton;
    private View newWordButton;
    private Button definitionToggle;
    public boolean editing;
    public List<DictionaryEntry> dictionaryEntries;

    ActivityComponent component;

    @Inject
    DataService firebaseDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        component = DaggerActivityComponent.builder().activityModule(new ActivityModule(this)).build();
        component.inject(this);

        playLayout = findViewById(R.id.playLayout);
        newWordLayout = findViewById(R.id.newWordLayout);
        startLayout = findViewById(R.id.startLayout);
        playButton = startLayout.findViewById(R.id.play_button);
        newWordButton = startLayout.findViewById(R.id.new_word_button);
        definitionToggle = (Button) findViewById(R.id.reveal_definition);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playLayout.setVisibility(View.VISIBLE);
                newWordLayout.setVisibility(View.GONE);

            }
        });

        newWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newWordLayout.setVisibility(View.VISIBLE);
                playLayout.setVisibility(View.GONE);

            }
        });

        newWord = (EditText) findViewById(R.id.newWord);
        newWordDefinition = (EditText) findViewById(R.id.newWordDefinition);
        submitNewWord = (Button) findViewById(R.id.submitNewWord);
    }

    public void toggleDefinition(View view) {
        toggleDefinition();
    }

    public void toggleDefinition() {
        TextView wordView = (TextView) findViewById(R.id.entry_word);

        if (definitionToggle.getText().equals(getString(R.string.reveal_definition))) {
            definitionToggle.setText(getString(R.string.ok));
            wordView.setText(currentEntry.mDefinition);
        } else {
            definitionToggle.setText(getString(R.string.reveal_definition));
            wordView.setText(currentEntry.mWord);

        }
    }

    public void displayRandomWord(View view) { displayRandomWord();}
    public void displayRandomWord() {
        int currentEntryIndex = new Random().nextInt(dictionaryEntries.size());
        currentEntry = dictionaryEntries.get(currentEntryIndex);

        TextView wordView = (TextView) findViewById(R.id.entry_word);
        wordView.setText(currentEntry.mWord);
        definitionToggle.setText(getString(R.string.reveal_definition));

    }

    public void submitNewWord(View view) {
        String word = newWord.getText().toString();
        String definition = newWordDefinition.getText().toString();

        DictionaryEntry dictionaryEntry = new DictionaryEntry(word, definition);
        int dictionaryIndex = dictionaryEntries.indexOf(dictionaryEntry);

        if (!word.isEmpty()) {
            if (dictionaryIndex >= 0) {
                dictionaryEntries.set(dictionaryIndex, dictionaryEntry);
            } else {
                dictionaryEntries.add(dictionaryEntry);
            }

            firebaseDataService.setValue(dictionaryEntries);
        }
    }

    public void cancelNewWord(View view) {
        newWordDefinition.setText("");
        newWord.setText("");
        playLayout.setVisibility(View.VISIBLE);
        newWordLayout.setVisibility(View.GONE);
    }

    public void editEntry(View view) {
        editing = true;
        newWord.setText(currentEntry.mWord);
        newWordDefinition.setText(currentEntry.mDefinition);
        newWordLayout.setVisibility(View.VISIBLE);
        playLayout.setVisibility(View.GONE);
    }
}
