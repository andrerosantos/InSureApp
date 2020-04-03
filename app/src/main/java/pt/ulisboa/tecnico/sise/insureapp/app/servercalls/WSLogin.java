package pt.ulisboa.tecnico.sise.insureapp.app.servercalls;

import android.os.AsyncTask;
import android.util.Log;

public class WSLogin extends AsyncTask<String, Void, Integer> {
    public final static String TAG = "BackgroundLogin";

    @Override
    protected Integer doInBackground(String... strings) {
        int sessionID = 0;
        try {
            sessionID = WSHelper.login(strings[0], strings[1]);
        } catch (Exception e){
            Log.d(TAG, e.toString());
        }
        return sessionID;
    }

    @Override
    protected void onPostExecute(Integer result){
        super.onPostExecute(result);
    }
}
