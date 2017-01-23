package de.jjff.flooose.memorypop.daggerstuff;

import dagger.Module;
import dagger.Provides;
import de.jjff.flooose.memorypop.SigninOrUpActivity;
import de.jjff.flooose.memorypop.services.LoginService;

@Module
public class LoginModule {

    // This actually isn't being used
    public LoginModule(SigninOrUpActivity activity) {

    }

//    @Provides
//    public LoginService loginService() {
//        return new LoginService();
//    }
}
