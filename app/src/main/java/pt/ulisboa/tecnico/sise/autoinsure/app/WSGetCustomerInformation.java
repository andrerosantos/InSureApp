package pt.ulisboa.tecnico.sise.autoinsure.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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
            Customer customer = WSHelper.getCustomerInfo(integer[0]);

            // write claims locally to use in case of absent connection
            String encodedCustomer = JsonCodec.encodeCustomerInfo(customer);
            JsonFileManager.jsonWriteToFile(_gs, InternalProtocol.KEY_CUSTOMER_INFORMATION_FILE + _gs.getSessionId(), encodedCustomer);

            return customer;
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Customer customer){
        if (customer == null){
            String encodedCustomer = JsonFileManager.jsonReadFromFile(_gs, InternalProtocol.KEY_CUSTOMER_INFORMATION_FILE + _gs.getSessionId());

            if (encodedCustomer.equals("")){
                Toast.makeText(_activity, "Oops, we could not get your information!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                customer = JsonCodec.decodeCustomerInfo(encodedCustomer);
            }
        }
        this.name.setText(customer.getName());
        this.birthDate.setText(customer.getDateOfBirth());
        this.fiscalNumber.setText(Integer.toString(customer.getFiscalNumber()));
        this.address.setText(customer.getAddress());
        this.policyNumber.setText(Integer.toString(customer.getPolicyNumber()));
    }
}
