package pt.ulisboa.tecnico.sise.autoinsure.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import pt.ulisboa.tecnico.sise.autoinsure.app.GlobalState;
import pt.ulisboa.tecnico.sise.autoinsure.app.WSNewClaim;
import pt.ulisboa.tecnico.sise.autoinsure.R;

public class NewClaimActivity extends AppCompatActivity {
    public static final String TAG = "NewClaim";

    private GlobalState _gs;

    private TextView claimTitle;
    private TextView carPlate;
    private TextView date;
    private TextView description;

    private Button backButton;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_claim);

        _gs = (GlobalState) getApplicationContext();

        this.claimTitle = (TextView) findViewById(R.id.newClaimTitle);
        this.carPlate = (TextView) findViewById(R.id.newClaimCarPlate);
        this.date = (TextView) findViewById(R.id.newClaimDate);
        this.description = (TextView) findViewById(R.id.newClaimDescription);

        this.backButton = (Button) findViewById(R.id.backButtonNewClaim);
        this.submitButton = (Button) findViewById(R.id.submitButtonNewClaim);

        this.submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if (claimTitle.getText().toString().equals("")){
                    Toast.makeText(NewClaimActivity.this, "A claim title is needed", Toast.LENGTH_SHORT).show();
                } else if (carPlate.getText().toString().equals("")){
                    Toast.makeText(NewClaimActivity.this, "A car plate is needed", Toast.LENGTH_SHORT).show();
                }  else if (date.getText().toString().equals("")){
                    Toast.makeText(NewClaimActivity.this, "A date is needed", Toast.LENGTH_SHORT).show();
                } else if (description.getText().toString().equals("")){
                    Toast.makeText(NewClaimActivity.this, "A description is needed", Toast.LENGTH_SHORT).show();
                } else {

                    (new WSNewClaim(_gs, NewClaimActivity.this)).execute(claimTitle.getText().toString(), date.getText().toString(), carPlate.getText().toString(), description.getText().toString());

                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewClaimActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
