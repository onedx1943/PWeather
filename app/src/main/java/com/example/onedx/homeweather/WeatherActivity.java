package com.example.onedx.homeweather;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.onedx.homeweather.gson.Weather;
import com.example.onedx.homeweather.util.Utility;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;

    private static final String USERNAME = "HE1808302218131363";
    private static final String HEWEATHER_KEY = "34761625bab849509d4ed6f7b8ab118a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        if(weatherString != null){
            Weather weather = Utility.handleWeatherResponse(weatherString);
            //showWeatherInfo(weather);
        }else {
            String weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 根据id请求城市天气信息
     */
    public void requestWeather(final String weatherId){
        String weatherUrl = "";

    }

    /**
     * 展示Weather数据
     */
    private void showWeatherInfo(Weather weather){

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


}
