package us.ch.jiangge.ketangpaiAssistant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import us.ch.jiangge.ketangpaiAssistant.activities.LoginActivity;
import us.ch.jiangge.ketangpaiAssistant.adapter.ContentListAdapter;
import us.ch.jiangge.ketangpaiAssistant.client.KeTangPaiClient;
import us.ch.jiangge.ketangpaiAssistant.entity.ContentItem;
import us.ch.jiangge.ketangpaiAssistant.utils.ContentSqliteOpenHelper;

public class MainActivity extends AppCompatActivity {

    private final String TAG="MainActivity";

    private static KeTangPaiClient client;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.d(TAG, "handleMessage: message.what=["+msg.what+"]...");
            if(msg.what==1){
                setUpListView();
            }else {
                Toast.makeText(MainActivity.this, "错误码："+msg.what, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: ");

        super.onCreate(savedInstanceState);

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            Log.d(TAG, "onCreate:  set actionbar...");
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.actionbar_activity_main);
        }

        setContentView(R.layout.activity_main);

        SharedPreferences pref=getSharedPreferences("rememberuser", MODE_PRIVATE);
        String lastUser=pref.getString("lastUser", null);
        Log.d(TAG, "onCreate:  lastUser=["+lastUser+"]......");
        if(lastUser==null){
            Log.d(TAG, "onCreate: lastUser null"+lastUser);
            Intent loginIntent=new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(loginIntent, 1);
        }else {
            String lastUserToken=pref.getString(lastUser+"_token", null);
            startKetangpaiClient(lastUserToken);
        }
    }

    private final int OFF=0;
    private final int EXIT=-1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, OFF, 1, "注销");
        menu.add(1, EXIT, 2, "退出程序");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case OFF:{
                client.setToken(null);
                Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, 1);
                break;
            }
            case EXIT:finish();break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String token=data.getStringExtra("token");
            startKetangpaiClient(token);
        }
    }

    private void startKetangpaiClient(String token){
        client=new KeTangPaiClient(MainActivity.this, token);

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Message message=new Message();
                    message.what=client.start();
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void setUpListView(){
        Log.d(TAG, "setUpUI: ...");
        ContentSqliteOpenHelper sqlHelper=new ContentSqliteOpenHelper(MainActivity.this, "contents.db", null, 1);
        List<ContentItem> itemList=sqlHelper.initContentsToItemList();
        ContentListAdapter adapter=new ContentListAdapter(MainActivity.this, R.layout.listitem_content, itemList);
        ListView listView=(ListView) findViewById(R.id.content_list_view);
        listView.setAdapter(adapter);
    }
}