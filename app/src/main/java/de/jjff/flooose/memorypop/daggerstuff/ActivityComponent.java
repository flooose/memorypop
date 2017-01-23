package de.jjff.flooose.memorypop.daggerstuff;

import android.app.Activity;
import android.provider.ContactsContract;

import dagger.Component;
import de.jjff.flooose.memorypop.NewEntryActivity;
import de.jjff.flooose.memorypop.services.DataService;
import de.jjff.flooose.memorypop.services.FirebaseDataService;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(NewEntryActivity newEntryActivity);
}
