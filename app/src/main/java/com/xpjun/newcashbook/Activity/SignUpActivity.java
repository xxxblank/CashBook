package com.xpjun.newcashbook.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
public class SignUpActivity extends Activity {

    @Bind(R.id.signUp_name)
    EditText name;
    @Bind(R.id.signUp_password)
    EditText password;
    @Bind(R.id.signUp_email)
    EditText email;
    @Bind(R.id.bt_signUp)
    Button bt_signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_layout);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_signUp)
    public void signUp(){
        BmobUser user = new BmobUser();
        user.setUsername(name.getText().toString());
        user.setPassword(password.getText().toString());
        user.setEmail(email.getText().toString());
        user.signUp(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if (e == null) {
                    Toast.makeText(SignUpActivity.this, "注册成功" , Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, "注册失败" + e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
