package com.example.bluetooth_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class bluetoothPairing extends AppCompatActivity {

    final String TAG = "SubActivity";
    ArrayAdapter<String> pairingAdapter, scanAdapter;
    ListView listview,listview_pairing, listview_scan, adapterView;
    ArrayList<String> pairingList, scanList;
    Button bt_cancel, bt_scan;
    BluetoothAdapter myBluetoothAdapter;
    protected static UUID MY_UUID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_pairing);

        bt_cancel = (Button) findViewById(R.id.bt_cancel);
        bt_scan = (Button) findViewById(R.id.bt_scan);
        listview_pairing = (ListView) findViewById(R.id.listview_pairing);
        listview_scan = (ListView) findViewById(R.id.listview_scan);

        // 블루투스 어댑터
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // 페어링 목록
        pairingList = new ArrayList<>();

        // 기기 스캔 목록
        scanList = new ArrayList<>();

        MY_UUID = UUID.randomUUID();
        Log.d(TAG, MY_UUID.toString());

        // 이미 페어링 되어있는 기기 목록 불러오기
        Set<BluetoothDevice> pairedDevices = myBluetoothAdapter.getBondedDevices();

        if(pairedDevices.size() >0){
            for (BluetoothDevice device : pairedDevices) {
                pairingList.add(device.getName() + "\n" + device.getAddress());
            }
        }

        pairingAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.activity_bluetooth_pairing, pairingList); //커스텀 레이아웃 사용
        listview_pairing.setAdapter(pairingAdapter);

        listview_pairing.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                String selectedItem = (String) adapterView.getItemAtPosition(i);
                Toast.makeText(getApplicationContext(), "연결기기 : " + selectedItem, Toast.LENGTH_SHORT).show();
            }
        });

        // 스캔 버튼을 클릭하면 주변 기기를 스캔
        bt_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myBluetoothAdapter.isDiscovering()){
                    myBluetoothAdapter.cancelDiscovery();
                }
                scanList.clear(); // 기존 목록을 클리어함
                myBluetoothAdapter.startDiscovery();
            }
        });

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(myReceiver,intentFilter);

        scanAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.activity_bluetooth_pairing,scanList);

        listview_scan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                String selectedItem = (String) adapterView.getItemAtPosition(i);
                Toast.makeText(getApplicationContext(),"연결기기: " + selectedItem,Toast.LENGTH_SHORT).show();
                // 선택된 기기를 페어링 목록에 추가
            }
        });

        // 액티비티 닫기 버튼
        bt_cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish(); // 액티비티 닫기
            }
        });
    }

    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                scanList.add(device.getName());
                scanAdapter.notifyDataSetChanged();
            }
        }
    };
}