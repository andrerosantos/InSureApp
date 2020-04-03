package pt.ulisboa.tecnico.sise.insureapp.app;

public class GlobalState {
    private static GlobalState instance;
    private int sessionID;

    private GlobalState() { }

    public int getSessionId(){
        return this.sessionID;
    }

    public void setSessionID(int sessionID){
        this.sessionID = sessionID;
    }

    public static GlobalState getInstance() {
        return instance;
    }
}
