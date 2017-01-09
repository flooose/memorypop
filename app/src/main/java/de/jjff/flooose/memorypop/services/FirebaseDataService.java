package de.jjff.flooose.memorypop.services;

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

import static de.jjff.flooose.memorypop.MemoryPopApplication.LOG_TAG;

/**
 * Created by chris on 06.01.17.
 */

public class FirebaseDataService implements DataService {
    private final NewEntryActivity mActivity;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mCurrentUser;
    private DatabaseReference dictionaryRef;

    public FirebaseDataService(NewEntryActivity activity) {
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
                    // User is signed out
                    Log.d(LOG_TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        });

        mAuth.signInWithEmailAndPassword("skeptikos@gmail.com", "snafu1234")
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(LOG_TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(LOG_TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(mActivity, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mActivity, R.string.auth_succeeded,
                                    Toast.LENGTH_SHORT).show();
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
                clearFields();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                clearFields();
                mActivity.cancelNewWord(null);
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
                mActivity.newWordDefinition.setText("");
                mActivity.newWord.setText("");
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
                    mActivity.dictionaryEntries = de;
                    if(mActivity.editing) {
                        mActivity.editing = false;
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
    }
}
