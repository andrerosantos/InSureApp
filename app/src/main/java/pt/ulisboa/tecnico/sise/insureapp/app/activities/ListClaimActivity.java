package pt.ulisboa.tecnico.sise.insureapp.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import pt.ulisboa.tecnico.sise.insureapp.R;
import pt.ulisboa.tecnico.sise.insureapp.app.GlobalState;
import pt.ulisboa.tecnico.sise.insureapp.app.WSGetClaimList;
import pt.ulisboa.tecnico.sise.insureapp.app.WSLogout;
import pt.ulisboa.tecnico.sise.insureapp.datamodel.ClaimItem;

public class ListClaimActivity extends AppCompatActivity {
    private ListView _listView;
    private List<ClaimItem> claimList;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_claim);

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

    @Override
    protected void onStart() {
        super.onStart();

        GlobalState globalState = (GlobalState) getApplicationContext();
        (new WSGetClaimList(globalState, ListClaimActivity.this, (ListView) findViewById(R.id.list_claims))).execute(globalState.getSessionId());

    }
}
