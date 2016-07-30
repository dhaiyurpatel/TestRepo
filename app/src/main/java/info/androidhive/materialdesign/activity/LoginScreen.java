package info.androidhive.materialdesign.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.app.AppConfig;
import info.androidhive.materialdesign.helper.ServiceHandler;

public class LoginScreen extends AppCompatActivity {
    private EditText inputMobile;
    private Button getotp;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        inputMobile = (EditText)findViewById(R.id.txt_login_mobile);
        getotp = (Button)findViewById(R.id.btn_login_getotp);
        final String mobile = inputMobile.getText().toString().trim();
        findViewById(R.id.btn_login_getotp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = inputMobile.getText().toString().trim();
                if (!mobile.isEmpty()) {
                    // login user
                    new SendRequest_Login().execute(mobile);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please Enter Mobile Number!", Toast.LENGTH_LONG)
                            .show();
                }

               /* startActivity(new Intent(getApplication(), MainActivity.class));
                finish();*/

            }
        });

        /*Change Button Color When User Start to Enter Mobile Number*/

        inputMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private class SendRequest_Login extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(LoginScreen.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {
            // Creating service handler class instance
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            String str_mobile = args[0];
            params.add(new BasicNameValuePair("mobile", str_mobile));
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(AppConfig.GET_OTP, ServiceHandler.POST, params);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("Response: ", "> " + result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            if (result != null) {
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    String status = jsonObj.optString("status");
                    String message = jsonObj.optString("message");

                    if(status == "false"){
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                    }else{
                        Intent intent = new Intent(LoginScreen.this, VerifyOTPScreen.class);
                        intent.putExtra("mobile", inputMobile.getText().toString());
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }

    }
}
