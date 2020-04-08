package pt.ulisboa.tecnico.sise.autoinsure.app;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.sise.autoinsure.datamodel.ClaimItem;
import pt.ulisboa.tecnico.sise.autoinsure.datamodel.ClaimRecord;
import pt.ulisboa.tecnico.sise.autoinsure.datamodel.Customer;
import pt.ulisboa.tecnico.sise.autoinsure.datamodel.Person;

public class JsonCodec {

    private static final String TAG = "JsonCodec";

    public static Customer decodeCustomerInfo(String jsonResult) {
        Customer customer = null;
        Log.i(TAG, "decodeCustomerInfo:" + jsonResult);
        try {
            JSONObject jsonRootObject = new JSONObject(jsonResult);
            String username = jsonRootObject.getString("username");
            String customerName = jsonRootObject.getString("name");
            int sessionId = Integer.parseInt(jsonRootObject.getString("sessionId"));
            int fiscalNumber = Integer.parseInt(jsonRootObject.getString("fiscalNumber"));
            String address = jsonRootObject.optString("address");
            String dateOfBirth = jsonRootObject.getString("dateOfBirth");
            int policyNumber = Integer.parseInt(jsonRootObject.optString("policyNumber"));
            Person person = new Person(customerName, fiscalNumber, address, dateOfBirth);
            customer = new Customer(username, sessionId, policyNumber, person);
        } catch (JSONException e) {
            //e.printStackTrace();
            Log.d(TAG, "decodeCustomerInfo:" + jsonResult);
        }
        return customer;
    }

    public static String encodeCustomerInfo(Customer customer) throws Exception {
        if (customer == null) return "";
        JSONObject jsonCustomerInfo = new JSONObject();
        jsonCustomerInfo.put("username", customer.getUsername());
        jsonCustomerInfo.put("name", customer.getName());
        jsonCustomerInfo.put("sessionId", customer.getSessionId());
        jsonCustomerInfo.put("fiscalNumber", customer.getFiscalNumber());
        jsonCustomerInfo.put("address", customer.getAddress());
        jsonCustomerInfo.put("dateOfBirth", customer.getDateOfBirth());
        jsonCustomerInfo.put("policyNumber", customer.getPolicyNumber());
        Log.i(TAG, "decodeCustomerInfo:" + jsonCustomerInfo.toString());
        return jsonCustomerInfo.toString();
    }

    public static ClaimRecord decodeClaimRecord(String jsonResult) {
        ClaimRecord claimRecord = null;
        Log.i(TAG, "decodeClaimRecord:" + jsonResult);
        try {
            JSONObject jsonRootObject = new JSONObject(jsonResult);
            int claimIdResp = Integer.parseInt(jsonRootObject.getString("claimId"));
            String claimTitle = jsonRootObject.getString("claimTitle");
            String plate = jsonRootObject.optString("plate");
            String submissionDate = jsonRootObject.optString("submissionDate");
            String occurrenceDate = jsonRootObject.optString("occurrenceDate");
            String description = jsonRootObject.optString("description");
            String status = jsonRootObject.optString("status");
            claimRecord = new ClaimRecord(claimIdResp, claimTitle, submissionDate, occurrenceDate,
                    plate, description, status);
        } catch (JSONException e) {
            Log.d(TAG, "decodeClaimRecord:" + jsonResult);
        }
        return claimRecord;
    }

    public static String encodeClaimRecord(ClaimRecord claimRecord) throws Exception {
        if (claimRecord == null) return "";
        JSONObject jsonClaim = new JSONObject();
        jsonClaim.put("claimId", claimRecord.getId());
        jsonClaim.put("claimTitle", claimRecord.getTitle());
        jsonClaim.put("plate", claimRecord.getPlate());
        jsonClaim.put("submissionDate", claimRecord.getSubmissionDate());
        jsonClaim.put("occurrenceDate", claimRecord.getOccurrenceDate());
        jsonClaim.put("description", 	claimRecord.getDescription());
        jsonClaim.put("status", 		claimRecord.getStatus());
        Log.i(TAG, "encodeClaimRecord:" + jsonClaim.toString());
        return jsonClaim.toString();
    }

    public static List<String> decodePlateList(String jsonResult) {
        ArrayList<String> plateList = null;
        Log.i(TAG, "decodePlateList:" + jsonResult);
        try {
            JSONArray jsonArray = new JSONArray(jsonResult);
            plateList = new ArrayList<>();
            for(int i=0; i < jsonArray.length(); i++){
                String plate = jsonArray.getString(i);
                plateList.add(plate);
            }
        }  catch (JSONException e) {
            Log.d(TAG, "decodePlateList:" + jsonResult);
        }
        return plateList;
    }

