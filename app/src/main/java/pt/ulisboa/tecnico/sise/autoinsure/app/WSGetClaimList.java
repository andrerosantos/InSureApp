package pt.ulisboa.tecnico.sise.autoinsure.app;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import pt.ulisboa.tecnico.sise.autoinsure.datamodel.ClaimItem;
import pt.ulisboa.tecnico.sise.autoinsure.app.activities.ListClaimActivity;

public class WSGetClaimList extends AsyncTask<Integer, Void, List<ClaimItem>> {
    public final static String TAG = "CallListClaims";

    private GlobalState _gs;
    private ListClaimActivity _activity;
    private ListView _listView;

    public WSGetClaimList(GlobalState globalState, ListClaimActivity activity, ListView listView) {
        this._gs = globalState;
        this._activity = activity;
        this._listView = listView;
    }

    @Override
    protected List<ClaimItem> doInBackground(Integer... sessionID) {
        try{
            List<ClaimItem> claims = WSHelper.listClaims(sessionID[0]);
            return claims;
        } catch (Exception e) {
            Toast.makeText(_activity, "Oops, we could not get your claims.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<ClaimItem> list){
        if (list != null){
            ArrayAdapter<ClaimItem> adapter = new ArrayAdapter<ClaimItem>(_gs, android.R.layout.simple_list_item_1, android.R.id.text1, list);
            _listView.setAdapter(adapter);
        }
    }


}
