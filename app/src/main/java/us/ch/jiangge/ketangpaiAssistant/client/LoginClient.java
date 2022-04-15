package us.ch.jiangge.ketangpaiAssistant.client;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginClient {

    private static final String TAG="LoginClient";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client=new OkHttpClient();

    /**
     * 登录方法
     * @return  token
     * @throws IOException
     */
    public static String login(String email, String password) throws Exception {
        Log.i(TAG, "login: ");
//        String bodyStr="{\"email\":\"133***1837\",\"password\":\"*********\",\"remember\":\"0\",\"code\":\"\",\"mobile\":\"\",\"type\":\"login\",\"reqtimestamp\":1648456768097}";


//        String email="133****1837";
//        String password="***************";


        JSONObject reqbody=new JSONObject("{\"remember\":\"0\",\"code\":\"\",\"mobile\":\"\",\"type\":\"login\"}");
//        JSONObject reqbody=JSONObject.parseObject("{\"remember\":\"0\",\"code\":\"\",\"mobile\":\"\",\"type\":\"login\"}");
        reqbody.put("email", email);
        reqbody.put("password", password);
        reqbody.put("reqtimestamp", System.currentTimeMillis());
        RequestBody body=RequestBody.create(JSON, reqbody.toString());
        Request request=new Request.Builder()
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.82 Safari/537.36")
                .url("https://openapiv5.ketangpai.com//UserApi/login")
                .post(body)
                .build();
        Response response=client.newCall(request).execute();
        JSONObject json=new JSONObject(response.body().string());
        if(json.get("message").equals("访问成功")){
            return json.getJSONObject("data").get("token").toString();
        }else {
            return null;
        }
    }
}
