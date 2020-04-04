package pt.ulisboa.tecnico.sise.insureapp.app;

import android.os.AsyncTask;
import android.util.Log;

public class WSLogout extends AsyncTask<Void, Void, Void> {
    public final static String TAG = "LogoutTask";
    private GlobalState _gs;

    public WSLogout(GlobalState globalState){
        _gs = globalState;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try{
            boolean result = WSHelper.logout(_gs.getSessionId());
            Log.d(TAG, "Logout result -> " + result);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        return null;
    }
}
