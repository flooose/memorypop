package de.jjff.flooose.memorypop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class PlayActivity extends AppCompatActivity implements Blub {

    @BindView(R.id.play_activity_reveal_definition)
    Button definitionToggle;

    private DictionaryEntry currentEntry;
    ActivityComponent component;
    private List<DictionaryEntry> dictionaryEntries;

    @Inject
    DataService firebaseDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);

        component = DaggerActivityComponent.builder().activityModule(new ActivityModule(this)).build();
        component.inject(this);
    }

    @OnClick(R.id.play_activity_reveal_definition)
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


    @Override
    public void signInOrUp() {

    }

    @Override
    public void resetFrom() {

    }

    @Override
    public void cancelNewWord(View view) {

    }

    @Override
    @OnClick(R.id.ok)
    public void displayRandomWord() {
        int currentEntryIndex = new Random().nextInt(dictionaryEntries.size());
        currentEntry = dictionaryEntries.get(currentEntryIndex);

        TextView wordView = (TextView) findViewById(R.id.entry_word);
        wordView.setText(currentEntry.mWord);
        definitionToggle.setText(getString(R.string.reveal_definition));

    }

    @Override
    public void setDictionaryEntries(List<DictionaryEntry> de) {
        this.dictionaryEntries = de;
    }

    @Override
    public boolean isEditing() {
        return false;
    }

    @Override
    public void setEditing(boolean b) {

    }
}
