package pt.ulisboa.tecnico.sise.insureapp.app;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import pt.ulisboa.tecnico.sise.insureapp.datamodel.Customer;

public class WSGetCustomerInformation extends AsyncTask<Integer, Void, Customer> {
    public final static String TAG = "GetCustomerInformation";

    private GlobalState _gs;
    private TextView name;
    private TextView birthDate;
    private TextView fiscalNumber;
    private TextView address;
    private TextView policyNumber;

    public WSGetCustomerInformation(GlobalState globalState, TextView name, TextView birthDate, TextView fiscalNumber, TextView address, TextView policyNumber){
        this._gs = globalState;
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
            return customer;
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Customer customer){
        this.name.setText(customer.getName());
        this.birthDate.setText(customer.getDateOfBirth());
        this.fiscalNumber.setText(Integer.toString(customer.getFiscalNumber()));
        this.address.setText(customer.getAddress());
        this.policyNumber.setText(Integer.toString(customer.getPolicyNumber()));
    }
}
