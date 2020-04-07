package pt.ulisboa.tecnico.sise.autoinsure.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class WSNewClaimPlates extends AsyncTask<Integer, Void, List<String>> {
    public static final String TAG = "WSNewClaimPlate";

    private GlobalState _globalState;
    private Activity _activity;
    private Spinner _spinner;


    public WSNewClaimPlates(GlobalState globalState, Activity activity, Spinner spinner){
        this._globalState = globalState;
        this._activity = activity;
        this._spinner = spinner;
    }

    @Override
    protected List<String> doInBackground(Integer... integers) {
        try {
            List<String> plates = WSHelper.listPlates(integers[0]);

            // save plates
            String encodedPlates = JsonCodec.encodePlateList(plates);
            JsonFileManager.jsonWriteToFile(_globalState, InternalProtocol.KEY_USER_PLATES_FILE + _globalState.getSessionId(), encodedPlates);

            return plates;

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<String> plates){
        if (plates == null){
            String encodedPlates = JsonFileManager.jsonReadFromFile(_globalState, InternalProtocol.KEY_USER_PLATES_FILE + _globalState.getSessionId());

            if (encodedPlates.equals("")){
                Toast.makeText(_activity, "Could not get your associated plates.", Toast.LENGTH_SHORT).show();
                return;
            } else
                plates = JsonCodec.decodePlateList(encodedPlates);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(_globalState, android.R.layout.simple_list_item_1, android.R.id.text1, plates);
        this._spinner.setAdapter(adapter);

    }
}
