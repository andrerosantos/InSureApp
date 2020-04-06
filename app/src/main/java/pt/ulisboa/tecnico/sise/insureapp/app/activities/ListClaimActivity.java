package pt.ulisboa.tecnico.sise.insureapp.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import pt.ulisboa.tecnico.sise.insureapp.R;
import pt.ulisboa.tecnico.sise.insureapp.app.GlobalState;
import pt.ulisboa.tecnico.sise.insureapp.app.InternalProtocol;
import pt.ulisboa.tecnico.sise.insureapp.app.WSGetClaimList;
import pt.ulisboa.tecnico.sise.insureapp.datamodel.ClaimItem;

public class ListClaimActivity extends AppCompatActivity {
    private final String TAG = "ListClaimActivity";
    private ListView _listView;
    private List<ClaimItem> claimList;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_claim);

        GlobalState globalState = (GlobalState) getApplicationContext();

        this._listView = (ListView) findViewById(R.id.list_claims);
        (new WSGetClaimList(globalState, ListClaimActivity.this, this._listView)).execute(globalState.getSessionId());

        _listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClaimItem claim = (ClaimItem) parent.getAdapter().getItem(position);
                int claimID = claim.getId();

                Intent intent = new Intent(ListClaimActivity.this, ClaimInformationActivity.class);
                                intent.putExtra(InternalProtocol.KEY_READ_CLAIM, claimID);

                Log.d(TAG, "request claim with id => " + claimID);

                startActivity(intent);
            }
        });

        backButton = findViewById(R.id.backButtonListClaims);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ListClaimActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
