package pt.ulisboa.tecnico.sise.autoinsure.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import pt.ulisboa.tecnico.sise.autoinsure.app.GlobalState;
import pt.ulisboa.tecnico.sise.autoinsure.app.WSNewClaim;
import pt.ulisboa.tecnico.sise.autoinsure.R;
import pt.ulisboa.tecnico.sise.autoinsure.app.WSNewClaimPlates;

public class NewClaimActivity extends AppCompatActivity {
    public static final String TAG = "NewClaim";

    private GlobalState _gs;

    private TextView claimTitle;
    private Spinner carPlate;
    private TextView occDate;
    private TextView description;

    private Button calendarButton;
    private Button backButton;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_claim);

        _gs = (GlobalState) getApplicationContext();

        this.claimTitle = (TextView) findViewById(R.id.newClaimTitle);
        this.carPlate = (Spinner) findViewById(R.id.spinnerClaimCarPlate);
        this.occDate = (TextView) findViewById(R.id.newClaimDate);
        this.description = (TextView) findViewById(R.id.newClaimDescription);

        (new WSNewClaimPlates(this._gs, NewClaimActivity.this, this.carPlate, getApplicationContext())).execute(_gs.getSessionId());

        this.calendarButton = (Button) findViewById(R.id.calendarButtonNewClaim);
        this.backButton = (Button) findViewById(R.id.backButtonNewClaim);
        this.submitButton = (Button) findViewById(R.id.submitButtonNewClaim);

        this.calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int selDay = calendar.get(Calendar.DAY_OF_MONTH);
                int selMonth = calendar.get(Calendar.MONTH);
                int selYear = calendar.get(Calendar.YEAR);

                DatePickerDialog date = new DatePickerDialog(NewClaimActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        occDate.setText(dateFormat.format(calendar.getTime()));
                    }
                }, selYear, selMonth, selDay);
                date.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                date.show();
            }
        });

        this.submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if (claimTitle.getText().toString().equals("")){
                    Toast.makeText(NewClaimActivity.this, "A claim title is needed", Toast.LENGTH_SHORT).show();
                } else if (carPlate.getSelectedItem().toString().equals("")){
                    Toast.makeText(NewClaimActivity.this, "A car plate is needed", Toast.LENGTH_SHORT).show();
                }  else if (occDate.getText().toString().equals("")){
                    Toast.makeText(NewClaimActivity.this, "A date is needed", Toast.LENGTH_SHORT).show();
                } else if (description.getText().toString().equals("")){
                    Toast.makeText(NewClaimActivity.this, "A description is needed", Toast.LENGTH_SHORT).show();
                } else {

                    try{
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        dateFormat.setLenient(false);
                        dateFormat.parse(occDate.getText().toString().trim());

                        //if no exception is thrown so far, the date is valid and sent to submission
                        (new WSNewClaim(_gs, NewClaimActivity.this, getApplicationContext())).execute(claimTitle.getText().toString(), occDate.getText().toString(), carPlate.getSelectedItem().toString(), description.getText().toString());

                    } catch (ParseException e) {
                        Toast.makeText(NewClaimActivity.this, "The provided date is no valid", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }
}
