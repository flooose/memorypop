package de.jjff.flooose.memorypop.daggerstuff;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;
import de.jjff.flooose.memorypop.MemoryPopGame;
import de.jjff.flooose.memorypop.NewEntryActivity;
import de.jjff.flooose.memorypop.services.DataService;
import de.jjff.flooose.memorypop.services.FirebaseDataService;

@Module
public class ActivityModule {
    private MemoryPopGame mGame;
    Blub mActivity;

    public ActivityModule(Blub activity) {
        mActivity = activity;
    }
    public ActivityModule(MemoryPopGame game) {
        mGame = game;
    }

    @Provides
    DataService dataService() {
        return new FirebaseDataService(mActivity);
    }
}