package com.qixiang.blebese;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private final static int REQUEST_ENABLE_BT = 2001;
    private BluetoothDevice theDevice;
    private List<String> fdArrayList = new ArrayList<String>();
    private boolean connected_flag;
    private boolean exit_activity = false;
    public String tmp, hex;

    private BLEService.OnWriteOverCallback mOnWriteOverCallback = new BLEService.OnWriteOverCallback() {

        @Override
        public void OnWriteOver(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int statue) {

            if (statue == BluetoothGatt.GATT_SUCCESS) {
                Log.e(Utils.TAG, "OnWriteOver..............");
                if (fdArrayList.size() != 0) {
                    fdArrayList.remove(0);
//                    if(fdArrayList.size()!= 0){
//                        spliteArray =fdArrayList.get(0).split(",");
//                        if (spliteArray[0].equals("#")) {//常规指令
//                            //SendDataString(spliteArray[1]);
//                            Tools.mBleService.characterWrite1.setValue(hexToBytes(spliteArray[1]));
//                            Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
//                        }else {//速度指令
//                            //SendControllerSpeed(spliteArray[1]);
//                            Tools.mBleService.characterWrite2.setValue(hexToBytes(spliteArray[1]));
//                            Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite2);
//                        }
//                    }
                }
            } else if (statue == BluetoothGatt.GATT_FAILURE) {
                myHandler.sendEmptyMessage(8);
//                if (fdArrayList.size()!= 0){
//                    spliteArray =fdArrayList.get(0).split(",");
//                    if (spliteArray[0].equals("#")) {//常规指令
//                        //SendDataString(spliteArray[1]);
//                        Tools.mBleService.characterWrite1.setValue(hexToBytes(spliteArray[1]));
//                        Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
//                    }else {//速度指令
//                        //SendControllerSpeed(spliteArray[1]);
//                        Tools.mBleService.characterWrite2.setValue(hexToBytes(spliteArray[1]));
//                        Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite2);
//                    }
//                }
            }
        }

    };

    public static String byteArrayToStr(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        String str = new String(byteArray);
        return str;
    }


    String data = "";

    int ppCount = 0;
    boolean conFlag = false;
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

            if (conFlag) {
                return;
            }
            if (null != device && null != device.getName()) {
                if (theDevice == null) {
                    if (device.getName().equals("qixiang_TS")) {
                        conFlag = true;
                        theDevice = device;
                        myHandler.sendEmptyMessage(13);
                    }
                }
            }
        }
    };

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder service) {
            // TODO Auto-generated method stub

            BLEService.LocalBinder binder = (BLEService.LocalBinder) service;
            Tools.mBleService = binder.getService();
            if (Tools.mBleService.initBle()) {
                if (!Tools.mBleService.mBluetoothAdapter.isEnabled()) {
                    final Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } else {
                    myHandler.sendEmptyMessage(11);
                    //scanBle();
                }
            }
        }
    };

    int numcount = 0;
    private Handler myHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            //Tools.setLog("log1", "myHandler.........."+msg.what);
            switch (msg.what) {
                case 111:
                    //Toast.makeText(MainActivity.this, "startActivityForResult..", Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(MainActivity.this,VideoActivity.class);
                    //startActivityForResult(intent, 12);
                    break;
                case 110:
                    Toast.makeText(MainActivity.this, "110............", Toast.LENGTH_SHORT).show();
                    break;
                case 112:
                    Toast.makeText(MainActivity.this, "112.............", Toast.LENGTH_SHORT).show();
                    break;
                case 11:
                    if (Tools.mBleService != null)
                        Tools.mBleService.scanBle(mLeScanCallback);
                    break;
                case 1313:
                    numcount++;
                    thetr.setText(data + "  共" + numcount + "次");
                    Toast.makeText(MainActivity.this, "连接成功:" + data + " ", Toast.LENGTH_LONG).show();
                    break;
                case 1314:
                    ppCount++;
                    thetr.setText("sleep100ms 共:" + ppCount + "次");
                    //Toast.makeText(MainActivity.this, "连接成功:"+data+" ", Toast.LENGTH_LONG).show();
                    break;
                case 1315:
                    ppCount++;
                    thetr.setText("Setting 100ms 共:" + ppCount + "次");
                    //Toast.makeText(MainActivity.this, "连接成功:"+data+" ", Toast.LENGTH_LONG).show();
                    break;
                case 12:
                    if (Tools.mBleService != null) {
                        Tools.mBleService.stopscanBle(mLeScanCallback);
                        Tools.mBleService.setOnWriteOverCallback(mOnWriteOverCallback);
                    }
                    Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_LONG).show();
                    Intent intent2 = new Intent("com.qixiang.blesuccess");
                    sendBroadcast(intent2);

                    break;
                case 122:
                    if (Tools.mBleService != null) {
                        Tools.mBleService.stopscanBle(mLeScanCallback);
                    }
                    break;
                case 13:
                    new MyConnectedThread().start();
                    break;
                case 14:
                    Bundle bundle = msg.getData();
                    String string = (String) bundle.get("Key");
                    Tools.setLog("log1", "msg.what 14:" + string);
                    Tools.mBleService.characterWrite1.setValue(hexToBytes(string));
                    Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
                    break;
                default:
                    break;
            }
        }

        ;
    };

    private SendBle mSendBle;
    TextView thetr;
    private TextView tv_logo;
    ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        thetr = (TextView) findViewById(R.id.thrtittle);

        setBroadcastReveiver();
        checkBluetoothPermission();
        //bindService(new Intent(this, BLEService.class), connection, Context.BIND_AUTO_CREATE);

        mSendBle = new SendBle(this);
        initClick();
        tv_logo = findViewById(R.id.tv_logo);
    }

    EditText edInput;
    private void initClick() {
        findViewById(R.id.btn_stay_shock).setOnClickListener(this);
        findViewById(R.id.btn_close_ble).setOnClickListener(this);
        findViewById(R.id.btn_go_jump).setOnClickListener(this);
        findViewById(R.id.btn_get_allcount).setOnClickListener(this);
        findViewById(R.id.btn_getflash_data).setOnClickListener(this);
        findViewById(R.id.btn_getflash_count).setOnClickListener(this);
        findViewById(R.id.btn_recount).setOnClickListener(this);
        findViewById(R.id.btn_get_countdata).setOnClickListener(this);
        findViewById(R.id.btn_clear_logo).setOnClickListener(this);
        findViewById(R.id.btn_one_min).setOnClickListener(this);
        findViewById(R.id.btn_count).setOnClickListener(this);

        edInput = (EditText) findViewById(R.id.et_setcount);
    }

    public void backtomain(View view) {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
        //MainActivity.this.finish();
    }

    private void InitBle() {
        BluetoothManager bManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bAdapter = bManager.getAdapter();
        if (bAdapter == null) {
            Toast.makeText(MainActivity.this, "not support", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (bAdapter.isEnabled()) {
            bindService(new Intent(this, BLEService.class), connection, Context.BIND_AUTO_CREATE);
        } else {
            bAdapter.enable();
        }

    }

    ArrayList<String> unPermissionList;

    private void checkBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermission = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN
            };
            unPermissionList = new ArrayList<String>();
            try {
                for (int i = 0; i < mPermission.length; i++) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, mPermission[i]) != PackageManager.PERMISSION_GRANTED) {
                        unPermissionList.add(mPermission[i]);
                    }
                }
                for (int i = 0; i < mPermission.length; i++) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, "android.permission.ACCESS_COARSE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
                        if (isLocationEnabled()) {

                        } else {
                            Toast.makeText(MainActivity.this, "Please open the location switch.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 0);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Exception0:" + e.toString(), Toast.LENGTH_LONG).show();
            }

        } else {
            InitBle();
            Toast.makeText(MainActivity.this, "Init.............:", Toast.LENGTH_LONG).show();
            return;
        }
        if (unPermissionList.isEmpty()) {
            if (isLocationEnabled())
                InitBle();
            //Toast.makeText(MainActivity.this, "Init.............:", Toast.LENGTH_LONG).show();
            //都授权了。。。
        } else {

            try {
                String[] permissionStrings = unPermissionList.toArray(new String[unPermissionList.size()]);
                ActivityCompat.requestPermissions(this, permissionStrings, 1);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Exception11:" + e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 判断定位服务是否开启
     *
     * @param
     * @return true 表示开启
     */

    public boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Toast.makeText(TestAct.this, "requestCode:"+requestCode, Toast.LENGTH_LONG).show();
        for (int i = 0; i < grantResults.length; i++) {
            //Toast.makeText(ScanActivity.this, permissions[i]+"     "+grantResults[i], Toast.LENGTH_LONG).show();

            if (permissions[i].equals("android.permission.ACCESS_COARSE_LOCATION")) {
                if ((grantResults[i] == 0)) {
                    if (!isLocationEnabled()) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, 0);
                    } else {
                        InitBle();
                        Toast.makeText(MainActivity.this, "Init.............:", Toast.LENGTH_LONG).show();
                    }
                } else {
                    finish();
                }

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (isLocationEnabled()) {
                checkBluetoothPermission();
            } else {
                Toast.makeText(MainActivity.this, "Please open the location switch.", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, 0);
                    }
                }, 1000);
            }
        }
    }

    //设置广播接收器
    private void setBroadcastReveiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BLEService.ACTION_STATE_CONNECTED);
        intentFilter.addAction(BLEService.ACTION_STATE_DISCONNECTED);
        intentFilter.addAction(BLEService.ACTION_WRITE_DESCRIPTOR_OVER);
        intentFilter.addAction(BLEService.ACTION_CHARACTER_CHANGE);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        bluetoothReceiver = new BluetoothReceiver();
        registerReceiver(bluetoothReceiver, intentFilter);
    }

    private BluetoothReceiver bluetoothReceiver = null;

    public class BluetoothReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();

            if (BLEService.ACTION_CHARACTER_CHANGE.equals(action)) {

                Log.e(Utils.TAG, "通知数据。。。。。。。。。。。。。。");
                //tmp_byte = characteristic.getValue();

                switchHead(intent.getByteArrayExtra("value"));
//                tmp = "";
//                for (int i = 0; i < tmp_byte.length; i++) {
//                    hex = Integer.toHexString(tmp_byte[i] & 0xFF);
//                    if (hex.length() == 1) {
//                        hex = '0' + hex;
//                    }
//                    tmp = tmp + hex;
//                }
            } else if (BLEService.ACTION_STATE_CONNECTED.equals(action)) {

            } else if (BLEService.ACTION_STATE_DISCONNECTED.equals(action)) {
                connected_flag = false;
                myHandler.sendEmptyMessage(11);
            } else if (BLEService.ACTION_WRITE_DESCRIPTOR_OVER.equals(action)) {
                connected_flag = true;
                theDevice = null;
                myHandler.sendEmptyMessage(12);
            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                switch (blueState) {
                    case BluetoothAdapter.STATE_ON:
                        Log.e(Utils.TAG, "BluetoothAdapter.STATE_ON..............蓝牙已打开");
                        Toast.makeText(MainActivity.this, "蓝牙已打开", Toast.LENGTH_LONG).show();
                        //开始扫描
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               bindService(new Intent(MainActivity.this, BLEService.class), connection, Context.BIND_AUTO_CREATE);
                           }
                       });
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.e(Utils.TAG, "BluetoothAdapter.STATE_TURNING_ON..............蓝牙正在打开");
                        //开始扫描
                        break;
                }

            }
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        Log.e(Utils.TAG, "onDestroy..............");
        exit_activity = true;
        unbindService(connection);
    }

    public byte[] hexToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] bytes = new byte[length];
        String hexDigits = "0123456789ABCDEF";
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            int h = hexDigits.indexOf(hexChars[pos]) << 4;
            int l = hexDigits.indexOf(hexChars[pos + 1]);
            if (h == -1 || l == -1) {
                return null;
            }
            bytes[i] = (byte) (h | l);
        }
        return bytes;
    }

    public class MyConnectedThread extends Thread {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();

            try {
                while (true) {
                    connected_flag = false;
                    if (exit_activity) return;
                    Tools.setLog("log1", "connectBle..........");
                    Tools.mBleService.connectBle(theDevice);

                    for (int j = 0; j < 50; j++) {

                        if (connected_flag) {
                            break;
                        }
                        sleep(100);
                    }

                    if (connected_flag)
                        break;
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    private byte[] command_ = new byte[16];

    /**
     * 初始化操作指令
     */
    private void initData() {

    }

    int i = 0;

    @Override
    public void onClick(View v) {
        if (null == Tools.mBleService.characterWrite1) {
            Toast.makeText(MainActivity.this, "null == characterWrite1", Toast.LENGTH_LONG).show();
            return;
        }
        switch (v.getId()) {
//            case R.id.btn_ble_jump:
//                if (i%2 == 0){
//                    byte[] oo = {0x02};
//                    Tools.mBleService.characterWrite1.setValue(oo);
//                    Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
//                }else {
//                    Tools.mBleService.mBluetoothGatt.readCharacteristic(Tools.mBleService.characterWrite1);
//                    byte[] gg = Tools.mBleService.characterWrite1.getValue();
//                    int j=2;
//                }
//                i++;
//                break;
            case R.id.btn_startAd:
                //initData();
                //boolean flag = mSendBle.d(sendstr, 3,myHandler);
                break;
            case R.id.btn_close_ble:
                Tools.mBleService.characterWrite1.setValue(new byte[]{Constant.SHUT_DOWN});
                Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
                setLogo("关闭ble");
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_stay_shock:
                Tools.mBleService.characterWrite1.setValue(new byte[]{Constant.SET_MOTOR_LONG});
                Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
                setLogo("长振");
                break;
            case R.id.btn_go_jump:
                if (edInput.getText().toString().length() == 0) {
                    Toast.makeText(MainActivity.this, "输入为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Utils.isNumeric(edInput.getText().toString())) {
                    Toast.makeText(MainActivity.this, "非数字输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Integer.valueOf(edInput.getText().toString()) > 10000) {
                    Toast.makeText(MainActivity.this, "数值太大", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    byte[] d = Utils.intToButeArray(Integer.valueOf(edInput.getText().toString()));
                    Tools.mBleService.characterWrite1.setValue(new byte[]{Constant.SET_SKIP_COUNT, d[0], d[1]});
                    Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
                    setLogo("走，去跳！");
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "输入异常", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_get_allcount:
                Tools.mBleService.characterWrite1.setValue(new byte[]{Constant.GET_TOTLE_COUNT});
                Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
                setLogo("获取总条数！");
                break;
            case R.id.btn_getflash_data:

                Tools.mBleService.characterWrite1.setValue(new byte[]{Constant.GET_BUFF_DATA});
                Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
                setLogo("获取Flash中数据！");
                byte[] readData = readData();
                switchFlashData(readData);
                //String readDataString = Utils.byteArrayToHexStr(readData);

                break;
            case R.id.btn_getflash_count:
                if (null == Tools.mBleService.characterWrite1) {
                    Toast.makeText(MainActivity.this, "null == characterWrite1", Toast.LENGTH_LONG).show();
                    return;
                }
                Tools.mBleService.characterWrite1.setValue(new byte[]{Constant.GET_BUFF_SIZE});
                Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
                setLogo("获取Flash中数据条数！");
                break;
            case R.id.btn_recount:
                Tools.mBleService.characterWrite1.setValue(new byte[]{Constant.CLEAR_HOLZER});
                Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
                setLogo("底层从新计算！");
                break;
            case R.id.btn_get_countdata:
                Tools.mBleService.characterWrite1.setValue(new byte[]{Constant.GET_COUNT_VALUE});
                Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
                try {
                    sleep(300);
                } catch (Exception e) {
                    e.toString();
                }
                Tools.mBleService.mBluetoothGatt.readCharacteristic(Tools.mBleService.characterRead);
                try {
                    sleep(300);
                } catch (Exception e) {
                    e.toString();
                }
                byte[] jk = new byte[16];
                for (int i = 0; i < 16; i++) {
                    jk[i] = Tools.mBleService.characterRead.getValue()[i];
                }
                //byte[] jk = Tools.mBleService.characterWrite1.getValue();
                // String data = Utils.byteArrayToHexStr(jk);
                setLogo("获取计数条数值！");
                break;
            case R.id.btn_clear_logo:
                reaminString = "";
                tv_logo.setText("");
                break;
//            case R.id.btn_free_jump:
//                Tools.mBleService.characterWrite1.setValue(new byte[]{0x01});
//                Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
//                break;
            case R.id.btn_one_min:
                Tools.mBleService.characterWrite1.setValue(new byte[]{Constant.START_1MIN});
                Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
                setLogo("一分钟跳！");
                break;
            case R.id.btn_count:
                Tools.mBleService.characterWrite1.setValue(new byte[]{Constant.START_COUNT});
                Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
                setLogo("记次跳！");
                break;
        }
    }

    //解析各个数据包头
    private void switchHead(byte[] dataArray) {
        String dataString = Utils.byteArrayToHexStr(dataArray);
        if (dataString.length() < 12)
            return;
        dataString = dataString.substring(8, 12);
        switch (dataString) {
            case "fC00"://flash读空
                setLogo("flash读空");
                break;

            case "0400"://flash中读到的1min跳数据
                setLogo("flash中读到的1min跳数据");
                break;
            case "0800"://flash中读到的自由跳数据
                setLogo("flash中读到的自由跳数据");
                break;
            case "0C00"://flash中读到的计数跳数据
                setLogo("flash中读到的计数跳数据");
                break;
            case "1000"://1min跳实时数据通知
                int count3 = Utils.byteArrayToInt(new byte[]{0, 0, dataArray[6], dataArray[7]});
                setLogo("1min跳实时数据通知:" + count3);
                Log.e(Utils.TAG, "1min跳模式。。。。。。。。。。。。。。" + count3);
                break;

            case "1400"://自由跳实时数据通知
                int count = Utils.byteArrayToInt(new byte[]{0, 0, dataArray[6], dataArray[7]});
                setLogo("自由跳实时数据通知:" + count);
                Log.e(Utils.TAG, "自由跳实时数据。。。。。。。。。。。。。。" + count);
//                uint16_t bleDataFreeTotalSkip =(dataArray[6]<<8)+(dataArray[7]);//跳的次数
//                uint16_t bleDataFreeSkip_Previous_Time =(dataArray[8]<<8)+(dataArray[9]);//时间毫秒
//                uint16_t bleDataFreeSkip_Current_Time =(dataArray[10]<<8)+(dataArray[11]);//时间毫秒0
                break;
            case "1800"://计数跳实时数据通知
                int count2 = Utils.byteArrayToInt(new byte[]{0, 0, dataArray[6], dataArray[7]});
                setLogo("计数跳实时数据通知:" + count2);
                Log.e(Utils.TAG, "计数跳实时数据。。。。。。。。。。。。。。" + count2);
                break;
            case "1C00"://1min跳模式开启通知
                setLogo("1min跳模式开启通知");
                break;

            case "2000"://自由跳模式开启通知(此条无用)
                setLogo("自由跳模式开启通知");
                break;
            case "2400"://计数跳模式开启通知
                setLogo("计数跳模式开启通知");
                break;
            case "2800"://总跳数数据获取
                setLogo("总跳数数据获取");
                break;

            case "2C00"://flash数据条数的个数获取
                setLogo("flash数据条数的个数获取");
                break;
            case "3000"://计数跳的计数值获取
                setLogo("计数跳的计数值获取");
                break;
            case "3400"://1min跳结束通知
                setLogo("1min跳结束通知");
                break;

            case "3800"://计数跳结束通知
                setLogo("计数跳结束通知");
                break;
            default:
                setLogo("未匹配");
        }
    }

    private byte[] readData() {
        try {
            sleep(500);
        } catch (Exception e) {
            e.toString();
        }
        Tools.mBleService.mBluetoothGatt.readCharacteristic(Tools.mBleService.characterRead);
        try {
            sleep(600);
        } catch (Exception e) {
            e.toString();
        }
        byte[] jk = new byte[Tools.mBleService.characterRead.getValue().length];
        for (int i = 0; i < Tools.mBleService.characterRead.getValue().length; i++) {
            jk[i] = Tools.mBleService.characterRead.getValue()[i];
        }
        return jk;
    }

    private String reaminString = "";

    private void setLogo(String s) {
        tv_logo.setText(reaminString + s + "\n");
        reaminString = reaminString + s + "\n";
    }

    private void switchFlashData(byte[] data) {
        String d = Utils.byteArrayToHexStr(data);
        if (d.length() < 12) {
            Toast.makeText(MainActivity.this, "switchFlashData解析异常", Toast.LENGTH_SHORT).show();
            return;
        }
        d = d.substring(8, 12);
        int ui = Utils.byteArrayToInt(new byte[]{0, 0, data[4], data[5]}) & Utils.byteArrayToInt(new byte[]{0, 0, (byte) 0xFC, 00});
        if (ui == 1024) {
            setLogo("flash中读到一分钟跳数据");
        } else if ("0800".equals(d)) {
            setLogo("flash中读到的自由跳数据");
        } else if ("0C00".equals(d)) {
            setLogo("flash中读到的计数跳数据");
        } else if ("FC00".equals(d)) {
            setLogo("flash中无数据");
        } else {
            setLogo("flash中读到异常数据");
        }

    }
}
