package de.jjff.flooose.memorypop.daggerstuff;

import dagger.Component;
import de.jjff.flooose.memorypop.SigninOrUpActivity;
import de.jjff.flooose.memorypop.services.LoginService;


@PerActivity
@Component(modules = {})
public interface LoginComponent {
//    void inject(SigninOrUpActivity activity);
    LoginService loginService();
}
