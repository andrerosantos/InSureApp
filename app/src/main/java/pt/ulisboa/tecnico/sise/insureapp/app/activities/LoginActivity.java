package pt.ulisboa.tecnico.sise.insureapp.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pt.ulisboa.tecnico.sise.insureapp.R;
import pt.ulisboa.tecnico.sise.insureapp.app.GlobalState;
import pt.ulisboa.tecnico.sise.insureapp.app.servercalls.WSLogin;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = "LginActivity:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextUserName = (EditText) findViewById(R.id.userNameInput);
                EditText editTextPassword = (EditText) findViewById(R.id.passwordInput);

                String username = editTextUserName.getText().toString();
                String password = editTextPassword.getText().toString();

                WSLogin login = new WSLogin();
                int sessionID = -1;

                try {
                    sessionID = login.execute(username, password).get();

                    if (sessionID > 0){
                        GlobalState.getInstance().setSessionID(sessionID);
                        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                        startActivity(intent);
                    } else {
                        Log.d(TAG, "login successful with session id: " + sessionID);
                        Toast.makeText(v.getContext(), "Wrong username or password", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                    Toast.makeText(v.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
