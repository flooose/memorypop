package de.jjff.flooose.memorypop;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import de.jjff.flooose.memorypop.daggerstuff.ActivityComponent;
import de.jjff.flooose.memorypop.daggerstuff.ActivityModule;
import de.jjff.flooose.memorypop.daggerstuff.DaggerActivityComponent;
import de.jjff.flooose.memorypop.services.DataService;
import de.jjff.flooose.memorypop.services.FirebaseDataService;

import static de.jjff.flooose.memorypop.MemoryPopApplication.LOG_TAG;


public class NewEntryActivity extends AppCompatActivity {

    private DictionaryEntry currentEntry;
    private StaticDictionaryService.VocabDictionary currentDictionary;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mCurrentUser;
    private EditText newWord;
    private EditText newWordDefinition;
    private Button submitNewWord;
    private DatabaseReference dictionaryRef;
    private View playLayout;
    private View newWordLayout;
    private View startLayout;
    private View playButton;
    private View newWordButton;
    private Button definitionToggle;
    private boolean editing;

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

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mCurrentUser = firebaseAuth.getCurrentUser();
                if (mCurrentUser != null) {
                    // User is signed in
                    populateDatabase();
                    Log.d(LOG_TAG, "onAuthStateChanged:signed_in:" + mCurrentUser.getUid());
                } else {
                    // User is signed out
                    Log.d(LOG_TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        currentDictionary = StaticDictionaryService.getDefaultDictionary();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        mAuth.signInWithEmailAndPassword("skeptikos@gmail.com", "snafu1234")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(LOG_TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(LOG_TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(NewEntryActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(NewEntryActivity.this, R.string.auth_succeeded,
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void populateDatabase() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference(mCurrentUser.getUid());
        dictionaryRef = userRef.child("dictionary");

        dictionaryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                clearFields();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                clearFields();
                cancelNewWord(null);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                clearFields();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                clearFields();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                clearFields();
            }

            private void clearFields() {
                newWordDefinition.setText("");
                newWord.setText("");
            }
        });

        dictionaryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                };

                GenericTypeIndicator<List<DictionaryEntry>> dictinaryEntries =
                        new GenericTypeIndicator<List<DictionaryEntry>>() {
                        };

                List<DictionaryEntry> de = dataSnapshot.getValue(dictinaryEntries);
                if (de != null) {
                    NewEntryActivity.this.currentDictionary.dictionaryEntries = de;
                    if(editing) {
                        editing = false;
                    } else {
                        NewEntryActivity.this.displayRandomWord();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                int blub = 4;
            }
        });
    }

    public void toggleDefinition(View view) { toggleDefinition();}
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
        int currentEntryIndex = new Random().nextInt(currentDictionary.dictionaryEntries.size());
        currentEntry = currentDictionary.dictionaryEntries.get(currentEntryIndex);

        TextView wordView = (TextView) findViewById(R.id.entry_word);
        wordView.setText(currentEntry.mWord);
        definitionToggle.setText(getString(R.string.reveal_definition));

    }

    public void submitNewWord(View view) {
        String word = newWord.getText().toString();
        String definition = newWordDefinition.getText().toString();

        DictionaryEntry dictionaryEntry = new DictionaryEntry(word, definition);
        int dictionaryIndex = currentDictionary.dictionaryEntries.indexOf(dictionaryEntry);

        if (!word.isEmpty()) {
            if (dictionaryIndex >= 0) {
                currentDictionary.dictionaryEntries.set(dictionaryIndex, dictionaryEntry);
            } else {
                currentDictionary.dictionaryEntries.add(dictionaryEntry);
            }

            dictionaryRef.setValue(currentDictionary.dictionaryEntries);
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
