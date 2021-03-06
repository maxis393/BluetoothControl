package com.led_on_off.led;

import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.UUID;


public class ledControl extends ActionBarActivity {

   // Button btnOn, btnOff, btnDis;
    ImageButton On, Off, Discnt, Abt;
    Button btnLvl0, btnLvl1, btnLvl2, btnLvl3, btnColorW, btnColorR, btnColorG, btnColorB, btnAuto;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device

        //view of the ledControl
        setContentView(R.layout.activity_led_control);

        //call the widgets
        On = (ImageButton)findViewById(R.id.on);
        Off = (ImageButton)findViewById(R.id.off);
        Discnt = (ImageButton)findViewById(R.id.discnt);
        Abt = (ImageButton)findViewById(R.id.abt);

        On.setVisibility(View.INVISIBLE);
        Off.setVisibility(View.INVISIBLE);
        Abt.setVisibility(View.INVISIBLE);

        btnLvl0 = (Button)findViewById(R.id.btnLvl0);
        btnLvl1 = (Button)findViewById(R.id.btnLvl1);
        btnLvl2 = (Button)findViewById(R.id.btnLvl2);
        btnLvl3 = (Button)findViewById(R.id.btnLvl3);
        btnColorW = (Button)findViewById(R.id.btnColorW);
        btnColorR = (Button)findViewById(R.id.btnColorR);
        btnColorG = (Button)findViewById(R.id.btnColorG);
        btnColorB = (Button)findViewById(R.id.btnColorB);
        btnAuto = (Button)findViewById(R.id.btnAuto);

        btnLvl0.setText(getResources().getString(R.string.powerDown));
        btnLvl1.setText(getResources().getString(R.string.lvl1));
        btnLvl2.setText(getResources().getString(R.string.lvl2));
        btnLvl3.setText(getResources().getString(R.string.lvl3));
        btnColorW.setText(getResources().getString(R.string.white));
        btnColorR.setText(getResources().getString(R.string.red));
        btnColorG.setText(getResources().getString(R.string.green));
        btnColorB.setText(getResources().getString(R.string.blue));
        btnAuto.setText(getResources().getString(R.string.automatic));


        new ConnectBT().execute(); //Call the class to connect

        //commands to be sent to bluetooth
        On.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                turnOnLed();      //method to turn on
            }
        });
        Off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                turnOffLed();   //method to turn off
            }
        });
        Discnt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Disconnect(); //close connection
            }
        });

        btnLvl0.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendCharToSerial("0");
            }
        });
        btnLvl1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendCharToSerial("1");
            }
        });
        btnLvl2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendCharToSerial("2");
            }
        });
        btnLvl3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendCharToSerial("3");
            }
        });
        btnColorW.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendCharToSerial("w");
            }
        });
        btnColorR.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendCharToSerial("r");
            }
        });
        btnColorG.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendCharToSerial("g");
            }
        });
        btnColorB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendCharToSerial("b");
            }
        });
        btnAuto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendCharToSerial("a");
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

    private void SendCharToSerial(String character)
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write(character.getBytes());
            }
            catch (IOException e)
            {
                msg("Error sending -> " + character);
            }
        }
    }

    private void turnOffLed()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("0".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void turnOnLed()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("1".toString().getBytes());
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
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    public  void about(View v)
    {
        if(v.getId() == R.id.abt)
        {
            Intent i = new Intent(this, AboutActivity.class);
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_led_control, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(ledControl.this, "Connecting...", "Please wait!!!");  //show a progress dialog
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
