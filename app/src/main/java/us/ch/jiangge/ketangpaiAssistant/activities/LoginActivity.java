package us.ch.jiangge.ketangpaiAssistant.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import us.ch.jiangge.ketangpaiAssistant.R;
import us.ch.jiangge.ketangpaiAssistant.client.LoginClient;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private final String TAG="LoginActivity";

    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:{
                    Bundle bundle=msg.getData();
                    Toast.makeText(LoginActivity.this, bundle.getString("exception"), Toast.LENGTH_SHORT).show();
                    break;
                }
                case -1:{
                    Toast.makeText(LoginActivity.this, "登录失败，请检查账号或密码...", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 1:{
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor prefEditer=getSharedPreferences("rememberuser", MODE_PRIVATE).edit();

                    Bundle data=msg.getData();
                    String token=data.getString("token");

                    CheckBox remember=findViewById(R.id.login_remember);
                    if(remember.isChecked()){
                        EditText emailEditer=findViewById(R.id.login_email);
                        EditText passwordEditer=findViewById(R.id.login_password);
                        String lastUser=emailEditer.getText().toString();
                        prefEditer.putString("lastUser", lastUser);
                        prefEditer.putString(lastUser+"_pwd", passwordEditer.getText().toString());
                        prefEditer.putString(lastUser+"_token", token);
                        prefEditer.apply();
                    }

                    Intent intent=getIntent();
                    intent.putExtra("token", token);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.actionbar_activity_login);
        }
        setContentView(R.layout.activity_login);
        EditText emailEditer=findViewById(R.id.login_email);
        emailEditer.setOnFocusChangeListener(this);
        Button loginBt=findViewById(R.id.login_loginBt);
        loginBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_loginBt:
                EditText emailEditer=findViewById(R.id.login_email);
                EditText passwordEditer=findViewById(R.id.login_password);
                String email=emailEditer.getText().toString();
                String password=passwordEditer.getText().toString();
                Thread loginThread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String token=null;
                        Message msg=new Message();
                        try {
                            token=LoginClient.login(email, password);
                        } catch (Exception e) {
                            e.printStackTrace();
                            msg.what=0;
                            Bundle exceptionData=new Bundle();
                            exceptionData.putString("exception", e.toString());
                            msg.setData(exceptionData);
                            handler.sendMessage(msg);
                            return;
                        }
                        if(token==null){
                            msg.what=-1;
                        }else {
                            msg.what=1;
                            Bundle data=new Bundle();
                            data.putString("token", token);
                            msg.setData(data);
                        }
                        handler.sendMessage(msg);
                    }
                });
                loginThread.start();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.d(TAG, "onFocusChange: ");
        switch (v.getId()){
            case R.id.login_email:{
                if(!hasFocus){
                    Log.d(TAG, "onFocusChange:  auto fill pwd");
                    SharedPreferences pref=getSharedPreferences("rememberuser", MODE_PRIVATE);
                    EditText emailEditer=findViewById(R.id.login_email);
                    String email=emailEditer.getText().toString();
                    String rememberPwd=pref.getString(email+"_pwd", null);
                    if(rememberPwd!=null){
                        Log.d(TAG, "onFocusChange:  auto fill pwd  ,  remember !=null");
                        EditText pwdEditer=findViewById(R.id.login_password);
                        pwdEditer.setText((CharSequence) rememberPwd);
                        CheckBox remember=findViewById(R.id.login_remember);
                        remember.setChecked(true);
                    }
                }
            }
        }
    }
}
