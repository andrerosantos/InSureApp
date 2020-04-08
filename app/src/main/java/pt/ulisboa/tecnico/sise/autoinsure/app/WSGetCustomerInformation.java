package pt.ulisboa.tecnico.sise.autoinsure.app;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import pt.ulisboa.tecnico.sise.autoinsure.app.activities.LoginActivity;
import pt.ulisboa.tecnico.sise.autoinsure.datamodel.Customer;

public class WSGetCustomerInformation extends AsyncTask<Integer, Void, Customer> {
    public final static String TAG = "GetCustomerInformation";

    private GlobalState _gs;
    private Activity _activity;
    private TextView name;
    private TextView birthDate;
    private TextView fiscalNumber;
    private TextView address;
    private TextView policyNumber;
    private boolean wrongSessionId = false;

    public WSGetCustomerInformation(GlobalState globalState, Activity activity, TextView name, TextView birthDate, TextView fiscalNumber, TextView address, TextView policyNumber){
        this._gs = globalState;
        this._activity = activity;
        this.name = name;
        this.birthDate = birthDate;
        this.fiscalNumber = fiscalNumber;
        this.address = address;
        this.policyNumber = policyNumber;
    }

    @Override
    protected Customer doInBackground(Integer... integer) {

        try {
            // get information from server
            Customer customer = WSHelper.getCustomerInfo(integer[0]);

            // if no customer or wrong username associated with current session ID
            if(customer == null || (!customer.getUsername().equals(_gs.getUsername()))){
                this.wrongSessionId = true;
                Log.d(TAG, "Wrong session id");
                return null;
            }

            // write claims locally to use in case of absent connection
            String encodedCustomer = JsonCodec.encodeCustomerInfo(customer);
            JsonFileManager.jsonWriteToFile(_gs, InternalProtocol.KEY_CUSTOMER_INFORMATION_FILE + _gs.getUsername(), encodedCustomer);

            return customer;
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());

            // no server connection
            // try to get information locally
            String encodedCustomer = null;
            try {
                encodedCustomer = JsonFileManager.jsonReadFromFile(_gs, InternalProtocol.KEY_CUSTOMER_INFORMATION_FILE + _gs.getUsername());
                Customer customer = JsonCodec.decodeCustomerInfo(encodedCustomer);

                Log.d(TAG, "Showing customer information saved locally.");
                return customer;

            } catch (Exception ex){
                // there is no local information
                Log.d(TAG, "No customer information saved locally.");
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Customer customer){
        if (customer == null){
            if (this.wrongSessionId){
                Toast.makeText(_activity, "Invalid session id. Please login!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(_activity, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                _activity.startActivity(intent);
                _activity.finish();
            } else {
                Toast.makeText(_activity, "Oops, we could not get your information!", Toast.LENGTH_SHORT).show();
            }
        } else {
            this.name.setText(customer.getName());
            this.birthDate.setText(customer.getDateOfBirth());
            this.fiscalNumber.setText(Integer.toString(customer.getFiscalNumber()));
            this.address.setText(customer.getAddress());
            this.policyNumber.setText(Integer.toString(customer.getPolicyNumber()));
        }
    }
}
