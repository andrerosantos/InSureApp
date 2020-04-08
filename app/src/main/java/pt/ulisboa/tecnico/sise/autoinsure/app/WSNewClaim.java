package pt.ulisboa.tecnico.sise.autoinsure.app;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import pt.ulisboa.tecnico.sise.autoinsure.app.activities.LoginActivity;
import pt.ulisboa.tecnico.sise.autoinsure.app.activities.MenuActivity;
import pt.ulisboa.tecnico.sise.autoinsure.datamodel.ClaimRecord;
import pt.ulisboa.tecnico.sise.autoinsure.datamodel.Customer;

public class WSNewClaim extends AsyncTask<String, Void, Boolean> {
    public static final String TAG = "WSNewClaim";

    private GlobalState globalState;
    private Activity activity;
    private boolean wrongSessionId = false;
    private boolean futureSubmission= false;

    public WSNewClaim(GlobalState globalState, Activity activity) {
        this.globalState = globalState;
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        //test connection and session ID
        try{
            Customer customer = WSHelper.getCustomerInfo(this.globalState.getSessionId());
            if(!customer.getUsername().equals(this.globalState.getUsername())){
                Log.d(TAG, "Invalid session ID");
                this.wrongSessionId = true;
                return false;
            }
        } catch (Exception e){
            //Not connected
            Log.d(TAG, e.getMessage());

            // save claim to submit later
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            ClaimRecord claimToSave = new ClaimRecord(-1, strings[0], df.format(new Date()), strings[1], strings[2], strings[3], "", null );
            try {
                String encodedClaim = JsonCodec.encodeClaimToSave(claimToSave, this.globalState, InternalProtocol.KEY_CLAIM_FOR_FUTURE_SUBMISSION_FILE + this.globalState.getUsername());
                JsonFileManager.jsonWriteToFile(this.globalState, encodedClaim, InternalProtocol.KEY_CLAIM_FOR_FUTURE_SUBMISSION_FILE + this.globalState.getUsername());
                this.futureSubmission = true;

            } catch (Exception ex) {
                Log.d(TAG, e.getMessage());
            }

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
            } else{
                if (this.futureSubmission){
                    Toast.makeText(this.activity, "Your claim will be submitted when you have a connection.", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this.activity, "No connection to server. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
