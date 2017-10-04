package com.example.karthik.healthmonitor;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import android.graphics.Canvas;
import android.view.View;
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    GraphView view;
    LinearLayout top;
    Random r = new Random();
    boolean stop = true;
    float values[] = new float[30];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        final Button run = (Button) findViewById(R.id.button);
        Button stops = (Button) findViewById(R.id.button4);

        top = (LinearLayout) findViewById(R.id.graph);
        //On click Event of Run button
        run.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view1) {
                if(stop == true){
                    String title = " ";

                    float min = 500, max = 2200;
                    for (int i = 0; i < 30; i++)
                        values[i] = min + r.nextFloat() * (max - min);
                    String horLabels[] = new String[5];
                    String verLabels[] = new String[4];
                    int start = 500;
                    for (int i = 1; i < 5; i++)
                        verLabels[i - 1] = 500 * (i - 1) + "";
                    start = 2700;
                    for (int i = 0; i < 5; i++)
                        horLabels[i] = start + (i * 50) + "";
                    view = new GraphView(view1.getContext(), values, title, horLabels, verLabels, true);
                    stop = false;
                    top.addView(view);
                    drawGraph();
                }

            }
        });
        //On click Event of Stop button
        stops.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view2) {
                stop = true;
                top.removeView(view);

            }
        });
    }

    //Generating graph
    public void drawGraph() {
        if(stop==false)
        {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawGraph();
                }
            },600);
            float min = 500, max = 2200;
            // Remove the first value
            for (int i = 0; i < 29; i++)
                values[i] = values[i+1];
            // Adding new random value to the end
            values[29] = min + r.nextFloat() * (max - min);
            top.removeView(view);
            view.setValues(values);
            top.addView(view);
        }
    }
}
