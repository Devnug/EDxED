package edxed.nug.devnug.edxed;

import android.content.Context;
import android.os.AsyncTask;
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
import java.util.ArrayList;

/**
 * Created by Nug on 4/19/2015.
 */
public class LoadingScheduleTask extends AsyncTask<String, Integer, Integer> {

    ItemDataSource db;
    public static final String TAG = "LoadingScheduleTask";
    public ArrayList<ViewModel> itemArray = new ArrayList<ViewModel>();

    public interface LoadingScheduleTaskFinishedListener {
        void onScheduleTaskFinished(); // If you want to pass something back to the listener add a param to this method
    }

    // This is the progress bar you want to update while the task is in progress
    //private final ProgressBar progressBar;
    // This is the listener that will be told when this task is finished
    private final LoadingScheduleTaskFinishedListener finishedListener;

    /**
     * A Loading task that will load some resources that are necessary for the app to start
     * @param context - context of the application
     * @param finishedListener - the listener that will be told when this task is finished
     */
    public LoadingScheduleTask(Context context, LoadingScheduleTaskFinishedListener finishedListener) {
        //this.progressBar = progressBar;
        db = new ItemDataSource(context);
        if(!db.hasEntries()) {
            db.createBaseTable();
            db.createOrgBaseTable();
            db.createScheduleBaseTable();
        }
        this.finishedListener = finishedListener;
    }

    @Override
    protected Integer doInBackground(String... params) {
        Log.i("Tutorial", "Starting task with url: " + params[0]);
        if(resourcesDontAlreadyExist()){
            try {
                downloadResources();
            }
            catch(IOException e) {
                Log.d(TAG, e.getStackTrace() + "");
            }
        }
        // Perhaps you want to return something to your post execute
        return 1234;
    }

    private boolean resourcesDontAlreadyExist() {
        // Here you would query your app's internal state to see if this download had been performed before
        // Perhaps once checked save this in a shared preference for speed of access next time
        return true; // returning true so we show the splash every time
    }

    public void updateDatabase() {
        for(int i = 0; i <itemArray.size(); i++) {
            db.createScheduleItem(itemArray.get(i));
        }
    }

    private void downloadResources()  throws IOException {
        // We are just imitating some process thats takes a bit of time (loading of resources / downloading)
        if(!db.hasEntries()) {
            db.createBaseTable();
            db.createOrgBaseTable();
            db.createScheduleBaseTable();
        }
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("schedule", "schedule"));
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
            // System.out.println(result);
            // Create a JSON object from the request response
            // JSONObject jsonObject = new JSONObject(result);
            JSONArray jArray = new JSONArray(result);
            for(int i = 0; i < jArray.length(); i++) {
                JSONObject obj = jArray.getJSONObject(i);
                //System.out.println("What is the tool? " + obj.getString("itemClass"));
                Log.d(TAG, obj.getString("name") + " " + " " + obj.getString("room") + " " + obj.getString("time"));
                itemArray.add(new ViewModel(obj.getString("name"), "", "", obj.getString("room"), "true", obj.getString("time"), "", ""));
            }
            updateDatabase();
            //int response = entity..getResponseCode();
            Log.d(TAG, "The response is: " + response);
            //is = conn.getInputStream();

            // Convert the InputStream into a string
            // readIt(is, len);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d(TAG, "Error when reading json");
        } finally {
            if (is != null) {
                is.close();
            }
        }
            try { Thread.sleep(1000); } catch (InterruptedException ignore) {}
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        //progressBar.setProgress(values[0]); // This is ran on the UI thread so it is ok to update our progress bar ( a UI view ) here
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        finishedListener.onScheduleTaskFinished();// Tell whoever was listening we have finished
    }
}
