package pt.ulisboa.tecnico.sise.autoinsure.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import pt.ulisboa.tecnico.sise.autoinsure.app.GlobalState;
import pt.ulisboa.tecnico.sise.autoinsure.app.WSLogout;
import pt.ulisboa.tecnico.sise.autoinsure.R;

public class MenuActivity extends AppCompatActivity {
    private final String TAG = "MenuLog: ";
    private GlobalState _gs;

    private Button informationButton;
    private Button historyButton;
    private Button newClaimButton;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        _gs = (GlobalState) getApplicationContext();

        informationButton = findViewById(R.id.informationButton);
        informationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, CustomerInformationActivity.class);
                startActivity(intent);
            }
        });

        historyButton = findViewById(R.id.historyButton);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ListClaimActivity.class);
                startActivity(intent);
            }
        });

        newClaimButton = findViewById(R.id.newClaimButton);
        newClaimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, NewClaimActivity.class);
                startActivity(intent);
            }
        });

        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                (new WSLogout(_gs, MenuActivity.this)).execute();
            }
        });

    }

}