package com.example.ogatafumitoshi.filepostdemo;

import android.app.Activity;
import android.os.Bundle;
import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.mime.content.StringBody;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import java.io.FileOutputStream;
import static android.os.Environment.DIRECTORY_MOVIES;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity.java";
    private File outputRoot;
    private String outputFile;

    public void onCaptureClick(View view) {
        Log.v(TAG, "start capture");
        //do background using AsyncTask
        new PostDataAsyncTask().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void createSampleMovieFile(){
        try {
            File picturesDir = Environment.getExternalStoragePublicDirectory(DIRECTORY_MOVIES);
            outputRoot = new File(picturesDir, "FilePostDemo");
            if (outputRoot.mkdirs()) {
                Log.d("tag","unable to create directory" + outputRoot.getAbsolutePath());
            }
            outputFile = new File(outputRoot, "test.mp4").getAbsolutePath();
            FileOutputStream outputStreamWriter = new FileOutputStream(
                    outputFile);
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class PostDataAsyncTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                postFile();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String lenghtOfFile) {
            // do stuff after posting data
        }
    }

    private void postFile(){
        try{
            createSampleMovieFile();

            String postReceiverUrl = "http://192.168.0.1/upload/upload.php";

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(postReceiverUrl);

            File file = new File(outputFile);
            FileBody fileBody = new FileBody(file);

            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity.addPart("file",fileBody);
            reqEntity.addPart("fileName", new StringBody("sample.mp4"));
            reqEntity.addPart("body", new StringBody("hello!"));
            reqEntity.addPart("userId", new StringBody("12345"));
            httpPost.setEntity(reqEntity);

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {
                String responseStr = EntityUtils.toString(resEntity).trim();
                Log.v(TAG, "Response: " +  responseStr);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}