package com.moebit.myfirstapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private ArrayList<Sensor> availableSensors = new ArrayList<>();
    private BufferedWriter bufferedWriter;
    private File path;

    @TargetApi(23)
    protected void askPermission() {
        String[] permissions = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
        requestPermissions(permissions, 200);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            askPermission();
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        final TextView mainPageTextView = (TextView) findViewById(R.id.firstText);
        final EditText outputFilename = (EditText) findViewById(R.id.filename);
        Button startButton = (Button) findViewById(R.id.startButton);
        Button stopButton = (Button) findViewById(R.id.stopButton);
        final CheckBox chkbox_accelerometer = (CheckBox) findViewById(R.id.accelerometer);
        final CheckBox chkbox_ambienttemp = (CheckBox) findViewById(R.id.ambienttemp);
        final CheckBox chkbox_gravity = (CheckBox) findViewById(R.id.gravity);
        final CheckBox chkbox_gyroscope = (CheckBox) findViewById(R.id.gyroscope);
        final CheckBox chkbox_light = (CheckBox) findViewById(R.id.light);
        final CheckBox chkbox_linearacceleration = (CheckBox) findViewById(R.id.linearAcceleration);
        final CheckBox chkbox_magneticfield = (CheckBox) findViewById(R.id.magneticField);
        final CheckBox chkbox_pressure = (CheckBox) findViewById(R.id.pressure);
        final CheckBox chkbox_proximity = (CheckBox) findViewById(R.id.proximity);
        final CheckBox chkbox_relativehumidity = (CheckBox) findViewById(R.id.relativeHumidity);
        final CheckBox chkbox_rotationvector = (CheckBox) findViewById(R.id.rotationVector);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkbox_accelerometer.isChecked() && sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
                    //Log.d("STATE", "ACCELEROMETER is ready!");
                    availableSensors.add(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
                }// else {
                    //Log.d("Debug", "ACCELEROMETER not available");
                //}
                if (chkbox_ambienttemp.isChecked() && sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
                    //Log.d("STATE", "AMBIENT TEMPERATURE is ready!");
                    availableSensors.add(sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE));
                }
                if (chkbox_gravity.isChecked() && sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null) {
                    availableSensors.add(sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY));
                }
                if (chkbox_gyroscope.isChecked() && sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
                    availableSensors.add(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));
                }
                if (chkbox_light.isChecked() && sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {
                    availableSensors.add(sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT));
                }
                if (chkbox_linearacceleration.isChecked() && sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) != null) {
                    availableSensors.add(sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION));
                }
                if (chkbox_magneticfield.isChecked() && sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
                    availableSensors.add(sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));
                }
                if (chkbox_pressure.isChecked() && sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null) {
                    availableSensors.add(sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE));
                }
                if (chkbox_proximity.isChecked() && sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null) {
                    availableSensors.add(sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY));
                }
                if (chkbox_relativehumidity.isChecked() && sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) != null) {
                    availableSensors.add(sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY));
                }
                if (chkbox_rotationvector.isChecked() && sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) != null) {
                    availableSensors.add(sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR));
                }
                // sensing functions should go here
                if (!outputFilename.getText().toString().isEmpty()) {
                    try {
                        path = (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) ? Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) : getFilesDir();
                        File filename = new File(path, outputFilename.getText().toString() + ".csv");
                        bufferedWriter = new BufferedWriter(new FileWriter(filename));
                        //Log.d("Debug", "Path: " + filename.toString());
                        for (Sensor sensor : availableSensors) {
                            //TODO: check difference between 0 and sensorManager.SENSOR_DELAY_NORMAL)
                            sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
                            //Log.d("Debug", sensor.getName() + " is registered!");
                        }
                        mainPageTextView.setText("Start 3n3ing ... ");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(R.string.emptyFilename).setTitle(R.string.emptyFilenameTitle)
                            .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sensorManager.unregisterListener(sensorEventListener);
                availableSensors.clear();
                mainPageTextView.setText("Stopped 3n3ing ... ");
            }
        });


    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            //long timestamp = sensorEvent.timestamp;
            //float value = sensorEvent.values[0];
            //Log.d("Debug", "onSensorChanged() is running");
            writeSensorData(sensorEvent.sensor.getName(), Long.toString(sensorEvent.timestamp), sensorEvent.values);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private void writeSensorData(String key, String timestamp, float[] values) {
        if (bufferedWriter != null) {
            String line = "";
            if (values != null) {
                for (float value : values) {
                    line += "," + Float.toString(value);
                }
                line = key + ":" + timestamp + ":" + line + "\n";
                try {
                    //Log.d("DATA", line);
                    bufferedWriter.write(line);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
