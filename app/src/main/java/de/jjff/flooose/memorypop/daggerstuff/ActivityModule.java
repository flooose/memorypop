package de.jjff.flooose.memorypop.daggerstuff;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;
import de.jjff.flooose.memorypop.services.DataService;
import de.jjff.flooose.memorypop.services.FirebaseDataService;

@Module
public class ActivityModule {
    Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    DataService dataService() {
        return new FirebaseDataService();
    }
}