package pt.ulisboa.tecnico.sise.autoinsure.app;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import pt.ulisboa.tecnico.sise.autoinsure.app.activities.LoginActivity;

public class WSLogin extends AsyncTask<String, Void, Integer> {
    private final static String TAG = "BackgroundLogin";
    private GlobalState _gs;
    private LoginActivity _la;

    public WSLogin(Context context, LoginActivity loginActivity){
        _gs = (GlobalState) context;
        _la = loginActivity;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        int sessionID = -1;
        try {
            sessionID = WSHelper.login(strings[0], strings[1]);
            _gs.set_sessionID(sessionID);
            _gs.setUsername(strings[0]);

            Log.d(TAG, "sessionID: " + sessionID);

        } catch (Exception e){
            Log.d(TAG, e.toString());
        }
        return sessionID;
    }

    @Override
    protected void onPostExecute(Integer result){
        if (_gs.getSessionId() > 0 ) {
            _la.nextActivity();
        } else {
            Toast.makeText(_la, "Oops, could not login!", Toast.LENGTH_SHORT).show();
        }
    }
}
