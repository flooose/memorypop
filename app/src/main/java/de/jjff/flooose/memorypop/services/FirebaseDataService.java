package de.jjff.flooose.memorypop.services;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import de.jjff.flooose.memorypop.DictionaryEntry;
import de.jjff.flooose.memorypop.NewEntryActivity;
import de.jjff.flooose.memorypop.R;

import static de.jjff.flooose.memorypop.MemoryPopApplication.LOG_TAG;

/**
 * Created by chris on 06.01.17.
 */

public class FirebaseDataService implements DataService {

    public ArrayList<DictionaryEntry> getDictionaryData() {
        return null;
    }
}
