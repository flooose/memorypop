package de.jjff.flooose.memorypop.daggerstuff;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;
import de.jjff.flooose.memorypop.NewEntryActivity;
import de.jjff.flooose.memorypop.services.DataService;
import de.jjff.flooose.memorypop.services.FirebaseDataService;

@Module
public class ActivityModule {
    Blub mActivity;

    // this should probably be an interface instead of an entire activity
    public ActivityModule(Blub activity) {
        mActivity = activity;
    }

    @Provides
    DataService dataService() {
        return new FirebaseDataService(mActivity);
    }
}