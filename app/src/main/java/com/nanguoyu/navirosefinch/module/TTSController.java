package com.nanguoyu.navirosefinch.module;

import android.content.Context;
import android.os.Bundle;

import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.NaviInfo;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechListener;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.nanguoyu.navirosefinch.R;

/**
 * 语音播报组件
 *
 */
public class TTSController implements SynthesizerListener, AMapNaviListener {

	public final static String[] strActions = { "无", "自车", "左转", "右转", "左前方行驶",
			"右前方行驶", "左后方行驶", "右后方行驶", "左转掉头", "直行", "到达途经点", "进入环岛", "驶出环岛",
			"到达服务区", "到达收费站", "到达目的地", "进入隧道", "靠左", "靠右", "通过人行横道", "通过过街天桥",
			"通过地下通道", "通过广场", "到道路斜对面" };
    public final static String[] strDrections = { "26","37","48","59"};
    public int direction;
	public static TTSController ttsManager;
	private Context mContext;
    public int direct;
    public String DIRECTSTRING="26.68";

	// 合成对象.
	private SpeechSynthesizer mSpeechSynthesizer;

	public TTSController(Context context) {
		mContext = context;
	}

	public static TTSController getInstance(Context context) {
		if (ttsManager == null) {
			ttsManager = new TTSController(context);
		}
		return ttsManager;
	}



	public void init() {
//		SpeechUser.getUser().login(mContext, null, null,
//				"appid=" + mContext.getString(R.string.app_id), listener);
		SpeechUtility.createUtility(mContext, SpeechConstant.APPID + "=56dc481f");

		// 初始化合成对象.
		mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(mContext, null);
		initSpeechSynthesizer();
	}

	/**
	 * 使用SpeechSynthesizer合成语音，不弹出合成Dialog.
	 * 
	 * @param
	 */
	public void playText(String playText) {
		if (!isfinish) {
			return;
		}
		if (null == mSpeechSynthesizer) {
			// 创建合成对象.
			mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(mContext, null);
			initSpeechSynthesizer();
		}
		// 进行语音合成.
		mSpeechSynthesizer.startSpeaking(playText, this);

	}

	public void stopSpeaking() {
		if (mSpeechSynthesizer != null)
			mSpeechSynthesizer.stopSpeaking();
	}
	public void startSpeaking() {
		 isfinish=true;
	}

	private void initSpeechSynthesizer() {
		// 设置发音人
		mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME,
				mContext.getString(R.string.preference_default_tts_role));
		// 设置语速
		mSpeechSynthesizer.setParameter(SpeechConstant.SPEED,
				"" + mContext.getString(R.string.preference_key_tts_speed));
		// 设置音量
		mSpeechSynthesizer.setParameter(SpeechConstant.VOLUME,
				"" + mContext.getString(R.string.preference_key_tts_volume));
		// 设置语调
		mSpeechSynthesizer.setParameter(SpeechConstant.PITCH,
				"" + mContext.getString(R.string.preference_key_tts_pitch));

	}

	/**
	 * 用户登录回调监听器.
	 */
	private SpeechListener listener = new SpeechListener() {

		@Override
		public void onCompleted(SpeechError error) {
			if (error != null) {

			}
		}

		@Override
		public void onEvent(int arg0, Bundle arg1) {
		}

		@Override
		public void onBufferReceived(byte[] bytes) {

		}
	};

	@Override
	public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEvent(int i, int i1, int i2, Bundle bundle) {

	}

	@Override
	public void onSpeakBegin() {
		// TODO Auto-generated method stub
		isfinish = false;

	}

	@Override
	public void onSpeakPaused() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeakProgress(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	boolean isfinish = true;
	@Override
	public void onCompleted(SpeechError speechError) {
		isfinish = true;
	}

	@Override
	public void onSpeakResumed() {
		// TODO Auto-generated method stub

	}

	public void destroy() {
		if (mSpeechSynthesizer != null) {
			mSpeechSynthesizer.stopSpeaking();
		}
	}

	@Override
	public void onArriveDestination() {
		// TODO Auto-generated method stub
		this.playText("到达目的地");
	}

	@Override
	public void onArrivedWayPoint(int arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onCalculateRouteFailure(int arg0) {
		 this.playText("路径计算失败，请检查网络或输入参数");
	}

	@Override
	public void onCalculateRouteSuccess() {
		String calculateResult = "路径计算就绪";

		this.playText(calculateResult);
	}

	@Override
	public void onEndEmulatorNavi() {
		this.playText("导航结束");

	}

	@Override
	public void onGetNavigationText(int arg0, String arg1) {
		// TODO Auto-generated method stub
		this.playText(arg1);
	}

	@Override
	public void onInitNaviFailure() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitNaviSuccess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChange(AMapNaviLocation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReCalculateRouteForTrafficJam() {
		// TODO Auto-generated method stub
		this.playText("前方路线拥堵，路线重新规划");
	}

	@Override
	public void onReCalculateRouteForYaw() {

		this.playText("您已偏航");
	}

	@Override
	public void onStartNavi(int arg0) {
		// TODO Auto-generated method stub
        this.playText("导航开始，请连接朱雀导航仪，请点击导航示图左侧“连接设备”按钮");
	}

	@Override
	public void onTrafficStatusUpdate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGpsOpenStatus(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviInfoUpdated(AMapNaviInfo arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onNaviInfoUpdate(NaviInfo naviInfo) {

        // TODO Auto-generated method stub
        StringBuffer sbf = new StringBuffer();
        StringBuffer sbluef = new StringBuffer();
        //
        if (naviInfo == null) {
            return;
        }
        sbf.append(" 开发者提示,当前方向引导：");
        sbf.append("沿当前路线行进");
        int RetainDistance = naviInfo.getCurStepRetainDistance();
        sbf.append(String.valueOf(RetainDistance));
        sbf.append(strActions[naviInfo.m_Icon]);
        this.playText(sbf.toString());
        direct = naviInfo.m_Icon;
        switch (direct) {
            case 2:DIRECTSTRING="37.68";
            case 3:DIRECTSTRING="59.68";
                default:DIRECTSTRING="26.45";
        }
                    //
                    sbluef.append("当前自车方向：");

        direction = (naviInfo.getDirection()) / 90;

        if ((direction <= 1) && (direction > 0)) {
            sbluef.append(strDrections[direction]);
        } else if ((direction <= 2) && (direction > 1)) {
            sbluef.append(strDrections[direction]);
        } else if ((direction <= 3) && (direction > 2)) {
            sbluef.append(strDrections[direction]);
        } else
            sbluef.append(strDrections[direction]);
        //sbluef.append(squen.substring(1,3));
        sbluef.append(".23");
        this.playText(sbluef.toString());
}

}


