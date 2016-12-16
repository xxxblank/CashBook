package com.xpjun.newcashbook.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.xpjun.newcashbook.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by U-nookia on 2016/8/19.
 */
public class LogInActivity extends Activity{

    @Bind(R.id.name_login)
    EditText name;
    @Bind(R.id.password_login)
    EditText password;
    @Bind(R.id.bt_login)
    Button login;
    @Bind(R.id.signUp_login)
    TextView signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        ButterKnife.bind(this);
        BmobUser bmobUser = BmobUser.getCurrentUser();
        if(bmobUser != null){
            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{

        }
    }


    @OnClick(R.id.bt_login)
    public void login(){
        String name_login = name.getText().toString();
        String password_login = password.getText().toString();
        BmobUser user = new BmobUser();
        user.setUsername(name_login);
        user.setPassword(password_login);
        user.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if (e == null) {
                    Toast.makeText(LogInActivity.this, bmobUser.getUsername() + "   登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LogInActivity.this, "登录失败" + e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @OnClick(R.id.signUp_login)
    public void signUp(){
        Intent intent = new Intent(LogInActivity.this,SignUpActivity.class);
        startActivity(intent);
    }
}
