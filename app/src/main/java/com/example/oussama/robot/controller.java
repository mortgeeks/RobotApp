package com.example.oussama.robot;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class controller extends AppCompatActivity {

    Button btnOn, btnOff, btnDis, btnup, btndown, btnleft, btnright,stop,send;
    SeekBar speed;
    TextView lumn;
    EditText mess;
    String address = null;String message;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        Intent newint = getIntent();
        address = newint.getStringExtra(devices.EXTRA_ADDRESS); //receive the address of the bluetooth device

        //call the widgtes
        btnOn = (Button)findViewById(R.id.ON);
        btnOff = (Button)findViewById(R.id.OFF);
        btnDis = (Button)findViewById(R.id.Dis);
        btnup = (Button)findViewById(R.id.up);
        btndown = (Button)findViewById(R.id.down);
        btnleft = (Button)findViewById(R.id.left);
        btnright = (Button)findViewById(R.id.right);
        speed = (SeekBar)findViewById(R.id.seekBar);speed.setMax(255);
        lumn = (TextView)findViewById(R.id.lumn);
        stop = (Button) findViewById(R.id.stop);
        send = (Button)findViewById(R.id.send);
        mess = (EditText)findViewById(R.id.mess);
        new ConnectBT().execute(); //Call the class to connect


        //commands to be sent to bluetooth
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = mess.getText().toString();
                send(message);
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });
        btnOn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                turnOn();      //method to turn on
            }
        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                turnOff();   //method to turn off
            }
        });

        btnDis.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Disconnect(); //close connection
            }
        });

        btnup.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                Up();
            }
        });

        btndown.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                Down();
            }
        });

        btnleft.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                turnLeft();
            }
        });

        btnright.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                turnRight();
            }
        });


        speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser==true)
                {
                    lumn.setText(String.valueOf(progress));
                    if (btSocket!=null){
                        try
                        {
                            btSocket.getOutputStream().write(String.valueOf(progress).getBytes());

                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (btSocket!=null){
                    try
                    {
                        btSocket.getOutputStream().write(String.valueOf(progress).getBytes());

                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (btSocket!=null){
                    try
                    {
                        btSocket.getOutputStream().write(String.valueOf(progress).getBytes());

                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }}
            }
        });


    }
    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish(); //return to the first layout

    }
    private void send(String message){
        if (btSocket!=null) //If the btSocket is busy
        {
            try {
                btSocket.getOutputStream().write(message.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }}

    private void stop(){
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("STOP".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void turnOff()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("OFF".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void turnOn()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("ON".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void Up()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("UP".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void Down()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("DOWN".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void turnLeft()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("LEFT".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void turnRight()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("RIGHT".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    // fast way to call Toast
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
    }



    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(controller .this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

}
