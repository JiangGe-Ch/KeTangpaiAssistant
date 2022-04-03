package us.ch.jiangge.ketangpaiAssistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;

import java.util.List;

import us.ch.jiangge.ketangpaiAssistant.adapter.ContentListAdapter;
import us.ch.jiangge.ketangpaiAssistant.client.KeTangPaiClient;
import us.ch.jiangge.ketangpaiAssistant.emuns.ContentType;
import us.ch.jiangge.ketangpaiAssistant.entity.ContentItem;
import us.ch.jiangge.ketangpaiAssistant.utils.ContentSqliteOpenHelper;

public class MainActivity extends AppCompatActivity {

    private final String TAG="MainActivity";

    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.d(TAG, "handleMessage: message.what=["+msg.what+"]...");
            setUpUI();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String TAG="onCreate";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: ");
        KeTangPaiClient client=new KeTangPaiClient(MainActivity.this);

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

    private void setUpUI(){
        Log.d(TAG, "setUpUI: ...");
        ContentSqliteOpenHelper sqlHelper=new ContentSqliteOpenHelper(MainActivity.this, "contents.db", null, 1);
        List<ContentItem> itemList=sqlHelper.initContentsToItemList();
        ContentListAdapter adapter=new ContentListAdapter(MainActivity.this, R.layout.listitem_content, itemList);
        ListView listView=(ListView) findViewById(R.id.content_list_view);
        listView.setAdapter(adapter);
    }
}