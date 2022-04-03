package us.ch.jiangge.ketangpaiAssistant.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import us.ch.jiangge.ketangpaiAssistant.entity.Content;
import us.ch.jiangge.ketangpaiAssistant.entity.ContentItem;

public class ContentSqliteOpenHelper extends SQLiteOpenHelper {

    private final String TAG="ContentSqliteOpenHelper";

    private Context context;

    public ContentSqliteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
    }

    private String  CREATE_TABLE="create table content("+
            "id text primary key,"+
            "contenttype text not null,"+
            "title text,"+
            "endtime text,"+
            "mstatus text,"+
            "warningcheckrate text,"+
            "timestatus text,"+
            "courseid text)";

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
     * 根据内容id判断对应记录是否存在
     * @param id
     * @return
     */
    public boolean isExist(String id){
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query("content", null, "id=?", new String[]{id}, null, null, null);
        if(cursor.getCount()==0){
            db.close();
            return false;
        }else{
            db.close();
            return true;
        }
    }


    /**
     * 插入jsonarray中包含的所有内容信息
     * @param contentJsons
     * @return
     * @throws Exception
     */
    public int put(JSONArray contentJsons) throws Exception {
        Log.d(TAG, "put: ");
        SQLiteDatabase db = null;
        int result=0;
        for(int i=0;i<contentJsons.length();i++){
            JSONObject cJson=contentJsons.getJSONObject(i);
            if(isExist(cJson.getString("id"))){
                Log.d(TAG, "put: update["+cJson.toString()+"]...");
                result+=update(cJson);
            }else{
                db=getWritableDatabase();
                ContentValues values=new ContentValues();
                values.put("id", cJson.getString("id"));
                values.put("contenttype", cJson.getString("contenttype"));
                values.put("title", cJson.getString("title"));
                values.put("endtime", cJson.getString("endtime"));
                values.put("mstatus", cJson.getString("mstatus"));
                values.put("warningcheckrate", cJson.getString("warmingcheckrate"));
                values.put("timestatus", cJson.getString("timestate"));
                values.put("courseid", cJson.getString("courseid"));
                result+=(int)db.insert("content", null, values);
            }
        }
        if(db!=null){
            db.close();
        }
        return result;
    }

    /**
     * 更新对应内容id的记录
     * @param json
     * @return
     * @throws JSONException
     */
    private int update(JSONObject json) throws JSONException {
        Log.d(TAG, "update: ");
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("id", json.getString("id"));
        values.put("contenttype", json.getString("contenttype"));
        values.put("title", json.getString("title"));
        values.put("endtime", json.getString("endtime"));
        values.put("mstatus", json.getString("mstatus"));
        values.put("warningcheckrate", json.getString("warmingcheckrate"));
        values.put("timestatus", json.getString("timestate"));
        values.put("courseid", json.getString("courseid"));
        return db.update("content", values, "id=?", new String[]{json.getString("id")});
    }

    @SuppressLint("Range")
    public void showAllRecord(){
        Log.d(TAG, "showAllRecord: ");
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query("content", null, null, null, null, null, null);
        while (cursor.moveToNext()){
            Log.d(TAG, "showAllRecord: data["+
                    cursor.getString(cursor.getColumnIndex("id"))+","+
                    cursor.getString(cursor.getColumnIndex("contenttype"))+","+
                    cursor.getString(cursor.getColumnIndex("title"))+","+
                    cursor.getString(cursor.getColumnIndex("endtime"))+","+
                    cursor.getString(cursor.getColumnIndex("mstatus"))+","+
                    cursor.getString(cursor.getColumnIndex("warningcheckrate"))+","+
                    cursor.getString(cursor.getColumnIndex("timestatus"))+"]...");
        }
        db.close();
    }

    //"id text primary key,"+
//        "contenttype text not null,"+
//        "title text,"+
//        "endtime text,"+
//        "mstatus text,"+
//        "warningcheckrate text,"+
//        "timestatus text)"

    @SuppressLint("Range")
    public List<ContentItem> initContentsToItemList(){
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from content order by endtime desc", null);
//        Cursor cursor=db.query("content", null, null, null, null, null, "endtime");
        List<ContentItem> itemList=new ArrayList<>();
        ContentItem item;
        CourseInfoSqliteOpenHelper courseInfodbHelper=new CourseInfoSqliteOpenHelper(this.context, "courseInfo.db", null, 1);
        while (cursor.moveToNext()){
            String courseid=cursor.getString(cursor.getColumnIndex("courseid"));
            item=new ContentItem(cursor.getString(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("contenttype")),
                    cursor.getString(cursor.getColumnIndex("title")),
                    cursor.getString(cursor.getColumnIndex("endtime")),
                    cursor.getString(cursor.getColumnIndex("mstatus")),
                    cursor.getString(cursor.getColumnIndex("warningcheckrate")),
                    cursor.getString(cursor.getColumnIndex("timestatus")),
                    courseInfodbHelper.getCourseName(courseid),
                    courseInfodbHelper.getCourseTeacher(courseid),
                    courseInfodbHelper.getCourseClassName(courseid));
            Log.d(TAG, "initContentsToItemList:  added["+item.toString()+"]...");
            itemList.add(item);
        }
        return itemList;
    }
}
