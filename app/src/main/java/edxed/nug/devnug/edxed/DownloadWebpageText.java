package edxed.nug.devnug.edxed;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Nug on 3/25/2015.
 */
public  class DownloadWebpageText extends AsyncTask<String, String, String> {

    public static final String TAG = "DOWNLOAD_WEBPAGE_TEXT";
    private Context context;
    public ArrayList<ViewModel> itemArray = new ArrayList<ViewModel>();
    public ItemDataSource db;

    public DownloadWebpageText(Context actContext) {
        context = actContext;
        db = new ItemDataSource(context);
        if(!db.hasEntries()) {
            db.createBaseTable();
            db.createOrgBaseTable();
        }
    }

    protected String doInBackground(String... urls) {

        Log.d(TAG, "dWT");
        // params comes from the execute() call: params[0] is the url.
        try {
            downloadUrl(urls[0]);
            //return db.getName();
            return "Updating Database";
        } catch (IOException e) {
            return "Cannot open Webpage";
        }
    }

    private ProgressDialog pdia;

    // onPreExecute() - displays during AsyncTask
    protected void onPreExecute()
    {
        super.onPreExecute();
        pdia = new ProgressDialog(context);
        pdia.setMessage("Loading...");
        pdia.setCanceledOnTouchOutside(false);
        pdia.setCancelable(false);
        pdia.show();

    }

    // onPostExecute displays the results of the AsyncTask.
    @SuppressWarnings("unchecked")
    protected void onPostExecute(String result) {
        //updateDatabase();
        pdia.dismiss();
        //textView.setText(result);
        //new updateDatabase().execute(itemArray);
    }

    public void updateDatabase() {
        for(int i = 0; i <itemArray.size(); i++) {
            db.createItem(itemArray.get(i));
        }
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private void downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
	        	 /*
	        	//Setup the parameters
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("Username", "pUser"));
				nameValuePairs.add(new BasicNameValuePair("Password", "pricelistapp"));
				//Add more parameters as necessary

				//Create the HTTP request
				HttpParams httpParameters = new BasicHttpParams();

				//Setup timeouts
				HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
				HttpConnectionParams.setSoTimeout(httpParameters, 15000);			

				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpPost httppost = new HttpPost(priceListURL);
				//httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));        
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
	            */

            //URL url = new URL(priceListURL);
            //Log.d(TAG,"TRYING");
            //HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //conn.setReadTimeout(10000 /* milliseconds */);
            //conn.setConnectTimeout(15000 /* milliseconds */);
            //conn.setRequestMethod("POST");
            //String credentials = "pUser" + ":" + "pricelistapp";
            //String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            //conn.setRequestProperty("Authorization", "Basic " + base64EncodedCredentials);
            //conn.setDoInput(true); 
            // Starts the query
            //conn.connect();

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("name", "name"));
            //Add more parameters as necessary

            //Create the HTTP request
            HttpParams httpParameters = new BasicHttpParams();

            //Setup timeouts
            HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
            HttpConnectionParams.setSoTimeout(httpParameters, 15000);

            HttpClient httpclient = new DefaultHttpClient(httpParameters);
            //Old
            //HttpPost httppost = new HttpPost("http://192.241.187.197/pricelist/test.php");

            //New
            HttpPost httppost = new HttpPost("http://192.241.187.197/edxed.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //httppost.setHeader("Authorization", "Basic " + base64EncodedCredentials);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            String result = EntityUtils.toString(entity);
            //System.out.println(result);
            // Create a JSON object from the request response
            //JSONObject jsonObject = new JSONObject(result);
            JSONArray jArray = new JSONArray(result);
            for(int i = 0; i < jArray.length(); i++) {
                JSONObject obj = jArray.getJSONObject(i);
                //System.out.println("What is the tool? " + obj.getString("itemClass"));
                Log.d(TAG, obj.getString("name") + " " + obj.getString("title") + " " + obj.getString("desc") + " " + obj.getString("room") + " " + obj.getString("session") + " " + obj.getInt("last_update"));
                itemArray.add(new ViewModel(obj.getString("name"), obj.getString("title"), obj.getString("desc"), obj.getString("room"), obj.getString("session"), obj.getInt("last_update"), obj.getInt("_id")));
            }
            updateDatabase();
            //int response = entity..getResponseCode();
            Log.d(TAG, "The response is: " + response);
            //is = conn.getInputStream();

            // Convert the InputStream into a string
            //readIt(is, len);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}
