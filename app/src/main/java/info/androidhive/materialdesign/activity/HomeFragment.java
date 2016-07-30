package info.androidhive.materialdesign.activity;

/**
 * Created by Ravi on 29/07/15.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.adapter.HomeAdapter;
import info.androidhive.materialdesign.app.AppConfig;
import info.androidhive.materialdesign.helper.ServiceHandler;
import info.androidhive.materialdesign.model.HomeBean;


public class HomeFragment extends Fragment {

    private ProgressDialog pDialog;

    // URL to get contacts JSON

    ListView lv;

    // JSON Node names
    private static final String TAG_RESULT = "result";
    private static final String TAG_MEMBER = "member";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "firstname";
    private static final String TAG_RELATIONSHIP = "relationship";
    private static final String TAG_PHOTO = "photo";


    // contacts JSONArray
    JSONArray resultarray = null;
    JSONArray memberarray = null;

    // Hashmap for ListView
    ArrayList<HomeBean> contactList;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);




        contactList = new ArrayList<HomeBean>();

        lv = (ListView)rootView.findViewById(R.id.list);

        // Listview on item click listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String name = ((TextView) view.findViewById(R.id.name))
                        .getText().toString();
                String cost = ((TextView) view.findViewById(R.id.email))
                        .getText().toString();
                String description = ((TextView) view.findViewById(R.id.mobile))
                        .getText().toString();
            }

        });

        new GetMembers().execute();


        // Inflate the layout for this fragment
        return rootView;
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetMembers extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {
            // Creating service handler class instance

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("mobile", "9999999999"));
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(AppConfig.GET_MEMBERS_FROM_LOGIN, ServiceHandler.POST, params);
            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node

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
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            Log.d("Response: ", "> " + result);
            if (result != null) {
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    String result_object = jsonObj.optString("result");
                    Log.d("Response of result : ", "> " + result_object);

                    JSONObject member = new JSONObject(result_object);
                    memberarray =    member.getJSONArray(TAG_MEMBER);



                    // looping through All Contacts
                    for (int i = 0; i < memberarray.length(); i++) {
                        JSONObject c = memberarray.getJSONObject(i);

                        HomeBean bean=new HomeBean();
                        bean.setmName(c.getString(TAG_NAME));
                        bean.setmPhoto(c.getString(TAG_PHOTO));
                        bean.setmReletionship(c.getString(TAG_RELATIONSHIP));
                        bean.setmId(c.getString(TAG_ID));

                        contactList.add(bean);
                    }
                    HomeAdapter adp=new HomeAdapter(getActivity(),contactList);
                    lv.setAdapter(adp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
