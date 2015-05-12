package com.fortnitta.game.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


public class StartupActivity extends Activity implements DialogInterface.OnCancelListener {

    /**
     * Need global reference to dialogs so that we can dismiss them when launching the game. Leaked
     * window error otherwise.
     */
    private ProgressDialog mProgressDialog;
    private AlertDialog mAlertDialog;

    /**
     * Need global reference to AsyncTasks so that I can stop them if we leave the activity.
     */
    private UpdateChecker checker;
    private Updater appUpdater;

    /**
     * Path to new apk file from the server. Temporarily a random apk from the internet.
     */
    private static final String serverApkPath = "http://marshallhampson.com/assets/apks/fortnitta.apk";

    private static final String serverVersionPath = "http://marshallhampson.com/assets/apks/version.txt";

    /**
     * Path of the downloaded apk file.
     */
    private String downloadedApkPath;

    /**
     * Latest version found on the web.
     */
    private int latestVersionCode = -1;

    private boolean updateAttempted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAlertDialog != null && mAlertDialog.isShowing())
            mAlertDialog.dismiss();
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        if (checker != null)
            checker.cancel(true);
        if (appUpdater != null)
            appUpdater.cancel(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (updateAttempted) {
            launchGame();
        }
        else
            displayUpdateDialog();
    }



    void updateApp(){
        appUpdater = new Updater();
        appUpdater.execute(serverApkPath);
        updateAttempted = true;
    }

    void displayUpdateDialog(){
        checker = new UpdateChecker();
        checker.execute(serverVersionPath);

    }

    private class Updater extends AsyncTask<String, Integer, Void>{


        /**
         * Shows the progress of the download.
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(StartupActivity.this);
            mProgressDialog.setMessage("Downloading update...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(false);
    /**
     *  set it to 0 just in case.
     */
            mProgressDialog.setProgress(0);
            mProgressDialog.show();
        }

        /**
         * Updates the progress dialog with download progress.
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressDialog.setProgress(values[0]);
        }


        /**
         * Downloads apk from the given url.
         * @param serverUrl Given url of new apk.
         */
        @Override
        protected Void doInBackground(String... serverUrl) {
            getApkFromWeb(serverUrl[0]);
            return null;
        }

        /**
         * Opens the new apk for installing. User must have "Unknown App" setting checked.
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_VIEW);
            i.setDataAndType(Uri.fromFile(new File(downloadedApkPath)), "application/vnd.android.package-archive" );
            startActivity(i);
        }



        private String getApkFromWeb(String webUrl){
            String appName = getString(R.string.app_name);
    /**
     * Path to where we will download the app
     */
            downloadedApkPath = "/sdcard/" + appName + ".apk";
            try {
                if (!webUrl.endsWith(".apk")){
    /**
     *  MUST dismiss dialog or crash!
     */
                    mProgressDialog.dismiss();
    /**
     *  cancels the download
     */
                    this.cancel(true);
                }
                URL url = new URL(webUrl);
                URLConnection connection = url.openConnection();
                connection.connect();

                int fileLength = connection.getContentLength();


    /**
     *  download the file
     */
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(downloadedApkPath);

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("fortnitta", "Error downloading update: " + e.getMessage());
            }
            return downloadedApkPath;
        }

    }

    private class UpdateChecker extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... webUrl) {
            Log.i("fortnitta", "Fetching latest version code.");
            int latestVersion = getLatestVersion(webUrl[0]);
            return latestVersion;
        }

        @Override
        protected void onPostExecute(Integer version) {
            latestVersionCode = version;
            int curVersionCode = -1;
    /**
     *  Will show if we had an error getting the version number.
     */
            try {
                PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                curVersionCode = packageInfo.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            Log.i("fortnitta", "Current Game Version: " + curVersionCode + " | Latest Game Version: " + latestVersionCode);
            if (curVersionCode < latestVersionCode) {
                mAlertDialog = showUpdateDialog(curVersionCode);
                mAlertDialog.show();
            } else {
                launchGame();
            }
        }

    }


    AlertDialog showUpdateDialog(int currentVersion){
        return new AlertDialog.Builder(StartupActivity.this)
                .setTitle("Update")
                .setMessage("There is a new version available, update now? Current version: " + currentVersion)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
    /**
     *  We want to update!updateDialog.dismiss();
     */
                        updateApp();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
    /**
     *  We don't want to update!
     */
                        launchGame();
    /**
     *  run current version
     */
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();

    }


    private int getLatestVersion(String webUrl){
        if (!webUrl.endsWith(".txt")){
            return -1;
        }
        StringBuilder total = new StringBuilder();
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet(webUrl);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity ht = response.getEntity();
            BufferedHttpEntity buf = new BufferedHttpEntity(ht);
            InputStream is = buf.getContent();
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line + "\n");
            }
        } catch (IOException e) {
            Log.e("FortNitta", "Error: " + e.getMessage());
        }

        return Integer.parseInt(total.toString().replaceAll("\\s",""));
    }

    void launchGame(){
        Intent launchGame = new Intent(this, AndroidLauncher.class);
        startActivity(launchGame);
    }

    /**
     * User cancelled the update, usually by back button or pressing outside of dialog box.
     */
    @Override
    public void onCancel(DialogInterface dialogInterface) {
        Log.e("FortNitta", "Dialog cancelled!");
        appUpdater.cancel(true);
    /**
     *  cancel the update
     */
        checker.cancel(true);
        launchGame();
    }

}
