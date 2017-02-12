package de.jjff.flooose.memorypop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.jjff.flooose.memorypop.daggerstuff.ActivityComponent;
import de.jjff.flooose.memorypop.daggerstuff.ActivityModule;
import de.jjff.flooose.memorypop.daggerstuff.Blub;
import de.jjff.flooose.memorypop.daggerstuff.DaggerActivityComponent;
import de.jjff.flooose.memorypop.services.DataService;


public class NewEntryActivity extends AppCompatActivity implements Blub {

    private DictionaryEntry currentEntry;
    private View playButton;
    private View newWordButton;
    public boolean editing;
    public List<DictionaryEntry> dictionaryEntries;

    ActivityComponent component;

    @Inject
    DataService firebaseDataService;

    @BindView(R.id.newWordLayout) View newWordLayout;
    @BindView(R.id.newWord) EditText newWord;
    @BindView(R.id.newWordDefinition) EditText newWordDefinition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
        ButterKnife.bind(this);

        component = DaggerActivityComponent.builder().activityModule(new ActivityModule(this)).build();
        component.inject(this);

        dictionaryEntries = firebaseDataService.getDictionaryData();
    }

//    @OnClick(R.id.reveal_definition)
//    public void toggleDefinition() {
//        TextView wordView = (TextView) findViewById(R.id.entry_word);
//
//        if (definitionToggle.getText().equals(getString(R.string.reveal_definition))) {
//            definitionToggle.setText(getString(R.string.ok));
//            wordView.setText(currentEntry.mDefinition);
//        } else {
//            definitionToggle.setText(getString(R.string.reveal_definition));
//            wordView.setText(currentEntry.mWord);
//
//        }
//    }

    // TODO Legacy, This needs to be removed from the interface
    //@OnClick(R.id.ok)
    public void displayRandomWord() {
//        int currentEntryIndex = new Random().nextInt(dictionaryEntries.size());
//        currentEntry = dictionaryEntries.get(currentEntryIndex);
//
//        TextView wordView = (TextView) findViewById(R.id.entry_word);
//        wordView.setText(currentEntry.mWord);
//        definitionToggle.setText(getString(R.string.reveal_definition));

    }

    @OnClick(R.id.submitNewWord)
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

    public void resetFrom() {
        newWordDefinition.setText("");
        newWord.setText("");
        newWord.requestFocus();
    }

    @OnClick(R.id.cancelNewWord)
    public void cancelNewWord(View view) {
        newWordDefinition.setText("");
        newWord.setText("");
        finish();
    }

//    @OnClick(R.id.editEntry)
//    public void editEntry(View view) {
//        editing = true;
//        newWord.setText(currentEntry.mWord);
//        newWordDefinition.setText(currentEntry.mDefinition);
//        newWordLayout.setVisibility(View.VISIBLE);
//    }

    @Override
    public boolean isEditing() {
        return editing;
    }

    @Override
    public void setEditing(boolean b) {
        this.editing = b;
    }

    @Override
    public void setDictionaryEntries(List<DictionaryEntry> de) {
        this.dictionaryEntries = de;
    }

    public void signInOrUp() {
        Intent intent = new Intent(this, SigninOrUpActivity.class);
        startActivity(intent);
    }
}
