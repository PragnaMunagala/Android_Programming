package com.example.karthik.healthmonitor;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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


import static android.R.attr.duration;
import static android.R.id.message;

/**
 * Created by karthik on 10/17/17.
 */

public class ServerUpload extends AsyncTask<String,Integer,String> {
    private Context context;
    public ServerUpload(Context context) {
        this.context = context;
    }
    @Override
    protected String doInBackground(String... s) {
        String urlToUpload = "http://10.218.110.136/CSE535Fall17Folder/UploadToServer.php";
        String filePath = "/storage/emulated/0/Android/data/CSE535_ASSIGNMENT2/data.db";
        //Source:-http://androidexample.com/Upload_File_To_Server_-_Android_Example/index.php?view=article_discription&aid=83
        try {
            HttpURLConnection conn = null;
            DataOutputStream dataOutputStream = null;
            FileInputStream fileInputStream = null;
            File sourceFile = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1048576;
            int serverResponseCode = 0;

            try {
                sourceFile = new File(filePath);
                fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(urlToUpload);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file",filePath);
                dataOutputStream = new DataOutputStream(conn.getOutputStream());
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + sourceFile + "\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dataOutputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data...
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                Log.d("Response:",String.valueOf(serverResponseCode));
                Log.d("Response:",String.valueOf(conn.getResponseCode()));
                Log.d("Response:",String.valueOf(conn.getResponseMessage()));
                if (serverResponseCode != 200) {
                    return "Response from server: " + conn.getResponseCode() + " message: " + conn.getResponseMessage();
                } else {
                    System.out.print("RESPONSE CODE: "+serverResponseCode);
                    System.out.println("RESPONSE MESSAGE: "+conn.getResponseMessage());
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    fileInputStream.close();
                    if(dataOutputStream!=null) {
                        dataOutputStream.flush();
                        dataOutputStream.close();
                    }
                } catch (IOException ignored) {
                }
                if (conn != null)
                    conn.disconnect();
            }
            return null;
        }catch(Exception ex) {
            ex.printStackTrace();
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
            //Snackbar.make(view, "Error in File upload", duration).show();
            Toast.makeText(context,"Error in file uploading: "+result, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"File Successfully uploaded", Toast.LENGTH_SHORT).show();
        }
    }
}
