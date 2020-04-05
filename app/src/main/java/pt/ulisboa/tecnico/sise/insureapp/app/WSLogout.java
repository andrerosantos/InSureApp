package pt.ulisboa.tecnico.sise.insureapp.app;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MenuInflater;
import android.widget.Toast;

import pt.ulisboa.tecnico.sise.insureapp.app.activities.LoginActivity;
import pt.ulisboa.tecnico.sise.insureapp.app.activities.MenuActivity;

public class WSLogout extends AsyncTask<Void, Void, Boolean> {
    public final static String TAG = "LogoutTask";
    private GlobalState _gs;
    private Activity _activity;


    public WSLogout(GlobalState globalState, Activity activity){
        _gs = globalState;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        boolean result = false;
        try{
            result = WSHelper.logout(_gs.getSessionId());
            _gs.set_sessionID(-1);
            Log.d(TAG, "Logout result -> " + result);

        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        return result;
    }

    public void onPostExecute(boolean result){
        if (result) {
            Toast.makeText(_activity, "Logged out", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(_activity, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            _activity.startActivity(i);
            _activity.finish();

        } else {
            Toast.makeText(_activity, "Logout unsuccessful. Please try later!", Toast.LENGTH_SHORT).show();
        }
    }
}
