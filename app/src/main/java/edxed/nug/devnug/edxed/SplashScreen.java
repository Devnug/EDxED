package edxed.nug.devnug.edxed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import edxed.nug.devnug.edxed.ItemDataSource;
import edxed.nug.devnug.edxed.NavigationDrawerFragment;
import edxed.nug.devnug.edxed.R;

/**
 * Created by Nug on 4/17/2015.
 */
public class SplashScreen extends Activity implements LoadingTask.LoadingTaskFinishedListener, LoadingScheduleTask.LoadingScheduleTaskFinishedListener {

    ItemDataSource db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Show the splash screen
        setContentView(R.layout.splash);
        // Find the progress bar
        //ProgressBar progressBar = (ProgressBar) findViewById(R.id.activity_splash_progress_bar);
        // Start your loading
        new LoadingTask(this, this).execute("www.google.co.uk"); // Pass in whatever you need a url is just an example we don't use it in this tutorial
    }

    // This is the callback for when your async task has finished
    @Override
    public void onTaskFinished(boolean error) {
        //new LoadingScheduleTask(this,this).execute("");
        if(error)
            Toast.makeText(this, "Connection Error.  Data may not be up to date.", Toast.LENGTH_LONG);
        completeSplash();
    }

    public void onScheduleTaskFinished() {
        completeSplash();
    }

    private void completeSplash(){
        startApp();
        //finish(); // Don't forget to finish this Splash Activity so the user can't return to it!
    }

    private void startApp() {
        //Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        //startActivity(intent);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //Create an intent that will start the main activity.
                Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                SplashScreen.this.startActivity(mainIntent);

                //Finish splash activity so user cant go back to it.
                //SplashScreen.this.finish();

                //Apply splash exit (fade out) and main entry (fade in) animation transitions.
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                SplashScreen.this.finish();
            }
        }, 1000);
    }

}

