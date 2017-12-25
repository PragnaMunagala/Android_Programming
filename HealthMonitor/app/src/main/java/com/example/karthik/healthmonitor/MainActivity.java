package com.example.karthik.healthmonitor;

import android.view.*;
import android.widget.*;
import android.Manifest;
import java.util.InputMismatchException;
import java.util.Random;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.*;
import android.os.*;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;




public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private double ax,ay,az;
    private long timestamp;
    Context context;
    myDbHandler db = null;
    EditText age, id, name;
    RadioGroup gender;
    RadioButton genderValue;
    Button submit;
    SQLiteDatabase myDatabase =null;
    String patientTable = new String();
    final Handler graphHandler = new Handler();
    Thread t,t1;
    boolean stop =false, run =false;
    LineGraphSeries<DataPoint> series;
    final float[] val = new float[50];
    final float[] random_val=new float[4];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        context=this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                //LoadMyData(Environment.getExternalStorageDirectory().getAbsolutePath());
                db = new myDbHandler(context);
            } else {
                requestPermission();
            }
        }
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        id =(EditText) findViewById(R.id.editText6);
        name =(EditText) findViewById(R.id.editText4);
        age =(EditText) findViewById(R.id.editText5);
        gender =(RadioGroup) findViewById(R.id.gender);
        myDatabase =db.getWritableDatabase();
        int i=0;
        while(i<12)
        {
            Random r = new Random();
            val[i++] = r.nextFloat();
        }
        i=0;
        while(i<4)
        {
            Random r = new Random();
            random_val[i++] = r.nextFloat();
        }

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getPatientInfo(v);
                stop =true;
            }
        });
        Button stop = (Button) findViewById(R.id.button4);
        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    MainActivity.this.stop =false;
                    run =false;
                }catch(Exception e)
                {

                }
                GraphView layout = (GraphView) findViewById(R.id.graph);
                layout.removeAllSeries();
            }
        });
    }

    public void uploadDatabase(View view){
        String[] s= new String[1];
        ServerUpload operation=new ServerUpload(this);
        operation.execute(s);
    }

    public void downloadDatabase(View view){
        String[] s=new String[1];
        ServerDownload download=new ServerDownload(this);
        download.execute(s);
    }

    protected boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    protected void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //LoadMyData(Environment.getExternalStorageDirectory().getAbsolutePath());
                    db = new myDbHandler(context);
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }
/*public void drawGraph() {
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
* */

    public void createTable(String id, String name,String age,String gender){

        myDatabase =db.getWritableDatabase();
        patientTable =name+'_'+id+'_'+age+'_'+gender;
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+ patientTable +"(timestamp varchar(10) primary key,_xValue float,_yValue float,_zValue float)");
    }

    public void insertValuesToDatabase() {
        Long tsLong = System.currentTimeMillis()/1000;
        Values val;
        val=new Values(ax,ay,az,tsLong);
        db.addValues(val, patientTable);
    }

    public void getPatientInfo(View v){
        String id= this.id.getText().toString();
        String name= this.name.getText().toString();
        String age= this.age.getText().toString();
        int selectedButton= gender.getCheckedRadioButtonId();
        genderValue =(RadioButton) findViewById(selectedButton);
        String gender= genderValue.getText().toString();
        SQLiteDatabase sqldb=db.getWritableDatabase();
        try{
            if(id.isEmpty() || name.isEmpty()|| age.isEmpty() || gender.isEmpty())
            {
                throw new InputMismatchException();
            }
            Integer.parseInt(id);
            Integer.parseInt(age);
            boolean flag=true;
            try{
                Integer.parseInt(name.charAt(0)+"");
                flag=false;
            }catch (Exception e){
                flag=true;
            }
            if(flag)
                createTable(id,name,age,gender);
            else
                Toast.makeText(this, "Enter all the fields", Toast.LENGTH_LONG).show();
            createTable(id,name,age,gender);
            t1=new Thread(new Runnable(){
                public void run(){


                    try{
                        while(true){
                            insertValuesToDatabase();
                            Thread.sleep(1000);
                        }}catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            });
            t1.start();
        }catch(InputMismatchException ime){
            Toast.makeText(this,"Enter all the fields",Toast.LENGTH_LONG).show();
        }
    }


    public void onResume() {
        super.onResume();
        final GraphView graph=(GraphView) findViewById(R.id.graph);
        graph.setTitle("X axis : Timestamp Y axis: X,Y and Z of accelerometer");
        graph.removeAllSeries();
        t = new Thread(new Runnable() {
            @Override
            public void run() {

            drawGraph(graph);
                graphHandler.postDelayed(this, 1000);
            }
        });
        t.start();}
    //}

    public void drawGraph(GraphView graph) {
        graph.removeAllSeries();
        if(stop) {
            String result="";
            if(!patientTable.isEmpty()){
                result= getResult();
                String[] data = result.split("\n");
                int length = data.length;
                if(length>=1){
                    DataPoint[] x = new DataPoint[length];
                    DataPoint[] y = new DataPoint[length];
                    DataPoint[] z = new DataPoint[length];
                    int index=0;
                    int j = length-1;
                    while(j>=0){
                        String i=data[j--];
                        String[] rowData=i.split(",");
                        x[index]=new DataPoint(Long.parseLong(rowData[0]),Double.parseDouble(rowData[1]));
                        y[index]=new DataPoint(Long.parseLong(rowData[0]),Double.parseDouble(rowData[2]));
                        z[index]=new DataPoint(Long.parseLong(rowData[0]),Double.parseDouble(rowData[3]));
                        index++;
                    }
                    Long min=Long.parseLong(data[length-1].split(",")[0]);
                    Long max=Long.parseLong(data[0].split(",")[0]);
                    graph.getViewport().setMaxX(max);
                    graph.getViewport().setMinX(min);
                    series = new LineGraphSeries<DataPoint>(z);
                    series.setColor(Color.RED);
                    graph.addSeries(series);
                    series = new LineGraphSeries<DataPoint>(y);
                    series.setColor(Color.YELLOW);
                    graph.addSeries(series);
                    series = new LineGraphSeries<DataPoint>(x);
                    series.setColor(Color.MAGENTA);
                    graph.addSeries(series);
                    graph.getViewport().setXAxisBoundsManual(true);


                }
            }
        }
        else
            {
                for(int i=0;i<10;i++)
                    val[i]=0;
            graph.removeAllSeries();
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMaxY(15);
            graph.getViewport().setMinY(-15);
            graph.getViewport().setMaxX(10);
            graph.getViewport().setMinX(-10);

        }

    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }
//Source for the following function https://stackoverflow.com/questions/18782829/android-sensormanager-strange-how-to-remapcoordinatesystem
    @Override
    public void onSensorChanged(SensorEvent event) {
        Display mDisplay = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;

        switch (mDisplay.getRotation()) {
            case Surface.ROTATION_0:
                ax = event.values[0];
                ay = event.values[1];
                break;
            case Surface.ROTATION_90:
                ax = -event.values[1];
                ay = event.values[0];
                break;
            case Surface.ROTATION_180:
                ax = -event.values[0];
                ay = -event.values[1];
                break;
            case Surface.ROTATION_270:
                ax = event.values[1];
                ay = -event.values[0];
                break;
        }
        az = event.values[2];
        timestamp = event.timestamp;
    }

    //Retrieve the latest 10 results from the database.
    public String getResult(){

        myDatabase =db.getReadableDatabase();
        String[] columns=new String[]{"timestamp","_xValue","_yValue","_zValue"};
        Cursor cursor= myDatabase.query(patientTable,columns,null,null,null,null,"timestamp desc","10");
        String top10="";
        int timestamp_VALUE=cursor.getColumnIndex("timestamp");
        int x_VALUE=cursor.getColumnIndex("_xValue");
        int y_VALUE=cursor.getColumnIndex("_yValue");
        int z_VALUE=cursor.getColumnIndex("_zValue");
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            top10 = top10 + cursor.getString(timestamp_VALUE) + "," + cursor.getString(x_VALUE) + "," + cursor.getString(y_VALUE) + "," + cursor.getString(z_VALUE) + "\n";
            cursor.moveToNext();
        }
        return top10;
    }

}
