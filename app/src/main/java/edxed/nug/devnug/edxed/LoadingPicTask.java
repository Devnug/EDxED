package edxed.nug.devnug.edxed;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Nug on 4/19/2015.
 */
public class LoadingPicTask extends AsyncTask<String, Integer, Integer> {

    ItemDataSource db;
    public static final String TAG = "Loading Task";
    public ArrayList<ViewModel> itemArray = new ArrayList<ViewModel>();
    public Bitmap pic;

    public interface LoadingPicTaskFinishedListener {
        void onTaskFinished(Bitmap pic); // If you want to pass something back to the listener add a param to this method
    }

    // This is the progress bar you want to update while the task is in progress
    //private final ProgressBar progressBar;
    // This is the listener that will be told when this task is finished
    private final LoadingPicTaskFinishedListener finishedListener;

    /**
     * A Loading task that will load some resources that are necessary for the app to start
     * @param context - context of the application
     * @param finishedListener - the listener that will be told when this task is finished
     */
    public LoadingPicTask(Context context, LoadingPicTaskFinishedListener finishedListener) {
        //this.progressBar = progressBar;
        this.finishedListener = finishedListener;
    }

    @Override
    protected Integer doInBackground(String... params) {
        Log.i("Tutorial", "Starting task with url: " + params[0]);
        if(resourcesDontAlreadyExist()){
            try {
                downloadResources(params[0]);
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


    private void downloadResources(String urlString)  throws IOException {
        // We are just imitating some process thats takes a bit of time (loading of resources / downloading)
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(urlString);
            pic = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        //progressBar.setProgress(values[0]); // This is ran on the UI thread so it is ok to update our progress bar ( a UI view ) here
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        finishedListener.onTaskFinished(pic);// Tell whoever was listening we have finished
    }
}
