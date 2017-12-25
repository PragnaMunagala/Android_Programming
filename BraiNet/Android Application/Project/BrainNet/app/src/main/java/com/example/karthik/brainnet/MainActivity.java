package com.example.karthik.brainnet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import java.io.IOException;
import java.io.*;
import java.util.Random;
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
import android.os.StrictMode;
import android.widget.TextView;
import android.content.res.AssetManager;
public class MainActivity extends AppCompatActivity {
    AssetManager assetManager;
    private TextView batteryTxt;
    int level;
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            //batteryTxt.setText(String.valueOf(level) + "%");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assetManager = getAssets();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        batteryTxt = (TextView) this.findViewById(R.id.battery);
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    private static double atof(String s)
    {
        double d = Double.valueOf(s).doubleValue();
        if (Double.isNaN(d) || Double.isInfinite(d))
        {
            System.err.print("NaN or Infinity in input\n");
            System.exit(1);
        }
        return(d);
    }


    public void SendUserDataActivity(View view) {
        InputStream input=null;
        OutputStream output=null;
        try {
            input = assetManager.open("TestDataSet.txt");

            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();

            // byte buffer into a string
            String samples[] = new String[10];
            for(int i = 0;i<10;i++)
                samples[i] = "";
            String text = new String(buffer);
            int i = 0;
            for(String line:text.split("\n")) {
                int rowCommaEliminator = 0;
                String lines[] = line.split("\\s+");
                for(String sample:lines)
                {
                    if(rowCommaEliminator++ == lines.length-1)
                        samples[i]+=sample;
                    else
                        samples[i]+=sample+',';
                }
                ++i;
            }
            Random index = new Random();
            int toSend = index.nextInt(10);
            Long startTime = System.currentTimeMillis();
            BufferedOutputStream out = null;
            String urlToDownload = "http://192.168.0.27:5000/classify";
            input = null;
            output = null;
            HttpURLConnection connection = null;
                URL url = new URL(urlToDownload);
                connection =  (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);


                out = new BufferedOutputStream(connection.getOutputStream());

                BufferedWriter writer = new BufferedWriter (new OutputStreamWriter(out, "UTF-8"));

                writer.write(samples[toSend]);

                writer.flush();

                writer.close();

                out.close();

                connection.connect();
                int fileLength = connection.getContentLength();
                input = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(input));
                String response = "";
                StringBuilder sb = new StringBuilder();
                while((response=br.readLine())!=null)
                    sb.append(response);
                System.out.println(sb.toString());
                String result = sb.toString();
                Long endTime = System.currentTimeMillis();
                Long fogExecutionTime = endTime - startTime;
                Intent intent = new Intent(this,UserResultsActivity.class);
                intent.putExtra("UserLabel",result);
                intent.putExtra("FogExectionTime",String.valueOf(fogExecutionTime));
                intent.putExtra("battery",String.valueOf(level));
                startActivity(intent);
            //txtContent.setText(text);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {

            }
        }


    }

    public void SendUserRemoteDataActivity(View view) {
        InputStream input=null;
        OutputStream output=null;
        try {
            input = assetManager.open("TestDataSet.txt");

            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();

            // byte buffer into a string
            String samples[] = new String[10];
            for(int i = 0;i<10;i++)
                samples[i] = "";
            String text = new String(buffer);
            int i = 0;
            for(String line:text.split("\n")) {
                int rowCommaEliminator = 0;
                String lines[] = line.split("\\s+");
                for(String sample:lines)
                {
                    if(rowCommaEliminator++ == lines.length-1)
                        samples[i]+=sample;
                    else
                        samples[i]+=sample+',';
                }
                ++i;
            }
            Random index = new Random();
            int toSend = index.nextInt(10);
            Long startTime = System.currentTimeMillis();
            BufferedOutputStream out = null;
            String urlToDownload = "http://ec2-52-36-107-183.us-west-2.compute.amazonaws.com:5000/classify";
            input = null;
            output = null;
            HttpURLConnection connection = null;
            URL url = new URL(urlToDownload);
            connection =  (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);


            out = new BufferedOutputStream(connection.getOutputStream());

            BufferedWriter writer = new BufferedWriter (new OutputStreamWriter(out, "UTF-8"));

            writer.write(samples[toSend]);

            writer.flush();

            writer.close();

            out.close();

            connection.connect();
            int fileLength = connection.getContentLength();
            input = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String response = "";
            StringBuilder sb = new StringBuilder();
            while((response=br.readLine())!=null)
                sb.append(response);
            System.out.println(sb.toString());
            String result = sb.toString();
            Long endTime = System.currentTimeMillis();
            Long fogExecutionTime = endTime - startTime;
            Intent intent = new Intent(this,UserResultsActivity.class);
            intent.putExtra("UserLabel",result);
            intent.putExtra("FogExectionTime",String.valueOf(fogExecutionTime));
            intent.putExtra("battery",String.valueOf(level));
            startActivity(intent);
            //txtContent.setText(text);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {

            }
        }


    }

    public void ChooseTheBestActivity(View view) {
        WifiManager wifi = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String ip="";
        if (wifi.isWifiEnabled()) {
            ip = "http://192.168.0.27:5000/classify";
            Toast.makeText(getApplicationContext(),"Fog Server is chosen", Toast.LENGTH_SHORT).show();
        }
            else {
            ip = "http://ec2-52-36-107-183.us-west-2.compute.amazonaws.com:5000/classify";
            Toast.makeText(getApplicationContext(), "Remote Server is chosen", Toast.LENGTH_SHORT).show();
        }
            InputStream input = null;
            OutputStream output = null;
            try {
                input = assetManager.open("TestDataSet.txt");

                int size = input.available();
                byte[] buffer = new byte[size];
                input.read(buffer);
                input.close();

                // byte buffer into a string
                String samples[] = new String[10];
                for (int i = 0; i < 10; i++)
                    samples[i] = "";
                String text = new String(buffer);
                int i = 0;
                for (String line : text.split("\n")) {
                    int rowCommaEliminator = 0;
                    String lines[] = line.split("\\s+");
                    for (String sample : lines) {
                        if (rowCommaEliminator++ == lines.length - 1)
                            samples[i] += sample;
                        else
                            samples[i] += sample + ',';
                    }
                    ++i;
                }
                Random index = new Random();
                int toSend = index.nextInt(10);
                Long startTime = System.currentTimeMillis();
                BufferedOutputStream out = null;
                String urlToDownload = ip;
                input = null;
                output = null;
                HttpURLConnection connection = null;
                URL url = new URL(urlToDownload);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);


                out = new BufferedOutputStream(connection.getOutputStream());

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

                writer.write(samples[toSend]);

                writer.flush();

                writer.close();

                out.close();

                connection.connect();
                int fileLength = connection.getContentLength();
                input = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(input));
                String response = "";
                StringBuilder sb = new StringBuilder();
                while ((response = br.readLine()) != null)
                    sb.append(response);
                System.out.println(sb.toString());
                String result = sb.toString();
                Long endTime = System.currentTimeMillis();
                Long fogExecutionTime = endTime - startTime;
                Intent intent = new Intent(this, UserResultsActivity.class);
                intent.putExtra("UserLabel", result);
                intent.putExtra("FogExectionTime", String.valueOf(fogExecutionTime));
                intent.putExtra("battery", String.valueOf(level));
                startActivity(intent);
                //txtContent.setText(text);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {

                }
            }

        }

    }

