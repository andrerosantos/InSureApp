package pt.ulisboa.tecnico.sise.insureapp.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import pt.ulisboa.tecnico.sise.insureapp.R;
import pt.ulisboa.tecnico.sise.insureapp.app.GlobalState;

public class MenuActivity extends AppCompatActivity {
    private final String TAG = "MenuLog: ";
    private GlobalState _gs;

    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        _gs = (GlobalState) getApplicationContext();

        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(intent);

                _gs.set_sessionID(-1);


                Log.d(TAG, Integer.toString(_gs.getSessionId()));
            }
        });

    }
}
