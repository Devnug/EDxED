package edxed.nug.devnug.edxed;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.GregorianCalendar;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    public ItemDataSource db;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private Context mContext;
    private static final String TAG = "MAIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        restoreActionBar();
        mContext = this.getApplicationContext();
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        db = new ItemDataSource(this);
        //db.resetAllTables();
        // Create database on first run
        if(!db.hasEntries()) {
            db.createBaseTable();
            db.createOrgBaseTable();
        }
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        //updateList();
    }

    public void updateList() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        //Log.d(TAG, networkInfo.toString());
        //Log.d(DEBUG_TAG, stringUrl);
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageText(this).execute("");
            //Log.d(TAG, "true");
        } else {
            Toast.makeText(this, "No network connection available", Toast.LENGTH_LONG).show();
            //Toast.makeText(this, textView.setText("No network connection available.");
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (position == 0)
            fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1)).addToBackStack(null)
                    .commit();
        if (position == 1) {
            this.invalidateOptionsMenu();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, ActivityFeedFragment.newInstance(position + 1)).addToBackStack(null)
                    .commit();
        }
        if (position == 2)
            fragmentManager.beginTransaction()
                    .replace(R.id.container, ConversationFragment.newInstance(position + 1)).addToBackStack(null)
                    .commit();
        if (position == 3)
            fragmentManager.beginTransaction()
                    .replace(R.id.container, FeedbackFragment.newInstance(position + 1)).addToBackStack(null)
                    .commit();
        if (position == 4)
            fragmentManager.beginTransaction()
                    .replace(R.id.container, OrganizersFragment.newInstance(position + 1)).addToBackStack(null)
                    .commit();
        if (position == 5)
            fragmentManager.beginTransaction()
                    .replace(R.id.container, ScheduleFragment.newInstance(position + 1)).addToBackStack(null)
                    .commit();
        /*
        if(position == 6)
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PlaceholderFragment.newInstance(position + 1)).addToBackStack(null)
                    .commit();
                    */
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
            case 6:
                mTitle = getString(R.string.title_section6);
                break;
            case 7:
                mTitle = getString(R.string.title_section6);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            createAboutDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("About the Developer");
        //builder.setIcon(R.drawable.patrick);
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.about_dialog, null);
        ImageView image = (ImageView) dialoglayout.findViewById(R.id.image);
        image.setImageResource(R.drawable.patrick);
        final TextView twitter = (TextView) dialoglayout.findViewById(R.id.dev_twitter);
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(TAG, "To Twitter");
                final View contextView = v;
                Uri address= Uri.parse("http://www.twitter.com/" + twitter.getText().toString().substring(1));
                Intent browser= new Intent(Intent.ACTION_VIEW, address);
                v.getContext().startActivity(browser);
            }
        });
        builder.setView(dialoglayout);
        //builder.setMessage(R.string.about_the_dev)
        builder.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                    }
                });
        // Create the AlertDialog object and return it
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() == 1) {
            super.onBackPressed();
            finish();
            //this.onDestroy();
            //Log.d(TAG, "No fragments in backstack");
        }
        else {
            //Log.d(TAG, "Fragments in backstack");
            getSupportFragmentManager().popBackStack();
            //restoreActionBar();
        }

    }


}
