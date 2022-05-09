package com.example.bluetooth_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final String TAG = "MainActivity";
    int REQUEST_ENABLE_BT = 1;
    Button bt_on, bt_off, bt_pairing;
    BluetoothAdapter myBluetoothAdapter;
    Intent btEnableIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_on = (Button) findViewById(R.id.bt_bluetooth_on);
        bt_off = (Button) findViewById(R.id.bt_bluetooth_off);
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        bluetoothOnMethod();
        bluetoothOffMethod();
    }

    private void bluetoothOffMethod() {
        bt_off.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(myBluetoothAdapter.isEnabled()){
                    myBluetoothAdapter.disable();
                    Toast.makeText(getApplicationContext(), "블루투스가 비활성화됩니다.",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void bluetoothOnMethod() {
        bt_on.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(myBluetoothAdapter == null){
                    Toast.makeText(getApplicationContext(),"블루투스를 지원하지 않는 기기입니다.",Toast.LENGTH_SHORT).show();

                } else{
                    if(!myBluetoothAdapter.isEnabled()){
                        btEnableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(btEnableIntent,REQUEST_ENABLE_BT);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "블루투스가 활성화 되었습니다.", Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "블루투스가 활성화 되지 않았습니다.", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void bluetoothPairing() {
        bt_pairing.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(myBluetoothAdapter.isEnabled()) {
                    Intent pairingIntent = new Intent(MainActivity.this,bluetoothPairing.class);
                    startActiviry(pairingIntent);
                } else {
                    Toast.makeText(getApplicationContext(),"먼저 블루투스를 활성화 하세요.",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void startActiviry(Intent pairingIntent) {
    }

}