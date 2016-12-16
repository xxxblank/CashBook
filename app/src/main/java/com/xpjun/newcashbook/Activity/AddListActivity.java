package com.xpjun.newcashbook.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.xpjun.newcashbook.R;
import com.xpjun.newcashbook.model.ExpenditureBean;

import cn.bmob.v3.BmobACL;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by U-nookia on 2016/7/19.
 */
public class AddListActivity extends Activity implements AdapterView.OnItemSelectedListener{

    private Spinner spinner_expenditure;
    private ArrayAdapter<CharSequence> adapter_expenditure;
    private EditText editText_price,editText_remark;
    private Button yes;
    private String thing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        initView();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String price, content;
                price = editText_price.getText().toString();
                content = editText_remark.getText().toString();
                if (price==null||price.equals(""))
                    Toast.makeText(AddListActivity.this,"请添加该支出的具体数目",Toast.LENGTH_SHORT).show();
                else {
                    ExpenditureBean expenditure = new ExpenditureBean();
                    expenditure.setThing(thing);
                    expenditure.setNote(content);
                    expenditure.setPrice(price);

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("thing",thing);
                    bundle.putString("note",content);
                    bundle.putString("price", price);
                    intent.putExtras(bundle);
                    setResult(1, intent);

                    BmobACL acl = new BmobACL();
                    acl.setReadAccess(BmobUser.getCurrentUser(), true);
                    acl.setWriteAccess(BmobUser.getCurrentUser(), true);
                    expenditure.setACL(acl);
                    expenditure.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Toast.makeText(AddListActivity.this, "数据添加成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddListActivity.this, "数据添加失败" + e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    finish();
                }
            }
        });

    }

    private void initView() {
        spinner_expenditure = (Spinner) findViewById(R.id.spinner_expenditure);
        adapter_expenditure = ArrayAdapter.createFromResource(this,R.array.expenditure,R.layout.support_simple_spinner_dropdown_item);
        editText_price = (EditText) findViewById(R.id.editText_price);
        editText_remark = (EditText) findViewById(R.id.editText_remark);
        yes = (Button) findViewById(R.id.add_list_yes);
        adapter_expenditure.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_expenditure.setOnItemSelectedListener(this);
        spinner_expenditure.setAdapter(adapter_expenditure);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        thing = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
