package pt.ulisboa.tecnico.sise.autoinsure.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import pt.ulisboa.tecnico.sise.autoinsure.datamodel.ClaimRecord;

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
            ClaimRecord claim = WSHelper.getClaimInfo(integers[0], integers[1]);
            Log.d(TAG, "Got record of claim => " + integers[1]);

            // write claims locally to use in case of absent connection
            String encodedClaim = JsonCodec.encodeClaimRecord(claim);
            JsonFileManager.jsonWriteToFile(this.globalState, InternalProtocol.KEY_CLAIM_RECORD_FILE + this.globalState.getSessionId(), encodedClaim);

            return claim;
        } catch (Exception e) {
            Log.d(TAG, "Could not get claim record. (session, claim) => " + integers[0] + ", " + integers[1]);
            Log.d(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(ClaimRecord claim){
        if (claim == null){
            String encodedClaim = JsonFileManager.jsonReadFromFile(this.globalState, InternalProtocol.KEY_CLAIM_RECORD_FILE + this.globalState.getSessionId());

            if (encodedClaim.equals("")){
                Toast.makeText(this.activity, "Could not get claim information", Toast.LENGTH_SHORT).show();
                return;
            } else {
                claim = JsonCodec.decodeClaimRecord(encodedClaim);
            }
        }

        this.textViewTitle.setText(claim.getTitle());
        this.textViewOccurrenceDate.setText(claim.getOccurrenceDate());
        this.textViewSubmissionDate.setText(claim.getSubmissionDate());
        this.textViewCarPlate.setText(claim.getPlate());
        this.textViewStatus.setText(claim.getStatus());
        this.textViewDescription.setText(claim.getDescription());
    }
}
