package us.ch.jiangge.ketangpaiAssistant.client;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import okhttp3.*;
import us.ch.jiangge.ketangpaiAssistant.utils.ContentSqliteOpenHelper;
import us.ch.jiangge.ketangpaiAssistant.utils.CourseInfoSqliteOpenHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class KeTangPaiClient {

    private Context context;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client=new OkHttpClient();
    private static final String TAG="KeTangPaiClient";

    public KeTangPaiClient(Context contextArgs){
        this.context=contextArgs;
    }

    public int start() throws Exception {
        Log.i(TAG, "start: ");
        String token=login();
        if(token==null) {
            Log.i(TAG, "start: 登录失败，未获取到token...");
            return -1;
        }
        Log.i(TAG, "start: 登录成功，获取到token["+token+"]...");;
        JSONArray courseListJson=getCourseList(token);
        if(courseListJson==null){
            Log.i(TAG, "start: 获取课程列表失败...");;
            return -2;
        }
        Log.i(TAG, "start: 获取到课程列表json["+courseListJson+"]...");
        storeCourseInfo(courseListJson);
        Log.i(TAG, "start: 成功建立课程信息映射...");

//        CourseInfoSqliteOpenHelper t=new CourseInfoSqliteOpenHelper(this.context, "courseInfo.db", null, 1);
//        t.showAllRecord();
        JSONArray contentListJson=getCourseContent(courseListJson, token);
        if(contentListJson.length()==0){
            Log.i(TAG, "start: 获取课程内容失败...");
            return -3;
        }
//        Log.i(TAG, "start: content["+contentListJson.toString()+"]...");
        ContentSqliteOpenHelper contentSqliteOpenHelper=new ContentSqliteOpenHelper(this.context, "contents.db", null, 1);
        contentSqliteOpenHelper.put(contentListJson);
//        contentSqliteOpenHelper.showAllRecord();


        return 1;
    }

    /**
     * 登录方法
     * @return  token
     * @throws IOException
     */
    private String login() throws Exception {
        Log.i(TAG, "login: ");
//        String bodyStr="{\"email\":\"133***1837\",\"password\":\"*********\",\"remember\":\"0\",\"code\":\"\",\"mobile\":\"\",\"type\":\"login\",\"reqtimestamp\":1648456768097}";
        Scanner sc=new Scanner(System.in);


        String email="课堂派账号";
        String password="课堂派密码";


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


    /**
     * 获取课程列表（以2021-2022学年第二学期为例，修改请求体可相应获取各学期数据）
     * @param token
     * @return  课程列表jsonarray
     * @throws IOException
     */
    private JSONArray getCourseList(String token) throws Exception {
        Log.i(TAG, "getCourseList: ");
//        String bodyStr="{\"isstudy\":\"1\",\"search\":\"\",\"semester\":\"2021-2022\",\"term\":\"2\",\"reqtimestamp\":1648457586350}";
        JSONObject reqjson=new JSONObject("{\"isstudy\":\"1\",\"search\":\"\",\"semester\":\"2021-2022\",\"term\":\"2\",\"reqtimestamp\":1648457586350}");
//        JSONObject reqjson=JSONObject.parseObject("{\"isstudy\":\"1\",\"search\":\"\",\"semester\":\"2021-2022\",\"term\":\"2\",\"reqtimestamp\":1648457586350}");
        reqjson.put("reqtimestamp", System.currentTimeMillis());
        RequestBody body=RequestBody.create(JSON, reqjson.toString());
        Request request=new Request.Builder()
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.82 Safari/537.36")
                .addHeader("token", token)
                .url("https://openapiv5.ketangpai.com//CourseApi/semesterCourseList")
                .post(body)
                .build();
        Response response=client.newCall(request).execute();
        JSONObject json=new JSONObject(response.body().string());
        if(json.get("message").equals("访问成功")){
            return json.getJSONArray("data");
        }else{
            return null;
        }
    }

    /**
     * 获取课程内容（作业）
     * @param courseListJson    课程列表的jsonarray
     * @param token     token 从登录方法获取
     * @return  课程内容（作业）的jsonarray
     * @throws IOException
     */
    private JSONArray getCourseContent(JSONArray courseListJson, String token) throws Exception {
        Log.i(TAG, "getCourseContent: ");
//        String bodyStr="{\"contenttype\":4,\"dirid\":0,\"lessonlink\":[],\"sort\":[],\"page\":1,\"limit\":50,\"desc\":3,\"courserole\":0,\"vtr_type\":\"\",\"reqtimestamp\":1648459039651}";
        JSONArray contenJsonArray=new JSONArray();
        for(int i=0; i<courseListJson.length(); i++){
            JSONObject reqBodyJson=new JSONObject("{\"contenttype\":4,\"dirid\":0,\"lessonlink\":[],\"sort\":[],\"page\":1,\"limit\":50,\"desc\":3,\"courserole\":0,\"vtr_type\":\"\"}");
//            JSONObject reqBodyJson=JSONObject.parseObject("{\"contenttype\":4,\"dirid\":0,\"lessonlink\":[],\"sort\":[],\"page\":1,\"limit\":50,\"desc\":3,\"courserole\":0,\"vtr_type\":\"\"}");
            reqBodyJson.put("courseid", courseListJson.getJSONObject(i).getString("id"));
            reqBodyJson.put("reqtimestamp", System.currentTimeMillis());
            Log.i(TAG, "getCourseContent: ");
            RequestBody body=RequestBody.create(JSON, reqBodyJson.toString());
            Request request=new Request.Builder()
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.82 Safari/537.36")
                    .addHeader("token", token)
                    .url("https://openapiv5.ketangpai.com//FutureV2/CourseMeans/getCourseContent")
                    .post(body)
                    .build();
            Response response=client.newCall(request).execute();
            JSONObject json=new JSONObject(response.body().string());
            if(json.getString("message").equals("访问成功")){
                Log.i(TAG, "getCourseContent: courseid ["+courseListJson.getJSONObject(i).getString("id")+"] 访问成功...");
                JSONArray contentArray=json.getJSONObject("data").getJSONArray("list");
                for(int j=0;j<contentArray.length();j++){
                    contenJsonArray.put(contentArray.getJSONObject(j));
//                    contenJsonArray.add(contentArray.getJSONObject(j));
                }
//                contenJsonArray.add(json.getJSONObject("data"));
            }else{
                Log.i(TAG, "getCourseContent: 错误，courseid ["+courseListJson.getJSONObject(i).getString("id")+"] 访问失败...");
            }
        }
        Log.i(TAG, "getCourseContent: "+contenJsonArray.length()+"项课程内容访问成功...");
        return contenJsonArray;
    }

    /**
     * 通过数据库建立课程id与课程信息的映射
     * @param contentJsonList
     * @return
     */
    private void storeCourseInfo(JSONArray contentJsonList) throws Exception {
        Log.d(TAG, "storeCourseInfo: ");
        CourseInfoSqliteOpenHelper courseInfoDb=new CourseInfoSqliteOpenHelper(this.context, "courseInfo.db", null, 1);
        for(int i=0;i<contentJsonList.length();i++){
            courseInfoDb.put(contentJsonList.getJSONObject(i));
        }
        courseInfoDb.close();
//        Map<String, Map<String, String>> courseInfos=new HashMap<>();
//        Map<String, String> details;
//        JSONObject cJson;
//        for(int i=0;i<contentJsonList.length();i++){
//            details=new HashMap<>();
//            cJson=contentJsonList.getJSONObject(i);
//            details.put("name", cJson.getString("coursename"));     //课程名
//            details.put("code", cJson.getString("code"));       //课程号
//            details.put("class", cJson.getString("classname"));     //课程班级
//            details.put("teacher", cJson.getString("username"));        //课程老师
//            courseInfos.put(cJson.getString("id"), details);
//        }
//
//        return courseInfos;
    }

    /**
     * 将获取到的ContentList通过结束时间排序
     * @param contentList 待排序json数组
     * @param dec true 降序，fals 升序
     */
    private void sortContentListByEndTime(JSONArray contentList, boolean dec) throws Exception {
        if(dec){
            for(int j=0;j<contentList.length();j++){
                for(int i=0; i<contentList.length()-1;i++){
                    if(contentList.getJSONObject(i).getLong("endtime")<contentList.getJSONObject(i+1).getLong("endtime")){
                        JSONObject tmp=contentList.getJSONObject(i);
                        contentList.put(i, contentList.getJSONObject(i+1));
                        contentList.put(i+1, tmp);
                    }
                }
            }
        }else{
            for(int j=0;j<contentList.length();j++){
                for(int i=0; i<contentList.length()-1;i++){
                    if(contentList.getJSONObject(i).getLong("endtime")>contentList.getJSONObject(i+1).getLong("endtime")){
                        JSONObject tmp=contentList.getJSONObject(i);
                        contentList.put(i, contentList.getJSONObject(i+1));
                        contentList.put(i+1, tmp);
                    }
                }
            }
        }
    }

    /**
     * 示例输出方法
     * @param contentListJson
     */
    private void printContentList(JSONArray contentListJson, Map<String, Map<String, String>> courseInfos) throws Exception {
        sortContentListByEndTime(contentListJson, true);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=0; i<contentListJson.length(); i++){
            long lt=Long.parseLong(contentListJson.getJSONObject(i).getString("endtime"));
            Log.i(TAG, "printContentList: endtimestamp["+lt+"]...");
            Date endtime=new Date(lt*1000);
            String mstatusStr;
            if(contentListJson.getJSONObject(i).getInt("mstatus")==1){
                mstatusStr="是";
            }else{
                mstatusStr="否";
            }
            System.out.println("[作业名称："+contentListJson.getJSONObject(i).getString("title")+
                    "] [截止时间："+sdf.format(endtime)+
                    "] [已提交："+mstatusStr+"]"+
                    "课程信息：["+courseInfos.get(contentListJson.getJSONObject(i).getString("courseid")).toString()+"]...");
        }
    }
}