    public static String encodePlateList(List<String> plateList) throws Exception {
        if(plateList == null) return "";
        JSONArray jsonPlateList = new JSONArray();
        for(String plate : plateList) {
            jsonPlateList.put(plate);
        }
        Log.i(TAG, "encodePlateList:" + jsonPlateList.toString());
        return jsonPlateList.toString();
    }


    public static List<ClaimItem> decodeClaimList(String jsonResult) {
        ArrayList<ClaimItem> claimList = null;
        Log.i(TAG, "decodeClaimList:" + jsonResult);
        try {
            JSONArray jsonArray = new JSONArray(jsonResult);
            claimList = new ArrayList<>();
            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int claimId = Integer.parseInt(jsonObject.optString("claimId"));
                String claimTitle = jsonObject.optString("claimTitle");
                claimList.add(new ClaimItem(claimId, claimTitle));
            }
        }  catch (JSONException e) {
            Log.d(TAG, "decodeClaimList:" + jsonResult);
        }
        return claimList;
    }

    public static List<ClaimRecord> decodeClaimRecordList(String jsonResult){
        ArrayList<ClaimRecord> claimList = null;
        Log.i(TAG, "decodeclaimRecordList: " + jsonResult);
        try{
            JSONArray jsonArray = new JSONArray(jsonResult);
            claimList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int claimIdResp = Integer.parseInt(jsonObject.getString("claimId"));
                String claimTitle = jsonObject.getString("claimTitle");
                String plate = jsonObject.optString("plate");
                String submissionDate = jsonObject.optString("submissionDate");
                String occurrenceDate = jsonObject.optString("occurrenceDate");
                String description = jsonObject.optString("description");
                String status = jsonObject.optString("status");
            }
        } catch (JSONException e) {
            Log.d(TAG, "decodeClaimRecordList:" + jsonResult);
        }
        return claimList;
    }

    public static String encodeClaimList(List<ClaimItem> claimItemList) throws Exception {
        if (claimItemList == null) return "";
        JSONArray jsonClaimList = new JSONArray();
        for (int i = 0; i < claimItemList.size(); i++) {
            ClaimItem c = claimItemList.get(i);
            JSONObject jsonClaim = new JSONObject();
            jsonClaim.put("claimId", 	c.getId());
            jsonClaim.put("claimTitle", c.getTitle());
            jsonClaimList.put(jsonClaim);
        }
        Log.i(TAG, "encodeClaimList:" + jsonClaimList.toString());
        return jsonClaimList.toString();
    }

    public static String encodeClaimRecordList(List<ClaimRecord> claimRecordList) throws JSONException {
        if (claimRecordList == null) return "";
        JSONArray jsonClaimList = new JSONArray();
        for (int i = 0; i < claimRecordList.size(); i++){
            ClaimRecord claimRecord = claimRecordList.get(i);
            JSONObject jsonClaim = new JSONObject();
            jsonClaim.put("claimId", claimRecord.getId());
            jsonClaim.put("claimTitle", claimRecord.getTitle());
            jsonClaim.put("plate", claimRecord.getPlate());
            jsonClaim.put("submissionDate", claimRecord.getSubmissionDate());
            jsonClaim.put("occurrenceDate", claimRecord.getOccurrenceDate());
            jsonClaim.put("description", 	claimRecord.getDescription());
            jsonClaim.put("status", 		claimRecord.getStatus());
        }
        Log.i(TAG, "encodedClaimRecord: " + jsonClaimList.toString());
        return jsonClaimList.toString();
    }

    public static String encodeClaimToSave(ClaimRecord newClaim, Context context, String filename) throws JSONException {
        List<ClaimRecord> decodedClaims = null;


        try {
            String previousEncodedClaims = JsonFileManager.jsonReadFromFile(context, filename);
            decodedClaims = decodeClaimRecordList(previousEncodedClaims);
            Log.i(TAG, "There are previous claims to submit");
        } catch (Exception e){
            Log.i(TAG, "No previous claims to submit");
        }

        if(decodedClaims == null){
            decodedClaims = new ArrayList<>();
        }

        Log.d(TAG, "try_0");
        decodedClaims.add(newClaim);
        Log.d(TAG, "try_1");
        String encodedClaims = encodeClaimRecordList(decodedClaims);
        Log.i(TAG, "Claim saved offline for later submission.");

        return encodedClaims;
    }
}