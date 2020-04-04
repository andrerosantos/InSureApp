package pt.ulisboa.tecnico.sise.insureapp.app;

import android.app.Application;
import android.content.Intent;

import pt.ulisboa.tecnico.sise.insureapp.app.activities.LoginActivity;

public class GlobalState extends Application {
    private int _sessionID;

    public GlobalState() { }

    public int getSessionId(){
        return this._sessionID;
    }

    public void set_sessionID(int _sessionID){
        this._sessionID = _sessionID;
    }

    public boolean isLoggedIn(){
        return (this._sessionID > 0);
    }

    public void checkSession(){
        if (!isLoggedIn()){
            logout();
        }
    }

    public void logout(){
        this._sessionID = -1;
        Intent intent = new Intent(null, LoginActivity.class);
    }
}
