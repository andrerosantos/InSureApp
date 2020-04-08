package pt.ulisboa.tecnico.sise.autoinsure.app;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import pt.ulisboa.tecnico.sise.autoinsure.app.activities.LoginActivity;
import pt.ulisboa.tecnico.sise.autoinsure.datamodel.Customer;

public class WSLogout extends AsyncTask<Void, Void, Boolean> {
    public final static String TAG = "LogoutTask";
    private GlobalState _gs;
    private Activity _activity;


    public WSLogout(GlobalState globalState, Activity activity){
        _gs = globalState;
        _activity = activity;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        //test connection and session ID
        try{
            Customer customer = WSHelper.getCustomerInfo(this._gs.getSessionId());
            if(customer == null || !customer.getAddress().equals(this._gs.getUsername())){
                // server is running but session id is invalid - no login
                // we will say to user that he has been logged out
                return true;
            }
        } catch (Exception e){
            //Not connected
            Log.d(TAG, e.getMessage());
            return false;
        }

        boolean result = false;
        try{
            result = WSHelper.logout(_gs.getSessionId());
            _gs.set_sessionID(-1);
            Log.d(TAG, "Logout result -> " + result);

        } catch (Exception e) {
            // this exception is not likely to happen since we already tested the connection
            Log.d(TAG, e.toString());
        }
        return result;
    }

    @Override
    protected void onPostExecute(Boolean result){
        if (result) {
            Toast.makeText(_activity, "Logged out", Toast.LENGTH_SHORT).show();
            _activity.onBackPressed();
            _activity.finish();

        } else {
            Toast.makeText(_activity, "Logout unsuccessful. Please try later!", Toast.LENGTH_SHORT).show();
        }
    }
}
