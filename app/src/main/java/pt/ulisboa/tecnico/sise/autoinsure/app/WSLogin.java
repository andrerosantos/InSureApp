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
    private Context _context;

    public WSLogin(GlobalState globalState, LoginActivity loginActivity, Context context){
        _gs = (GlobalState) context;
        _la = loginActivity;
        _context = context;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        int sessionID = -1;
        try {
            sessionID = WSHelper.login(strings[0], strings[1]);
            _gs.set_sessionID(sessionID);
            _gs.setUsername(strings[0]);

            _gs.checkFilesToSubmit(_la, _context);

        } catch (Exception e){
            Log.d(TAG, e.toString());
        }

        Log.d(TAG, "Session id => " + sessionID);
        return sessionID;
    }

    @Override
    protected void onPostExecute(Integer result){
        if (result > 0 ) {
            _la.nextActivity();
        } else if (result == 0){
            Toast.makeText(_la, "Wrong username or password!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(_la, "Oops, could not login!", Toast.LENGTH_SHORT).show();
        }
    }
}
