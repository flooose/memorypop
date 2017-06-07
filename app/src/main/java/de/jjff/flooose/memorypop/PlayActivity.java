package de.jjff.flooose.memorypop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;

import java.util.Iterator;
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

    private DictionaryEntry mCurrentEntry;
    ActivityComponent component;
    private DataSnapshot dictionaryEntries;

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
            wordView.setText(mCurrentEntry.mDefinition);
        } else {
            definitionToggle.setText(getString(R.string.reveal_definition));
            wordView.setText(mCurrentEntry.mWord);

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
        long numChildren = dictionaryEntries.getChildrenCount();
        int index, count = 0;
        index = new Random().nextInt((int) numChildren);

        Iterator<DataSnapshot> dictionaryEntriesIterator = dictionaryEntries.getChildren().iterator();
        DataSnapshot currentEntryDS = dictionaryEntriesIterator.next();
        while(dictionaryEntriesIterator.hasNext() && count < index) {
            currentEntryDS = dictionaryEntriesIterator.next();
            count++;
        }
        mCurrentEntry = currentEntryDS.getValue(DictionaryEntry.class);

        TextView wordView = (TextView) findViewById(R.id.entry_word);
        wordView.setText(mCurrentEntry.mWord);
        definitionToggle.setText(getString(R.string.reveal_definition));

    }

    @OnClick(R.id.editEntry)
    public void editEntry(View view) {
        Intent intent = new Intent(this, NewEntryActivity.class);
        intent.putExtra("entry", mCurrentEntry);
        startActivity(intent);
    }



    public <T> T doBlub(Class<T> var1) {
        try {
            return var1.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setDictionaryData(DataSnapshot de) {
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
