package de.jjff.flooose.memorypop.services;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import de.jjff.flooose.memorypop.DictionaryEntry;
import de.jjff.flooose.memorypop.daggerstuff.Blub;
import de.jjff.flooose.memorypop.migrations.ConvertEntriesToKeyValuePairs;

import static de.jjff.flooose.memorypop.MemoryPopApplication.LOG_TAG;

/**
 * Created by chris on 06.01.17.
 */

public class FirebaseDataService implements DataService {
    public static final String CURRENT_DICTIONARY = "dictionary2";
    private final Blub mActivity;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDictionaryRef;
    private DataSnapshot mDataSnapshot;
    private boolean really;

    public FirebaseDataService(Blub activity) {
        // this should probably be an interface, not a whole activity
        mActivity = activity;

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mCurrentUser = firebaseAuth.getCurrentUser();

                if (mCurrentUser != null) {
                    // User is signed in
                    populateDatabase();
                    Log.d(LOG_TAG, "onAuthStateChanged:signed_in:" + mCurrentUser.getUid());
                } else {
                    mActivity.signInOrUp();
                    Log.d(LOG_TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        });

    }

    private void populateDatabase() {
        mDictionaryRef = getDictionary();

        if (mDictionaryRef != null) {

            mDictionaryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    mActivity.resetFrom();
                    mActivity.cancelNewWord(null);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    mActivity.resetFrom();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    mActivity.resetFrom();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    mActivity.resetFrom();
                }
            });

            mDictionaryRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    FirebaseDataService.this.mDataSnapshot = dataSnapshot;
                    GenericTypeIndicator<List<DictionaryEntry>> dictinaryEntries =
                            new GenericTypeIndicator<List<DictionaryEntry>>() {
                            };

                    //List<DictionaryEntry> de = dataSnapshot.getValue(dictinaryEntries);
                    List<DictionaryEntry> de = null;
                    mActivity.setDictionaryData(mDataSnapshot);

                    if (mActivity.isEditing()) {
                        mActivity.setEditing(false);
                    } else {
                        mActivity.displayRandomWord();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    int blub = 4;
                }
            });
        } else {
            mActivity.signInOrUp();
        }
    }

    public DatabaseReference getDictionary() {
        if (mCurrentUser != null) {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference userRef = database.getReference(mCurrentUser.getUid());
            DatabaseReference dictionary = userRef.child(CURRENT_DICTIONARY);
            return dictionary;
        } else {
            return null;
        }
    }

    @Override
    public void setValue(List<DictionaryEntry> dictionaryEntries) {
        // mDictionaryRef.setValue(dictionaryEntries);
    }

    @Override
    public void stop() {
        mAuth.removeAuthStateListener(mAuthListener);
        mAuth.signOut();
    }

    private void wtf(boolean really) {
        this.really = really;
    }

    @Override
    public void addValue(DictionaryEntry dictionaryEntry) {
        mDictionaryRef.push().setValue(dictionaryEntry);
        mActivity.resetFrom();
    }

    @Override
    public void updateValue(final DictionaryEntry dictionaryEntry) {
        mDictionaryRef.orderByChild("mWord")
                .equalTo(dictionaryEntry.mWord)
                .limitToFirst(1)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        wtf(true);
                        DictionaryEntry entry = dataSnapshot.getValue(DictionaryEntry.class);
                        entry.mDefinition = dictionaryEntry.mDefinition;
                        mDictionaryRef.child(dataSnapshot.getKey()).setValue(entry);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        dataSnapshot.getValue();

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        dataSnapshot.getValue();

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        dataSnapshot.getValue();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public DictionaryEntry getRandomWord() {
        Math.random();
        return null;
    }

    @Override
    public void migrate() {
        if (mCurrentUser != null) {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference userRef = database.getReference(mCurrentUser.getUid());
            new ConvertEntriesToKeyValuePairs(userRef, getDictionary()).migrate();
        }
    }
}

