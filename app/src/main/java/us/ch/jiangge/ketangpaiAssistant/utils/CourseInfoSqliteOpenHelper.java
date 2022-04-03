package us.ch.jiangge.ketangpaiAssistant.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONObject;

public class CourseInfoSqliteOpenHelper extends SQLiteOpenHelper {
    private static final String TAG="CourseInfoSqliteOpenHelper";

    public CourseInfoSqliteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        Log.d(TAG, "CourseInfoSqliteOpenHelper: new...  content["+context+"]...");
    }

    private String CREATE_TABLE="create table courseinfo("+
            "courseid text primary key,"+
            "coursename text,"+
            "semester text,"+
            "term text,"+
            "code text,"+
            "classname text,"+
            "techer text)";
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: ");
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: ");
    }

    /**
     * 插入一条课程信息记录
     * @param infoJson
     * @throws Exception
     */
    public int put(JSONObject infoJson) throws Exception {
        Log.d(TAG, "put: ");
        if(isExist(infoJson)){
            return update(infoJson);
        }else{
            SQLiteDatabase db=getWritableDatabase();
            ContentValues values=new ContentValues();
            values.put("courseid", infoJson.getString("id"));
            values.put("coursename", infoJson.getString("coursename"));
            values.put("semester", infoJson.getString("semester"));
            values.put("term", infoJson.getString("term"));
            values.put("code", infoJson.getString("code"));
            values.put("classname", infoJson.getString("classname"));
            values.put("techer", infoJson.getString("username"));
            Log.d(TAG, "put: values=["+values.toString()+"]...");
            int result=(int)db.insert("courseInfo", null, values);
            db.close();
            return result;
        }
    }

    /**
     * 更新一条课程信息数据
     * @param infoJson
     * @return
     * @throws Exception
     */
    public int update(JSONObject infoJson) throws Exception {
        Log.d(TAG, "update: infoJson"+infoJson.toString());
        SQLiteDatabase db=getWritableDatabase();
        String courseid=infoJson.getString("id");
        ContentValues values=new ContentValues();
        values.put("courseid", infoJson.getString("id"));
        values.put("coursename", infoJson.getString("coursename"));
        values.put("semester", infoJson.getString("semester"));
        values.put("term", infoJson.getString("term"));
        values.put("code", infoJson.getString("code"));
        values.put("classname", infoJson.getString("classname"));
        values.put("techer", infoJson.getString("username"));
        Log.d(TAG, "update: values={"+values.toString()+"]...");
        int result=db.update("courseInfo", values, "courseid=?", new String[]{courseid});
        db.close();
        return result;
    }

    /**
     * 判断infoJson中的信息对应courseid记录是否存在
     * @param infoJson
     * @return
     */
    public boolean isExist(JSONObject infoJson) throws Exception {
        Log.d(TAG, "isExist: ");
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query("courseinfo", null, "courseid=?", new String[]{infoJson.getString("id")}, null, null, null);
        Log.d(TAG, "isExist: courseid=["+infoJson.getString("id")+"], cursor.getCount=["+cursor.getCount()+"]...");
        if(cursor.getCount()>0){
            db.close();
            return true;
        }else {
            db.close();
            return false;
        }
    }

    /**
     * 更具courseid获取对应课程的信息
     * @param courseid
     * @return
     * @throws Exception
     */
    @SuppressLint("Range")
    public JSONObject getInfo(String courseid) throws Exception {
        Log.d(TAG, "getInfo: ");
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query("courseinfo", new String[]{"*"}, "courseid=?", new String[]{courseid}, null, null, null);
        if(cursor.getCount()==1){
            cursor.moveToFirst();
            JSONObject infoJson=new JSONObject();
            infoJson.put("id", courseid);
            infoJson.put("semester", cursor.getString(cursor.getColumnIndex("semester")));
            infoJson.put("term", cursor.getString(cursor.getColumnIndex("term")));
            infoJson.put("code", cursor.getString(cursor.getColumnIndex("code")));
            infoJson.put("classname", cursor.getString(cursor.getColumnIndex("classname")));
            infoJson.put("username", cursor.getString(cursor.getColumnIndex("techer")));
            Log.d(TAG, "getInfo: infoJson=["+infoJson.toString()+"]...");
            return infoJson;
        }else {
            Log.d(TAG, "getInfo: cursor.getCount=["+cursor.getCount()+"]...");
            return null;
        }
    }
    
        /*
    "courseid text primary key,"+
            "coursename text,"+
            "semester text,"+
            "term text,"+
            "code text,"+
            "classname text,"+
            "techer text)"
     */

    @SuppressLint("Range")
    public String getCourseName(String courseid){
        Log.d(TAG, "getCourseName: courseid["+courseid+"]...");
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query("courseinfo", new String[]{"coursename"}, "courseid=?", new String[]{courseid}, null, null, null);
        if(cursor.getCount()==1&&cursor.moveToNext()){
            String coursename=cursor.getString(cursor.getColumnIndex("coursename"));
            db.close();
            Log.d(TAG, "getCourseName: courseid=["+courseid+"]， returned coursename["+coursename+"]...");
            return coursename;
        }else {
            db.close();
            return null;
        }
    }

    public String getCourseTeacher(String courseid){
        Log.d(TAG, "getCourseTeacher: courseid["+courseid+"]...");
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query("courseInfo", new String[]{"techer"}, "courseid=?", new String[]{courseid}, null, null, null);
        if(cursor.getCount()==1&&cursor.moveToNext()){
            @SuppressLint("Range") String teacher=cursor.getString(cursor.getColumnIndex("techer"));
            db.close();
            Log.d(TAG, "getCourseTeacher: courseid["+courseid+"]， returned teacher["+teacher+"]...");
            return teacher;
        }else {
            db.close();
            return null;
        }
    }

    public String getCourseClassName(String courseid){
        Log.d(TAG, "getCourseClassName: courseid["+courseid+"]...");
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query("courseInfo", new String[]{"classname"}, "courseid=?", new String[]{courseid}, null, null, null);
        if(cursor.getCount()==1&&cursor.moveToNext()){
            @SuppressLint("Range") String className=cursor.getString(cursor.getColumnIndex("classname"));
            db.close();
            Log.d(TAG, "getCourseClassName: courseid["+courseid+"]， returned className["+className+"]...");
            return className;
        }else {
            db.close();
            return null;
        }
    }

    @SuppressLint("Range")
    public void showAllRecord(){
        Log.d(TAG, "showAllRecord: ");
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query("courseinfo", null, null, null, null, null, null);
        while (cursor.moveToNext()){
            Log.d(TAG, "showAllRecord: data["+cursor.getString(cursor.getColumnIndex("courseid"))+","+
                    cursor.getString(cursor.getColumnIndex("coursename"))+","+
                    cursor.getString(cursor.getColumnIndex("semester"))+","+
                    cursor.getString(cursor.getColumnIndex("term"))+","+
                    cursor.getString(cursor.getColumnIndex("code"))+","+
                    cursor.getString(cursor.getColumnIndex("classname"))+","+
                    cursor.getString(cursor.getColumnIndex("techer"))+"]...");
        }
    }
}
