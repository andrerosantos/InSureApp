package pt.ulisboa.tecnico.sise.insureapp.app;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.sise.insureapp.datamodel.ClaimItem;
import pt.ulisboa.tecnico.sise.insureapp.datamodel.ClaimMessage;
import pt.ulisboa.tecnico.sise.insureapp.datamodel.ClaimRecord;
import pt.ulisboa.tecnico.sise.insureapp.datamodel.Customer;
import pt.ulisboa.tecnico.sise.insureapp.datamodel.Person;

public class WSHelper {
    private static final String TAG = "WSHelper";
    private static final String NAMESPACE = "http://pt.ulisboa.tecnico.sise.insure.ws/";
    private static final String URL = "http://10.0.2.2:8080/InSureWS?WSDL";
    private static final String serviceName = "InsureWS";
    private static int TIMEOUT = 4000;

    // reusable method to make generic requests
    private static String makeRequest(String method, String... args) throws Exception{
        // create the request
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        SoapObject request = new SoapObject(NAMESPACE, method);
        int paramCounter = 0;
        for(String arg : args){
            request.addProperty("arg" + paramCounter++, arg);
        }
        envelope.setOutputSoapObject(request);

        // make the request
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, TIMEOUT);
        String actionString = "\"" + NAMESPACE + serviceName + "/" + method + "\"";
        androidHttpTransport.call(actionString, envelope);

        // obtain the result
        SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
        return resultsRequestSOAP.toString();
    }

    public static String hello(String name) throws Exception {
        final String METHOD_NAME = "hello";
        return makeRequest(METHOD_NAME, name);
    }

    public static int login(String username, String password) throws Exception {
        final String METHOD_NAME = "login";
        int sessionID = Integer.parseInt(makeRequest(METHOD_NAME, username, password));
        return sessionID;
    }

    public static Customer getCustomerInfo(int sessionId) throws Exception {
        final String METHOD_NAME = "getCustomerInfo";
        String jsonResult = makeRequest(METHOD_NAME, sessionId+"");     // add "" to convert int to String
        try {
            JSONObject jsonRootObject = new JSONObject(jsonResult);
            String username = jsonRootObject.getString("username");
            String customerName = jsonRootObject.getString("name");
            if (customerName == null || customerName.equals("")) return null;
            int fiscalNumber    = Integer.parseInt(jsonRootObject.getString("fiscalNumber"));
            String address      = jsonRootObject.optString("address");
            String dateOfBirth  = jsonRootObject.getString("dateOfBirth");
            int policyNumber    = Integer.parseInt(jsonRootObject.optString("policyNumber"));
            Person person = new Person(customerName, fiscalNumber, address, dateOfBirth);
            return new Customer(username, sessionId, policyNumber, person) ;  // dummy Customer without username and password, just used for details
        }  catch (JSONException e) {
            //e.printStackTrace();
            Log.d(TAG, "getCustomerInfo - JSONResult:" + jsonResult);
        }
        return null;
    }

    public static ClaimRecord getClaimInfo(int sessionId, int claimId) throws Exception {
        final String METHOD_NAME = "getClaimInfo";
        String jsonResult = makeRequest(METHOD_NAME, sessionId+"", claimId+"");
        try {
            JSONObject jsonRootObject = new JSONObject(jsonResult);
            int claimIdResp         = Integer.parseInt(jsonRootObject.getString("claimId"));
            String claimTitle       = jsonRootObject.getString("claimTitle");
            String plate            = jsonRootObject.optString("plate");
            String submissionDate   = jsonRootObject.optString("submissionDate");
            String occurrenceDate   = jsonRootObject.optString("occurrenceDate");
            String description      = jsonRootObject.optString("description");
            String status           = jsonRootObject.optString("status");
            return new ClaimRecord(claimIdResp, claimTitle, submissionDate, occurrenceDate, plate, description, status);
        }  catch (JSONException e) {
            //e.printStackTrace();
            Log.d(TAG, "getClaimInfo - JSONResult:" + jsonResult);
        }
        return null;
    }

    public static List<String> listPlates(int sessionId) throws Exception {
        final String METHOD_NAME = "listPlates";
        String jsonResult = makeRequest(METHOD_NAME, sessionId+"");
        try {
            JSONArray jsonArray = new JSONArray(jsonResult);
            ArrayList<String> plateList = new ArrayList<>();
            for(int i=0; i < jsonArray.length(); i++){
                String plate = (String)jsonArray.getString(i);
                plateList.add(plate);
            }
            return plateList;
        }  catch (JSONException e) {
            //e.printStackTrace();
            Log.d(TAG, "listPlates - JSONResult:" + jsonResult);
        }
        return null;
    }

    public static List<ClaimItem> listClaims(int sessionId) throws Exception {
        final String METHOD_NAME = "listClaims";
        String jsonResult = makeRequest(METHOD_NAME, sessionId+"");
        try {
            JSONArray jsonArray = new JSONArray(jsonResult);
            ArrayList<ClaimItem> claimList = new ArrayList<>();
            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int claimId = Integer.parseInt(jsonObject.optString("claimId"));
                String claimTitle = jsonObject.optString("claimTitle");
                claimList.add(new ClaimItem(claimId, claimTitle));
            }
            return claimList;
        }  catch (JSONException e) {
            //e.printStackTrace();
            Log.d(TAG, "listClaims - JSONResult:" + jsonResult);
        }
        return null;
    }

    public static List<ClaimMessage> listClaimMessages(int sessionId, int claimId) throws Exception {
        final String METHOD_NAME = "listClaimMessages";
        String jsonResult = makeRequest(METHOD_NAME, sessionId+"", claimId+"");
        // parse the result

        try {
            JSONArray jsonArray = new JSONArray(jsonResult);
            ArrayList<ClaimMessage> claimList = new ArrayList<>();
            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                claimList.add(
                        new ClaimMessage(
                                jsonObject.optString("sender"),
                                jsonObject.optString("msg"),
                                jsonObject.optString("date")));
            }
            return claimList;
        }  catch (JSONException e) {
            //e.printStackTrace();
            Log.d(TAG, "listClaimMessages - JSONResult:" + jsonResult);
        }
        return null;
    }

    public static boolean submitNewClaim(int sessionId, String claimTitle, String occurrenceDate, String plate, String claimDescription) throws Exception {
        final String METHOD_NAME = "submitNewClaim";
        String res = makeRequest(METHOD_NAME, sessionId+"", claimTitle, occurrenceDate, plate, claimDescription);
        return res != null && res.equals("true");
    }

    public static boolean submitNewMessage(int sessionId, int claimId, String message) throws Exception {
        final String METHOD_NAME = "submitNewMessage";
        String res = makeRequest(METHOD_NAME, sessionId+"", claimId+"", message);
        return res != null && res.toString().equals("true");
    }

    public static boolean logout(int sessionId) throws Exception {
        final String METHOD_NAME = "logout";
        String res = makeRequest(METHOD_NAME, sessionId+"");
        return res != null && res.toString().equals("true");
    }
}
