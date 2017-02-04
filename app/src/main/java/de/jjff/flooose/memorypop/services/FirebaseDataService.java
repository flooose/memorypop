package de.jjff.flooose.memorypop.services;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

import de.jjff.flooose.memorypop.DictionaryEntry;
import de.jjff.flooose.memorypop.NewEntryActivity;
import de.jjff.flooose.memorypop.R;
import de.jjff.flooose.memorypop.SigninOrUpActivity;
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
    private DatabaseReference dictionaryRef;

    public FirebaseDataService(Blub activity) {
        // this should probably be an interface, not a whole activity
        mActivity = activity;

        mAuth = FirebaseAuth.getInstance();

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
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference(mCurrentUser.getUid());
        dictionaryRef = userRef.child("dictionary");

        dictionaryRef.addChildEventListener(new ChildEventListener() {
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
                    mActivity.setDictionaryEntries(de);
                    if(mActivity.isEditing()) {
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
    }

    public ArrayList<DictionaryEntry> getDictionaryData() {
        return null;
    }

    @Override
    public void setValue(List<DictionaryEntry> dictionaryEntries) {
        dictionaryRef.setValue(dictionaryEntries);
    }

    @Override
    public void stop() {
        mAuth.removeAuthStateListener(mAuthListener);
        mAuth.signOut();
    }
}
