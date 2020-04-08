package pt.ulisboa.tecnico.sise.autoinsure.app;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.sise.autoinsure.app.activities.LoginActivity;
import pt.ulisboa.tecnico.sise.autoinsure.datamodel.ClaimRecord;

public class GlobalState extends Application {
    private final String TAG = "GlobalState";
    private int _sessionID;
    private String _userName;
    private ArrayList<ClaimRecord> claimsToSubmit = new ArrayList<>();

    public GlobalState() { }

    public int getSessionId(){
        return this._sessionID;
    }

    public void set_sessionID(int _sessionID){
        this._sessionID = _sessionID;
    }

    public String getUsername(){
        return _userName;
    }

    public void setUsername(String userName){
        this._userName = userName;
    }

    public boolean isLoggedIn(){
        return (this._sessionID > 0);
    }

    public void checkSession(Activity activity){
        if (!isLoggedIn()){
            Log.d(TAG, "Not logged in");
            logout(activity);
        } else {
            Log.d(TAG, "Client in session with id " + this._sessionID);
        }
    }

    public void logout(Activity activity){
        this._sessionID = -1;
        Intent intent = new Intent(null, LoginActivity.class);
        startActivity(intent);
    }

    public void addClaimToSubmit(ClaimRecord claim){
        this.claimsToSubmit.add(claim);
    }
}
