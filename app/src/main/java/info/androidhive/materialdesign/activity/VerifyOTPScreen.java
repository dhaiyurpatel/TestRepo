package info.androidhive.materialdesign.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class VerifyOTPScreen extends AppCompatActivity {
    private EditText verify_otp;
    private TextView otp_desc;
    private Button btn_verify_otp;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otpscreen);

        Bundle bundle = getIntent().getExtras();
        String mobile = bundle.getString("mobile");
        //Toast.makeText(getApplicationContext(),mobile,Toast.LENGTH_LONG).show();

        otp_desc = (TextView)findViewById(R.id.otp_desc);
        otp_desc.setText("One Time Password (OTP) has been sent to your mobile "+mobile+" please enter the same here to login");

        verify_otp = (EditText)findViewById(R.id.txt_vertify_otp_no);
        findViewById(R.id.btn_verify_otp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = verify_otp.getText().toString().trim();

                Bundle bundle = getIntent().getExtras();
                String mobile = bundle.getString("mobile");

                if (!otp.isEmpty()) {
                    // login user
                    new SendRequest_OTP_Verification().execute(mobile,otp);
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

    }


    private class SendRequest_OTP_Verification extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(VerifyOTPScreen.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {
            // Creating service handler class instance
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            String str_mobile = args[0];
            String str_otp = args[1];
            params.add(new BasicNameValuePair("mobile", str_mobile));
            params.add(new BasicNameValuePair("otp", str_otp));

            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(AppConfig.OTP_VERIFICATION, ServiceHandler.POST, params);

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
                        Bundle bundle = getIntent().getExtras();
                        String mobile = bundle.getString("mobile");
                        Intent intent = new Intent(VerifyOTPScreen.this, MainActivity.class);
                        intent.putExtra("mobile", mobile);
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
