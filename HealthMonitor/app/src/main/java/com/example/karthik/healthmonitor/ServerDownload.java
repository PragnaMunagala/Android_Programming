package com.example.karthik.healthmonitor;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by karthik on 10/17/17.
 */
//Source - http://www.coderzheaven.com/2012/04/29/download-file-android-device-remote-server-custom-progressbar-showing-progress/
public class ServerDownload extends AsyncTask<String,Integer,String> {
    private Context context;
    public ServerDownload(Context context) {
        this.context = context;
    }
    @Override
    protected String doInBackground(String... urlToDownload) {
        return this.getDatabaseFromServer();
    }
    public String getDatabaseFromServer(){
        String urlToDownload = "http://10.218.110.136/CSE535Fall17Folder/data.db";
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlToDownload);
            connection =  (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.connect();
            int fileLength = connection.getContentLength();
            input = connection.getInputStream();
            File dbFile = new File("/storage/emulated/0/Android/data/CSE535_ASSIGNMENT2_EXTRA/data.db");
            output = new FileOutputStream(dbFile,false);
            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
                return ignored.toString();
            }
        }
        return null;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
    }
    @Override
    protected void onPostExecute(String result) {
        if (result != null){
            Toast.makeText(context,"Error while downloading: "+result, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Downloaded database", Toast.LENGTH_SHORT).show();
        }
    }
}
