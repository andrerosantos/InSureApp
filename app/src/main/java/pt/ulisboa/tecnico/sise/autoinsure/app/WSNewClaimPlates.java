package pt.ulisboa.tecnico.sise.autoinsure.app;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import pt.ulisboa.tecnico.sise.autoinsure.app.activities.LoginActivity;
import pt.ulisboa.tecnico.sise.autoinsure.datamodel.Customer;

public class WSNewClaimPlates extends AsyncTask<Integer, Void, List<String>> {
    public static final String TAG = "WSNewClaimPlate";

    private GlobalState _globalState;
    private Activity _activity;
    private Spinner _spinner;
    private boolean wrongSessionId = false;


    public WSNewClaimPlates(GlobalState globalState, Activity activity, Spinner spinner){
        this._globalState = globalState;
        this._activity = activity;
        this._spinner = spinner;
    }

    @Override
    protected List<String> doInBackground(Integer... integers) {
        try {
            // get information from server
            Customer customer = WSHelper.getCustomerInfo(this._globalState.getSessionId());

            // if no customer or wrong username associated with current session ID
            if(customer == null || (!customer.getUsername().equals(this._globalState.getUsername()))){
                this.wrongSessionId = true;
                Log.d(TAG, "Wrong session id");
                return null;
            }

            try {
                // get information from server
                List<String> plates = WSHelper.listPlates(integers[0]);

                // if server went down since previous call - not likely but keeps app working
                if(plates == null){
                    this.wrongSessionId = true;
                    Log.d(TAG, "Wrong session id");
                    return null;
                }

                // save plates
                String encodedPlates = JsonCodec.encodePlateList(plates);
                JsonFileManager.jsonWriteToFile(_globalState, InternalProtocol.KEY_USER_PLATES_FILE + _globalState.getUsername(), encodedPlates);

                return plates;
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
            String encodedPlates = null;

            try {
                //try to get information locally
                encodedPlates = JsonFileManager.jsonReadFromFile(_globalState, InternalProtocol.KEY_USER_PLATES_FILE + _globalState.getSessionId());
                List<String> plates = JsonCodec.decodePlateList(encodedPlates);

                Log.d(TAG, "Showing customer information saved locally.");
                return plates;

            } catch (Exception ex){
                // there is no local information
                Log.d(TAG, "No customer information saved locally.");
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<String> plates){
        if (plates == null){
            if (this.wrongSessionId){
                Toast.makeText(_activity, "Invalid session id. Please login!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(_activity, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                _activity.startActivity(intent);
                _activity.finish();

            } else {
                Toast.makeText(_activity, "Oops, we could not get your vehicle plates!", Toast.LENGTH_SHORT).show();
            }
        } else {

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(_globalState, android.R.layout.simple_list_item_1, android.R.id.text1, plates);
            this._spinner.setAdapter(adapter);
        }
    }
}
