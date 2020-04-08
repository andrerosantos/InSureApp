package pt.ulisboa.tecnico.sise.autoinsure.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pt.ulisboa.tecnico.sise.autoinsure.app.GlobalState;
import pt.ulisboa.tecnico.sise.autoinsure.app.WSLogin;
import pt.ulisboa.tecnico.sise.autoinsure.R;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = "LoginLog:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalState globalState = (GlobalState) getApplicationContext();

                EditText editTextUserName = (EditText) findViewById(R.id.userNameInput);
                EditText editTextPassword = (EditText) findViewById(R.id.passwordInput);

                String username = editTextUserName.getText().toString();
                String password = editTextPassword.getText().toString();


                try {
                    (new WSLogin(globalState, LoginActivity.this, getApplicationContext())).execute(username, password).get();

                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                    Toast.makeText(v.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void nextActivity(){
        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
        startActivity(intent);
    }

}
