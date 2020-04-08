package pt.ulisboa.tecnico.sise.autoinsure.app;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import pt.ulisboa.tecnico.sise.autoinsure.app.activities.LoginActivity;
import pt.ulisboa.tecnico.sise.autoinsure.app.activities.MenuActivity;
import pt.ulisboa.tecnico.sise.autoinsure.datamodel.Customer;

public class WSNewClaim extends AsyncTask<String, Void, Boolean> {
    public static final String TAG = "WSNewClaim";

    private GlobalState globalState;
    private Activity activity;
    private boolean wrongSessionId = false;

    public WSNewClaim(GlobalState globalState, Activity activity) {
        this.globalState = globalState;
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        //test connection and session ID
        try{
            Customer customer = WSHelper.getCustomerInfo(this.globalState.getSessionId());
            if(!customer.getAddress().equals(this.globalState.getUsername())){
                this.wrongSessionId = true;
                return false;
            }
        } catch (Exception e){
            //Not connected
            Log.d(TAG, e.getMessage());
            return false;
        }

        //submit claim
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
            if (this.wrongSessionId){
                Toast.makeText(this.activity, "Invalid session id. Please login!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this.activity, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.activity.startActivity(intent);
                this.activity.finish();
            }
            Toast.makeText(this.activity, "No connection to server. Please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

}
