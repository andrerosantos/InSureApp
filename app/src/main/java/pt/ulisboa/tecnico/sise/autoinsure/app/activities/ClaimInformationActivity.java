package pt.ulisboa.tecnico.sise.autoinsure.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pt.ulisboa.tecnico.sise.autoinsure.app.GlobalState;
import pt.ulisboa.tecnico.sise.autoinsure.app.InternalProtocol;
import pt.ulisboa.tecnico.sise.autoinsure.app.WSGetClaimInformation;
import pt.ulisboa.tecnico.sise.autoinsure.R;

public class ClaimInformationActivity extends AppCompatActivity {
    private static final String TAG = "ClaimInfoActivity";
    private GlobalState _gs;
    private Button backButton;

    private TextView textViewTitle;
    private TextView textViewOccurrenceDate;
    private TextView textViewSubmissionDate;
    private TextView textViewCarPlate;
    private TextView textViewStatus;
    private TextView textViewDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_information);

        this._gs = (GlobalState) getApplicationContext();

        Bundle extras = getIntent().getExtras();
        if (extras == null){
            Log.d(TAG, "Index cannot be null");
            finish();
            return;
        }

        final int index = extras.getInt(InternalProtocol.KEY_READ_CLAIM);

        this.textViewTitle = (TextView) findViewById(R.id.claimInfoTitle);
        this.textViewOccurrenceDate = (TextView) findViewById(R.id.claimInfoOccurrenceDate);
        this.textViewSubmissionDate = (TextView) findViewById(R.id.claimInfoSubmissionDate);
        this.textViewCarPlate = (TextView) findViewById(R.id.claimInfoCarPlate);
        this.textViewStatus = (TextView) findViewById(R.id.claimInfoStatus);
        this.textViewDescription = (TextView) findViewById(R.id.claimInfoDescription);

        (new WSGetClaimInformation(this._gs, ClaimInformationActivity.this,
                this.textViewTitle, this.textViewOccurrenceDate, this.textViewSubmissionDate, this.textViewCarPlate, this.textViewStatus, this.textViewDescription, getApplicationContext())).execute(_gs.getSessionId(), index);

        this.backButton = (Button) findViewById(R.id.backButtonClaimInformation);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

    }
}
