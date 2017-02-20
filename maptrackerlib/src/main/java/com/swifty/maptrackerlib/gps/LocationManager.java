package com.swifty.maptrackerlib.gps;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.TtsMode;
import com.google.gson.Gson;
import com.swifty.maptrackerlib.SharedPrefsUtil;

import static com.baidu.tts.client.SpeechSynthesizer.AUDIO_BITRATE_AMR_15K85;
import static com.baidu.tts.client.SpeechSynthesizer.AUDIO_ENCODE_AMR;
import static com.baidu.tts.client.SpeechSynthesizer.MIX_MODE_DEFAULT;

/**
 * Created by swifty on 15/1/2017.
 */
public class LocationManager {
    private static final String LICENSE_FILE_FULL_PATH_NAME = "";
    private static final String SPEECH_MODEL_FILE_FULL_PATH_NAME = "";
    private static final String TEXT_MODEL_FILE_FULL_PATH_NAME = "";
    private static LocationManager ourInstance;
    private final BDLocationListener myListener = new MyLocationListener();
    private BDLocation currentLocation;
    private LocationListener locationListener;
    private LocationClient mLocationClient;
    private SpeechSynthesizer speechSynthesizer;

    private LocationManager(Context applicationContext) {
        mLocationClient = new LocationClient(applicationContext);     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        String current = SharedPrefsUtil.getInstance().getString("currentLocation");
        currentLocation = new Gson().fromJson(current, BDLocation.class);
        initLocation();
        initVoice(applicationContext);
    }

    private void initVoice(Context applicationContext) {
        speechSynthesizer = SpeechSynthesizer.getInstance();
        speechSynthesizer.setContext(applicationContext);
        speechSynthesizer.setSpeechSynthesizerListener(new com.baidu.tts.client.SpeechSynthesizerListener() {
            @Override
            public void onSynthesizeStart(String s) {
                
            }

            @Override
            public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {

            }

            @Override
            public void onSynthesizeFinish(String s) {

            }

            @Override
            public void onSpeechStart(String s) {

            }

            @Override
            public void onSpeechProgressChanged(String s, int i) {

            }

            @Override
            public void onSpeechFinish(String s) {

            }

            @Override
            public void onError(String s, SpeechError speechError) {

            }
        });
//        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE,
//                TEXT_MODEL_FILE_FULL_PATH_NAME);
//        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
//                SPEECH_MODEL_FILE_FULL_PATH_NAME);
//        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_LICENCE_FILE,
//                LICENSE_FILE_FULL_PATH_NAME);
        //speechSynthesizer.setAppId("9205750");
        //speechSynthesizer.setApiKey("nsK78fT6cgINqTYdp2kKwHR2rwgkjuUr", "yzHtu2WD8ZbDHuL3Chp9lR0zNHxtgPjv");
        speechSynthesizer.setAppId("9141740");
        speechSynthesizer.setApiKey("iLjKE2lg0aNGd6blmLB3yGBk", "c4fea423d578e1af73b121a738718aca");
        AuthInfo authInfo = speechSynthesizer.auth(TtsMode.ONLINE);
        speechSynthesizer.initTts(TtsMode.ONLINE);

        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "5");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        speechSynthesizer.setParam(SpeechSynthesizer. PARAM_MIX_MODE,
                MIX_MODE_DEFAULT);
        speechSynthesizer.setParam(SpeechSynthesizer. PARAM_AUDIO_ENCODE,
                AUDIO_ENCODE_AMR);
        speechSynthesizer.setParam(SpeechSynthesizer. PARAM_AUDIO_RATE,
                AUDIO_BITRATE_AMR_15K85);
        speechSynthesizer.setParam(SpeechSynthesizer. PARAM_VOCODER_OPTIM_LEVEL, "0");
    }

    public SpeechSynthesizer getSpeechSynthesizer(){
        return speechSynthesizer;
    }

    public static LocationManager getInstance() {
        if (ourInstance == null) {
            throw new RuntimeException("need to init LocationManager!");
        }
        return ourInstance;
    }

    public static void init(Context applicationContext) {
        ourInstance = new LocationManager(applicationContext.getApplicationContext());
    }

    public BDLocation getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(BDLocation currentLocation) {
        this.currentLocation = currentLocation;
        String json = new Gson().toJson(currentLocation);
        SharedPrefsUtil.getInstance().putString("currentLocation", json);
        if (locationListener != null) locationListener.getCurrentLocation(currentLocation);
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 5000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    public void startLocation() {
        mLocationClient.start();
    }

    public void stopLocation() {
        mLocationClient.stop();
    }

    public void requestionLocation() {
        mLocationClient.requestLocation();
    }

    public LocationClient getLocationClient() {
        return mLocationClient;
    }

    public void setLocationListener(LocationListener locationListener) {
        this.locationListener = locationListener;
    }

    public interface LocationListener {
        void getCurrentLocation(BDLocation location);
    }
}
