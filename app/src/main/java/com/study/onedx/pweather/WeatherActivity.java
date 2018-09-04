package com.study.onedx.pweather;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.onedx.homeweather.R;
import com.study.onedx.pweather.gson.BingPic;
import com.study.onedx.pweather.gson.Forecast;
import com.study.onedx.pweather.gson.LifeStyle;
import com.study.onedx.pweather.gson.Weather;
import com.study.onedx.pweather.util.HttpUtil;
import com.study.onedx.pweather.util.Utility;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    public SwipeRefreshLayout swipeRefresh;
    public DrawerLayout drawerLayout;
    private Button setIdButton;

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private LinearLayout lifestyleLayout;
    private ImageView bingPic;

    private static final String USERNAME = "HE1808302218131363";
    private static final String HEWEATHER_KEY = "34761625bab849509d4ed6f7b8ab118a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_weather);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setIdButton = (Button) findViewById(R.id.setId_button);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        lifestyleLayout = (LinearLayout) findViewById(R.id.lifestyle_layout);
        bingPic = (ImageView) findViewById(R.id.bing_pic);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        String bacPic = prefs.getString("bing_pic", null);
        if(bacPic != null){
            Glide.with(this).load(bacPic).into(bingPic);
        }else {
            loadBingPic();
        }
        final String weatherId;
        if(weatherString != null){
            Weather weather = Utility.handleWeatherResponse(weatherString);
            weatherId = weather.basic.cityId;
            showWeatherInfo(weather);
            requestWeather(weatherId);
        }else {
            weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });
        setIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    /**
     * 根据id请求城市天气信息
     */
    public void requestWeather(final String weatherId){
        long t = System.currentTimeMillis();
        HashMap dictionary = new HashMap();
        dictionary.put("location", weatherId);
        dictionary.put("username", USERNAME);
        dictionary.put("t", t);
        String key = null;
        try {
            key = getSignature(dictionary, HEWEATHER_KEY);
        }catch (IOException e){
            e.printStackTrace();
        }
        String weatherUrl = "https://free-api.heweather.com/s6/weather?" +
                "location=" + weatherId +
                "&username=" + USERNAME +
                "&t=" + t +
                "&sign=" + key;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather != null && "ok".equals(weather.status)){
                            SharedPreferences.Editor editor =
                                    PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else {
                            Toast.makeText(WeatherActivity.this,
                                    "status false", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,
                                "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        loadBingPic();
    }

    /**
     * 展示Weather数据
     */
    private void showWeatherInfo(Weather weather){
        String cityName = weather.basic.cityName;
        String updateTime = weather.update.locationTime;
        String degree = weather.now.fl + "℃";
        String weatherInfo = weather.now.Condition;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for(Forecast forecast : weather.forecastList){
            View view = LayoutInflater.from(this).inflate(
                    R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.dayCondition);
            maxText.setText(forecast.maxTemperature);
            minText.setText(forecast.minTemperature);
            forecastLayout.addView(view);
        }
        lifestyleLayout.removeAllViews();
        for(LifeStyle lifeStyle : weather.lifeStyleList){
            View view = LayoutInflater.from(this).inflate(
                    R.layout.lifestyle_item, lifestyleLayout, false);
            TextView comfort = (TextView) view.findViewById(R.id.comfort);
            TextView comfortText = (TextView) view.findViewById(R.id.comfort_text);
            String type = getLifestyleType(lifeStyle.type);
            if(!"".equals(type)){
                comfort.setText(type + "：" + lifeStyle.comfort);
                comfortText.setText(lifeStyle.info);
                lifestyleLayout.addView(view);
            }
        }
        weatherLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 和风天气签名生成算法-JAVA版本
     * @param  params 请求参数集，所有参数必须已转换为字符串类型
     * @param  secret 签名密钥（用户的认证key）
     * @return 签名
     * @throws IOException
     */
    public static String getSignature(HashMap params, String secret) throws IOException {
        // 先将参数以其参数名的字典序升序进行排序
        Map sortedParams = new TreeMap(params);
        Set<Map.Entry> entrys = sortedParams.entrySet();

        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起

        StringBuilder baseString = new StringBuilder();
        for (Map.Entry param : entrys) {
            //sign参数 和 空值参数 不加入算法
            if(param.getValue()!=null && !"".equals(param.getKey().toString().trim())
                    && !"sign".equals(param.getKey().toString().trim())
                    && !"key".equals(param.getKey().toString().trim())
                    && !"".equals(param.getValue().toString().trim())) {
                baseString.append(param.getKey().toString().trim()).append("=")
                        .append(param.getValue().toString().trim()).append("&");
            }
        }
        if(baseString.length() > 0 ) {
            baseString.deleteCharAt(baseString.length() - 1).append(secret);
        }

        // 使用MD5对待签名串求签
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(baseString.toString().getBytes("UTF-8"));
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (GeneralSecurityException ex) {
            throw new IOException(ex);
        }
    }

    private String getLifestyleType(String type){
        if("comf".equals(type)){
            return "舒适度指数";
        }else if("cw".equals(type)){
            return "洗车指数";
        }else if("drsg".equals(type)){
            return "穿衣指数";
        }else if("flu".equals(type)){
            return "感冒指数";
        }else if("sport".equals(type)){
            return "运动指数";
        }else if("trav".equals(type)){
            return "旅游指数";
        }else if("uv".equals(type)){
            return "紫外线指数";
        }else if("air".equals(type)){
            return "空气污染扩散条件指数";
        }else if("ac".equals(type)){
            return "空调开启指数";
        }else if("ag".equals(type)){
            return "过敏指数";
        }else if("gl".equals(type)){
            return "太阳镜指数";
        }else if("mu".equals(type)){
            return "化妆指数";
        }else if("airc".equals(type)){
            return "晾晒指数";
        }else if("ptfc".equals(type)){
            return "交通指数";
        }else if("fisin".equals(type)){
            return "钓鱼指数";
        }else if("spi".equals(type)){
            return "防晒指数";
        }
        return "";
    }

    private void loadBingPic(){
        String requestBingPic = "http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final BingPic bingPicUrl = Utility.handleBingPicResponse(responseText);
                final String picUrl = "http://cn.bing.com" + bingPicUrl.url;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                            SharedPreferences.Editor editor =
                                    PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("bing_pic", picUrl);
                            editor.apply();
                            Glide.with(WeatherActivity.this).load(picUrl).into(bingPic);
                    }
                });
            }
        });
    }

}
