package fr.lenours.dropboxapi;

import android.app.Activity;
import android.content.SharedPreferences;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;
import com.dropbox.client2.session.TokenPair;

/**
 * Created by Clemsbrowning on 10/06/2016.
 */
public class DropboxAuthActivity extends Activity{

    private final static String DROPBOX_FILE_DIR = "/DropboxDemo/";
    private final static String DROPBOX_NAME = "dropbox_prefs";
    private final static String ACCESS_KEY = "zuj8q0szm7xdq6p";
    private final static String ACCESS_SECRET = "fajd2zi0azmjf1w";
    private final static Session.AccessType ACCESS_TYPE = Session.AccessType.DROPBOX;
    private DropboxAPI dropboxApi;
    private boolean isUserLoggedIn;

    public DropboxAuthActivity(){}

    public DropboxAuthActivity(Activity activity) {

        AppKeyPair appKeyPair = new AppKeyPair(ACCESS_KEY, ACCESS_SECRET);
        AndroidAuthSession session;

        SharedPreferences prefs = activity.getSharedPreferences(DROPBOX_NAME, 0);
        String key = prefs.getString(ACCESS_KEY, null);
        String secret = prefs.getString(ACCESS_SECRET, null);

        if (key != null && secret != null) {
            AccessTokenPair token = new AccessTokenPair(key, secret);
            session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE, token);
        } else {
            session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE);
        }

        dropboxApi = new DropboxAPI(session);

    }

    public void onResume(Activity activity) {
        AndroidAuthSession session = (AndroidAuthSession) dropboxApi.getSession();
        if (session.authenticationSuccessful()) {
            try {
                session.finishAuthentication();

                TokenPair tokens = session.getAccessTokenPair();
                SharedPreferences prefs = activity.getSharedPreferences(DROPBOX_NAME, 0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(ACCESS_KEY, tokens.key);
                editor.putString(ACCESS_SECRET, tokens.secret);
                editor.commit();

                loggedIn(true);
            } catch (IllegalStateException e) {

            }
        }
    }

    public void connect(Activity activity) {

    }

    public void loggedIn(boolean userLoggedIn) {
        isUserLoggedIn = userLoggedIn;
    }
}
