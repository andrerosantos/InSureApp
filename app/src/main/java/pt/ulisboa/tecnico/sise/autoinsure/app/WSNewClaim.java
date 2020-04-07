package pt.ulisboa.tecnico.sise.autoinsure.app;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import pt.ulisboa.tecnico.sise.autoinsure.app.activities.MenuActivity;

public class WSNewClaim extends AsyncTask<String, Void, Boolean> {
    public static final String TAG = "WSNewClaim";

    private GlobalState globalState;
    private Activity activity;

    public WSNewClaim(GlobalState globalState, Activity activity) {
        this.globalState = globalState;
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        boolean result = false;
        try{
            result = WSHelper.submitNewClaim(this.globalState.getSessionId(), strings[0], strings[1], strings[2], strings[3]);
            Log.d(TAG, "submission result => " + result);
        } catch (Exception e){
            Log.d(TAG, e.getMessage());
        }
        return result;
    }

    @Override
    protected void onPostExecute(Boolean result){
        if (result){
            Toast.makeText(this.activity, "Claim successfully submitted", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this.activity, MenuActivity.class);
            this.activity.startActivity(intent);
            this.activity.finish();
        } else {
            Toast.makeText(this.activity, "Claim not submitted. Please try later.", Toast.LENGTH_SHORT).show();
        }
    }

}
