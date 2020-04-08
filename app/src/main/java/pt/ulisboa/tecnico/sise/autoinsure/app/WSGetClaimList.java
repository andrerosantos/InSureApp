package pt.ulisboa.tecnico.sise.autoinsure.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import pt.ulisboa.tecnico.sise.autoinsure.app.activities.LoginActivity;
import pt.ulisboa.tecnico.sise.autoinsure.datamodel.ClaimItem;
import pt.ulisboa.tecnico.sise.autoinsure.app.activities.ListClaimActivity;
import pt.ulisboa.tecnico.sise.autoinsure.datamodel.Customer;

public class WSGetClaimList extends AsyncTask<Integer, Void, List<ClaimItem>> {
    public final static String TAG = "CallListClaims";

    private GlobalState _gs;
    private ListClaimActivity _activity;
    private ListView _listView;
    private boolean wrongSessionId = false;

    public WSGetClaimList(GlobalState globalState, ListClaimActivity activity, ListView listView) {
        this._gs = globalState;
        this._activity = activity;
        this._listView = listView;
    }

    @Override
    protected List<ClaimItem> doInBackground(Integer... sessionID) {
        try {
            // get information from server
            Customer customer = WSHelper.getCustomerInfo(this._gs.getSessionId());

            // if no customer or wrong username associated with current session ID
            if(customer == null || (!customer.getUsername().equals(this._gs.getUsername()))){
                this.wrongSessionId = true;
                Log.d(TAG, "Wrong session id");
                return null;
            }

            try {
                // get information from server
                List<ClaimItem> claims = WSHelper.listClaims(sessionID[0]);

                // if server went down since previous call - not likely but keeps app working
                if(claims == null){
                    this.wrongSessionId = true;
                    Log.d(TAG, "Wrong session id");
                    return null;
                }

                // write claims locally to use in case of absent connection
                String encodedClaims = JsonCodec.encodeClaimList(claims);
                JsonFileManager.jsonWriteToFile(_gs, InternalProtocol.KEY_CLAIM_LIST_FILE + _gs.getUsername(), encodedClaims);

                return claims;
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
            String encodedClaims = null;

            try {
                //try to get information locally
                encodedClaims = JsonFileManager.jsonReadFromFile(_gs, InternalProtocol.KEY_CLAIM_LIST_FILE + _gs.getUsername());
                List<ClaimItem> list = JsonCodec.decodeClaimList(encodedClaims);

                Log.d(TAG, "Showing customer information saved locally.");
                return list;

            } catch (Exception ex){
                // there is no local information
                Log.d(TAG, "No customer information saved locally.");
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<ClaimItem> list){
        if (list == null){
            if (this.wrongSessionId){
                Toast.makeText(_activity, "Invalid session id. Please login!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(_activity, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                _activity.startActivity(intent);
                _activity.finish();

            } else {
                Toast.makeText(_activity, "Oops, we could not get your information!", Toast.LENGTH_SHORT).show();
            }
        } else {

            ArrayAdapter<ClaimItem> adapter = new ArrayAdapter<ClaimItem>(_gs, android.R.layout.simple_list_item_1, android.R.id.text1, list);
            _listView.setAdapter(adapter);
        }
    }
}
