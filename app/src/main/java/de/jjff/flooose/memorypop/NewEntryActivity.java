package de.jjff.flooose.memorypop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;

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

    public boolean editing;
    public DataSnapshot dictionaryEntries;

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

        firebaseDataService.migrate();
    }

    @Override
    protected void onResume() {
        super.onStart();
        Intent intent = getIntent();

        if(intent.getParcelableExtra("entry") != null) {
            DictionaryEntry dictionaryEntry = intent.getParcelableExtra("entry");
            newWord.setText(dictionaryEntry.mWord);
            newWordDefinition.setText(dictionaryEntry.mDefinition);
        }

    }


    // TODO Legacy, This needs to be removed from the interface
    //@OnClick(R.id.ok)
    public void displayRandomWord() {
    }

    @OnClick(R.id.submitNewWord)
    public void submitNewWord(View view) {
        String word = newWord.getText().toString();
        String definition = newWordDefinition.getText().toString();

        DictionaryEntry dictionaryEntry = new DictionaryEntry(word, definition);

        if (!word.isEmpty()) {
            if(getIntent().getParcelableExtra("entry") != null) {
                firebaseDataService.updateValue(dictionaryEntry);
                finish();
            } else {
                firebaseDataService.addValue(dictionaryEntry);
            }

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

    @Override
    public boolean isEditing() {
        return editing;
    }

    @Override
    public void setEditing(boolean b) {
        this.editing = b;
    }

    @Override
    public void setDictionaryData(DataSnapshot de) {
        this.dictionaryEntries = de;
    }

    public void signInOrUp() {
        Intent intent = new Intent(this, SigninOrUpActivity.class);
        startActivity(intent);
    }
}
