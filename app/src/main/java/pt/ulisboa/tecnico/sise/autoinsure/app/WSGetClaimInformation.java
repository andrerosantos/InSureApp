package pt.ulisboa.tecnico.sise.autoinsure.app;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;

import pt.ulisboa.tecnico.sise.autoinsure.app.activities.LoginActivity;
import pt.ulisboa.tecnico.sise.autoinsure.datamodel.ClaimRecord;
import pt.ulisboa.tecnico.sise.autoinsure.datamodel.Customer;

public class WSGetClaimInformation extends AsyncTask<Integer, Void, ClaimRecord> {
    public final static String TAG = "WSGetClaimInfo";
    private GlobalState globalState;
    private Activity activity;

    private TextView textViewTitle;
    private TextView textViewOccurrenceDate;
    private TextView textViewSubmissionDate;
    private TextView textViewCarPlate;
    private TextView textViewStatus;
    private TextView textViewDescription;
    private boolean wrongSessionId = false;

    public WSGetClaimInformation(GlobalState globalState, Activity activity, TextView title, TextView occurrenceDate, TextView submissionDate, TextView carPlate, TextView status, TextView description){
        this.globalState = globalState;
        this.activity = activity;
        this.textViewTitle = title;
        this.textViewOccurrenceDate = occurrenceDate;
        this.textViewSubmissionDate = submissionDate;
        this.textViewCarPlate = carPlate;
        this.textViewStatus = status;
        this.textViewDescription = description;
    }

    @Override
    protected ClaimRecord doInBackground(Integer... integers) {
        try {
            // get information from server
            Customer customer = WSHelper.getCustomerInfo(this.globalState.getSessionId());

            // if no customer or wrong username associated with current session ID
            if(customer == null || (!customer.getUsername().equals(this.globalState.getUsername()))){
                this.wrongSessionId = true;
                Log.d(TAG, "Wrong session id");
                return null;
            }

            try {
                // get information from server
                ClaimRecord claim = WSHelper.getClaimInfo(integers[0], integers[1]);

                // if server went down since previous call - not likely but keeps app working
                if(claim == null){
                    this.wrongSessionId = true;
                    Log.d(TAG, "Wrong session id");
                    return null;
                }

                Log.d(TAG, "Got record of claim => " + integers[1]);
                // write claims locally to use in case of absent connection
                String encodedClaim = JsonCodec.encodeClaimRecord(claim);
                JsonFileManager.jsonWriteToFile(this.globalState, InternalProtocol.KEY_CLAIM_RECORD_FILE + this.globalState.getUsername() + "_" + integers[1], encodedClaim);

                return claim;
            } catch (Exception e) {
                //wrong session ID
                this.wrongSessionId = true;
                Log.d(TAG, e.getMessage());
                Log.d(TAG, "Wrong session id");
                return null;
            }

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());

            // no server connection
            //try to get information locally
            try {
                String encodedClaim = JsonFileManager.jsonReadFromFile(this.globalState, InternalProtocol.KEY_CLAIM_RECORD_FILE + this.globalState.getUsername() + "_" + integers[1]);
                ClaimRecord claim = JsonCodec.decodeClaimRecord(encodedClaim);

                Log.d(TAG, "Showing customer information saved locally.");
                return claim;

            } catch (Exception ex){
                // there is no local information
                Log.d(TAG, e.getMessage());
                Log.d(TAG, "No customer information saved locally.");
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(ClaimRecord claim){
        if (claim == null){
            if (this.wrongSessionId){
                Toast.makeText(activity, "Invalid session id. Please login!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
                activity.finish();

            } else {
                Toast.makeText(activity, "Oops, we could not get your information!", Toast.LENGTH_SHORT).show();
            }
        } else {
            this.textViewTitle.setText(claim.getTitle());
            this.textViewOccurrenceDate.setText(claim.getOccurrenceDate());
            this.textViewSubmissionDate.setText(claim.getSubmissionDate());
            this.textViewCarPlate.setText(claim.getPlate());
            this.textViewStatus.setText(claim.getStatus());
            this.textViewDescription.setText(claim.getDescription());
        }
    }
}
