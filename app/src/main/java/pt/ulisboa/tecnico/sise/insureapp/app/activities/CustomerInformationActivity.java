package pt.ulisboa.tecnico.sise.insureapp.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pt.ulisboa.tecnico.sise.insureapp.R;
import pt.ulisboa.tecnico.sise.insureapp.app.GlobalState;
import pt.ulisboa.tecnico.sise.insureapp.app.WSGetCustomerInformation;

public class CustomerInformationActivity extends AppCompatActivity {
    private GlobalState _gs;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_information);

        _gs = (GlobalState) getApplicationContext();

        TextView customerName = (TextView) findViewById(R.id.customerViewName);
        TextView birthDate = (TextView) findViewById(R.id.costumerViewBirthdate);
        TextView fiscalNumber = (TextView) findViewById(R.id.costumerViewFiscalNumber);
        TextView address = (TextView) findViewById(R.id.costumerViewAddress);
        TextView policyNumber = (TextView) findViewById(R.id.costumerViewPolicyNumber);

        (new WSGetCustomerInformation(_gs, customerName, birthDate, fiscalNumber, address, policyNumber)).execute(_gs.getSessionId());

        backButton = findViewById(R.id.backButtonCustomerInformation);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(CustomerInformationActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
