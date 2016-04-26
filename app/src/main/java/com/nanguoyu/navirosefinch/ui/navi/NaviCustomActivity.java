package com.nanguoyu.navirosefinch.ui.navi;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.AMapNaviViewOptions;
import com.nanguoyu.navirosefinch.R;
import com.nanguoyu.navirosefinch.beans.BJCamera;
import com.nanguoyu.navirosefinch.module.TTSController;
import com.nanguoyu.navirosefinch.presenters.MarkerInteractor;
import com.nanguoyu.navirosefinch.presenters.MarkerInteractorImpl;
import com.nanguoyu.navirosefinch.ui.BaseActivity;

import java.util.ArrayList;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

/**
 * 实时导航界面
 * 
 */
public class NaviCustomActivity extends BaseActivity implements
		AMapNaviViewListener , SharedPreferences.OnSharedPreferenceChangeListener{
	//蓝牙
	BluetoothSPP bt= new BluetoothSPP(this);
	private static final String TAG = "BTdata";
	//
	private AMapNaviView mAmapAMapNaviView;
	// 导航监听
	private AMapNaviListener mAmapNaviListener;
	private AMap mAmap;

    private SharedPreferences mPref;

    private ArrayList<Marker> markers;
    private ArrayList<BJCamera> mBJCameras;

    private BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_camera_location);

    TTSController tts = new TTSController(this);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navicustom);

        mPref = getSharedPreferences(getPackageName() + "_preferences", Context.MODE_PRIVATE);
        mPref.registerOnSharedPreferenceChangeListener(this);


		//语音播报开始
		TTSController.getInstance(this).startSpeaking();
		// 实时导航方式进行导航
		AMapNavi.getInstance(this).startNavi(AMapNavi.GPSNaviMode);
		
		initView(savedInstanceState);
		mAmap = mAmapAMapNaviView.getMap();

		MarkerInteractor markerInteractor = new MarkerInteractorImpl();
		markerInteractor.readCameras(new MarkerInteractor.OnReadCamerasListener() {

            @Override
            public void onReadCameras(ArrayList<BJCamera> cameraBeans) {
                mBJCameras = cameraBeans;

                if (NaviSetting.getBeijingCamera()){
                    addCameraMarkers(mBJCameras);
                }
            }

        });

		if(!bt.isBluetoothAvailable()) {
			Toast.makeText(getApplicationContext()
					, "蓝牙不可用"
					, Toast.LENGTH_SHORT).show();
			finish();
		}

		bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
			public void onDeviceDisconnected() {
			}

			public void onDeviceConnectionFailed() {
				TTSController.getInstance(getApplicationContext()).playText("连接失败，请重新尝试");
			}

			public void onDeviceConnected(String name, String address) {
				TTSController.getInstance(getApplicationContext()).playText("连接成功");
                TTSController.getInstance(getApplication()).playText("请将手机插入朱雀导航棒固定槽，手持导航棒，请将当前闪烁振动的一端指向您的前进方向");
                SendMessage();
                bt.send(tts.DIRECTSTRING,true);
			}
		});
		onConnectClickListener();
	}

    public void onStart(){
	super.onStart();
	if (!bt.isBluetoothEnabled()) {
		Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
	} else {
		if(!bt.isServiceAvailable()) {
			bt.setupService();
			bt.startService(BluetoothState.DEVICE_ANDROID);

		}
	}
}

	public void onConnectClickListener(){

		final	Button btnlink = (Button)findViewById(R.id.button_link);
		btnlink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				bt.setDeviceTarget(BluetoothState.DEVICE_OTHER);
				Intent intent = new Intent(getApplicationContext(), DeviceList.class);
				startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
				if (bt.getServiceState() == BluetoothState.STATE_CONNECTED)
					btnlink.setText("连接中");
				if (bt.getServiceState() == BluetoothState.STATE_CONNECTED)
					bt.disconnect();
			}
		});
	}


	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
			if(resultCode == Activity.RESULT_OK)
				bt.connect(data);
		} else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
			if(resultCode == Activity.RESULT_OK) {
				bt.setupService();
				bt.startService(BluetoothState.DEVICE_ANDROID);

			} else {
				Toast.makeText(getApplicationContext()
						, "蓝牙未开启."
						, Toast.LENGTH_SHORT).show();
				TTSController.getInstance(getApplicationContext()).playText("蓝牙未开启");
				finish();
			}
		}
	}

    public void addCameraMarkers(ArrayList<BJCamera> cameraBeans) {
        ArrayList<MarkerOptions> markerOptionses = new ArrayList<>();
        for (BJCamera cameraBean : cameraBeans) {
            LatLng latLng = new LatLng(cameraBean.getLatitude(), cameraBean.getLongtitude());
            MarkerOptions mo = new MarkerOptions().position(latLng).draggable(true).icon(icon);
            markerOptionses.add(mo);
        }

        markers = mAmap.addMarkers(markerOptionses, false);
    }

	private void initView(Bundle savedInstanceState) {
		mAmapAMapNaviView = (AMapNaviView) findViewById(R.id.customnavimap);
		mAmapAMapNaviView.onCreate(savedInstanceState);
		mAmapAMapNaviView.setAMapNaviViewListener(this);
		setAmapNaviViewOptions();
	}

	/**
	 * 设置导航的参数
	 */
	private void setAmapNaviViewOptions() {
		if (mAmapAMapNaviView == null) {
			return;
		}
		AMapNaviViewOptions viewOptions = new AMapNaviViewOptions();
		viewOptions.setSettingMenuEnabled(true);// 设置导航setting可用
		viewOptions.setNaviNight(NaviSetting.getNaviNight());// 设置导航是否为黑夜模式
		viewOptions.setReCalculateRouteForYaw(NaviSetting.getReCalculateRouteForYaw());// 设置导偏航是否重算
		viewOptions.setReCalculateRouteForTrafficJam(NaviSetting.getReCalculateRouteForTrafficJam());// 设置交通拥挤是否重算
		viewOptions.setTrafficInfoUpdateEnabled(NaviSetting.getTrafficInfoUpdateEnabled());// 设置是否更新路况
		viewOptions.setCameraInfoUpdateEnabled(NaviSetting.getCameraInfoUpdateEnabled());// 设置摄像头播报
		viewOptions.setScreenAlwaysBright(NaviSetting.getScreenAlwaysBright());// 设置屏幕常亮情况
		viewOptions.setNaviViewTopic(AMapNaviViewOptions.BLUE_COLOR_TOPIC);// 设置导航界面主题样式

		mAmapAMapNaviView.setViewOptions(viewOptions);

	}

	private AMapNaviListener getAMapNaviListener() {
		if (mAmapNaviListener == null) {
			mAmapNaviListener = TTSController.getInstance(getApplicationContext());
		}
		return mAmapNaviListener;
	}

	// -------处理
	/**
	 * 导航界面返回按钮监听
	 * */
	@Override
	public void onNaviCancel() {
		finish();
	}

	@Override
	public boolean onNaviBackClick() {
		return false;
	}

	/**
	 * 点击设置按钮的事件
	 */
	@Override
	public void onNaviSetting() {
		Intent intent = new Intent(this,NaviSettingActivity.class);
		startActivity(intent);

	}

	@Override
	public void onNaviMapMode(int arg0) {

	}
	@Override
	public void onNaviTurnClick() {
		
		
	}

	@Override
	public void onNextRoadClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScanViewButtonClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent);
	}

	// ------------------------------生命周期方法---------------------------
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mAmapAMapNaviView.onSaveInstanceState(outState);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setAmapNaviViewOptions();

		AMapNavi.getInstance(this).setAMapNaviListener(getAMapNaviListener());
		mAmapAMapNaviView.onResume();

	}

	@Override
	public void onPause() {
		mAmapAMapNaviView.onPause();
		super.onPause();
		AMapNavi.getInstance(this)
				.removeAMapNaviListener(getAMapNaviListener());

	}

	@Override
	public void onDestroy() {

		super.onDestroy();

        mPref.unregisterOnSharedPreferenceChangeListener(this);

		mAmapAMapNaviView.onDestroy();
	 	//页面结束时，停止语音播报
		TTSController.getInstance(this).stopSpeaking();
		bt.stopService();
	}

	@Override
	public void onLockMap(boolean arg0) {
		  
		// TODO Auto-generated method stub  
		
	}


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(NaviSetting.SETTING_PREF_NAVI_BEIJNG_CAMERA)){
            if (NaviSetting.getBeijingCamera()){
                addCameraMarkers(mBJCameras);
            } else{
                if (markers != null ){
                    for (Marker marker : markers){
                        marker.remove();
                        marker.destroy();
                        marker = null;
                    }
                }
            }
        }
    }


	public void SendMessage(){
        String Direction="26.74";
        bt.send(Direction, true);
    }



}
