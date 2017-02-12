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

import java.util.ArrayList;
import java.util.List;

import de.jjff.flooose.memorypop.DictionaryEntry;
import de.jjff.flooose.memorypop.daggerstuff.Blub;

import static de.jjff.flooose.memorypop.MemoryPopApplication.LOG_TAG;

/**
 * Created by chris on 06.01.17.
 */

public class FirebaseDataService implements DataService {
    private final Blub mActivity;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDictionaryRef;

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

        if(mDictionaryRef != null) {

            mDictionaryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    mActivity.resetFrom();
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
                    GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                    };

                    GenericTypeIndicator<List<DictionaryEntry>> dictinaryEntries =
                            new GenericTypeIndicator<List<DictionaryEntry>>() {
                            };

                    List<DictionaryEntry> de = dataSnapshot.getValue(dictinaryEntries);
                    if (de != null) {
                        mActivity.setDictionaryEntries(de);
                        if (mActivity.isEditing()) {
                            mActivity.setEditing(false);
                        } else {
                            mActivity.displayRandomWord();
                        }
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
        if(mCurrentUser != null) {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference userRef = database.getReference(mCurrentUser.getUid());
            DatabaseReference dictionary = userRef.child("dictionary");
            return dictionary;
        } else {
            return null;
        }
    }

    public ArrayList<DictionaryEntry> getDictionaryData() {
        return null;
    }

    @Override
    public void setValue(List<DictionaryEntry> dictionaryEntries) {
        mDictionaryRef.setValue(dictionaryEntries);
    }

    @Override
    public void stop() {
        mAuth.removeAuthStateListener(mAuthListener);
        mAuth.signOut();
    }
}
