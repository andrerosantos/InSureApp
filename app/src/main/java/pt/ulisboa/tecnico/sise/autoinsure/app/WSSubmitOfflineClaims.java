package pt.ulisboa.tecnico.sise.autoinsure.app;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.sise.autoinsure.datamodel.ClaimRecord;

public class WSSubmitOfflineClaims extends AsyncTask<Void, Void, Void> {
    public static final String TAG = "WS_SubmitOffline";

    private GlobalState _gs;
    private Activity _activity;
    private Context _context;
    private boolean error = false;
    private boolean nothingToSubmit = false;

    public WSSubmitOfflineClaims(GlobalState gs, Activity activity, Context context){
        this._gs = gs;
        this._activity = activity;
        this._context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        List<ClaimRecord> claimList = new ArrayList<>();
        String filename = InternalProtocol.KEY_CLAIM_FOR_FUTURE_SUBMISSION_FILE + _gs.getUsername();
        try {
            String encodedClaimRecordList = JsonFileManager.jsonReadFromFile(_gs, filename);
            claimList = JsonCodec.decodeClaimRecordList(encodedClaimRecordList);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            this.nothingToSubmit = true;
            return null;
        }

        for (int i = 0; i < claimList.size(); i++){
            ClaimRecord newClaim = claimList.get(i);
            Log.d(TAG, "claim found. title => " + newClaim.getTitle());
            try {
                boolean success = WSHelper.submitNewClaim(_gs.getSessionId(), newClaim.getTitle(), newClaim.getOccurrenceDate(), newClaim.getPlate(), newClaim.getDescription());
                Log.d(TAG, "Trying to submit claim. Success? => " + success);
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
        }

        File dir = _context.getFilesDir();
        File fileToDelete = new File(dir, filename);
        fileToDelete.delete();

        return null;
    }

    @Override
    protected void onPostExecute(Void voids){
        if (this.nothingToSubmit){
            return;
        } else if(this.error){
            Toast.makeText(this._activity, "There was an error submitting previous claims. Please check submitted claims!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this._activity, "Offline claims successfully submitted!", Toast.LENGTH_SHORT).show();
        }
    }
}
