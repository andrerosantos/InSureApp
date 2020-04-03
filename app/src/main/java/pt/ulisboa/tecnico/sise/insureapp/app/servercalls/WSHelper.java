package pt.ulisboa.tecnico.sise.insureapp.app.servercalls;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

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

    public static int login(String username, String password) throws Exception {
        final String METHOD_NAME = "login";
        int sessionID = Integer.parseInt(makeRequest(METHOD_NAME, username, password));
        return sessionID;
    }
}
