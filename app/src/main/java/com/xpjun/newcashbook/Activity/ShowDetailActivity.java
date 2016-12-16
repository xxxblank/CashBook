package com.xpjun.newcashbook.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xpjun.newcashbook.R;
import com.xpjun.newcashbook.model.adapter.MyShowDetailsAdapter;
import com.xpjun.newcashbook.model.adapter.MyShowDetailsExpenAdapter;
import com.xpjun.newcashbook.model.IncomeBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by U-nookia on 2016/8/24.
 */
public class ShowDetailActivity extends Activity {
    private TextView title;
    private ListView listView;
    private String start,end;
    private MyShowDetailsAdapter adapter;
    private MyShowDetailsExpenAdapter adapterOfExpenditure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        title = (TextView) findViewById(R.id.add_list_title_activity);
        listView = (ListView) findViewById(R.id.listView);
        Intent intent = getIntent();
        int key = intent.getIntExtra("show", -1);
        int year = intent.getIntExtra("year",-1);
        int month= intent.getIntExtra("month",-1);
        initMsg(year,month);
        //Toast.makeText(ShowDetailActivity.this,"hhh"+year+month,Toast.LENGTH_SHORT).show();
        setListFromKey(key);
    }

    private void setListFromKey(int key) {
        switch (key){
            case 0:
                title.setText("生活费明细");
                getMsgFromBmob("生活费");
                break;
            case 1:
                title.setText("工资明细");
                getMsgFromBmob("工资");
                break;
            case 2:
                title.setText("红包明细");
                getMsgFromBmob("红包");
                break;
            case 3:
                title.setText("奖金明细");
                getMsgFromBmob("奖金");
                break;
            case 4:
                title.setText("借款明细");
                getMsgFromBmob("借款");
                break;
            case 5:
                title.setText("其他明细");
                getMsgFromBmob("其他");
                break;
            case 6:
                title.setText("饮食费用明细");
                getMsgFromBmob("饮食");
                break;
        }
    }

    private void initMsg(int year,int month) {
        if (month<10){
            if (year%4==0&&year%100!=0 || year % 400 ==0){
                if (month==1||month==3||month==5||month==7||month==8){
                    start = year + "-0" + month + "-01" + " " + "00:00:00";
                    end = year + "-0" + month + "-31" + " " + "23:59:59";
                }else if (month==4||month == 6||month == 9){
                    start = year + "-0" + month + "-01" + " " + "00:00:00";
                    end = year + "-0" + month + "-30" + " " + "23:59:59";
                }else if (month==2){
                    start = year + "-0" + month + "-01" + " " + "00:00:00";
                    end = year + "-0" + month + "-29" + " " + "23:59:59";
                }
            }else {
                if (month==1||month==3||month==5||month==7||month==8){
                    start = year + "-0" + month + "-01" + " " + "00:00:00";
                    end = year + "-0" + month + "-31" + " " + "23:59:59";
                }else if (month==4||month == 6||month == 9){
                    start = year + "-0" + month + "-01" + " " + "00:00:00";
                    end = year + "-0" + month + "-30" + " " + "23:59:59";
                }else if (month==2){
                    start = year + "-0" + month + "-01" + " " + "00:00:00";
                    end = year + "-0" + month + "-28" + " " + "23:59:59";
                }
            }
        }else {
            if (month==10||month==12){
                start = year + "-" + month + "-01" + " " + "00:00:00";
                end = year + "-" + month + "-31" + " " + "23:59:59";
            }else if (month==11){
                start = year + "-" + month + "-01" + " " + "00:00:00";
                end = year + "-" + month + "-30" + " " + "23:59:59";
            }
        }
    }

    public void getMsgFromBmob(String msg) {
        BmobQuery<IncomeBean> query = new BmobQuery<IncomeBean>();
        List<BmobQuery<IncomeBean>> and = new ArrayList<BmobQuery<IncomeBean>>();
        BmobQuery<IncomeBean> q1 = new BmobQuery<IncomeBean>();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date data = null;
        try {
            data = sdf1.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q1.addWhereGreaterThanOrEqualTo("createdAt",new BmobDate(data));
        and.add(q1);

        BmobQuery<IncomeBean> q2 = new BmobQuery<IncomeBean>();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1  = null;
        try {
            date1 = sdf2.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q2.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date1));
        and.add(q2);

        BmobQuery<IncomeBean> q3 = new BmobQuery<IncomeBean>();
        q3.addWhereEqualTo("thing", msg);
        q3.setLimit(1000);
        and.add(q3);

        query.and(and);
        query.findObjects(new FindListener<IncomeBean>() {
            @Override
            public void done(List<IncomeBean> list, BmobException e) {
                if (list!=null){
                    adapter = new MyShowDetailsAdapter(list,ShowDetailActivity.this);
                    listView.setAdapter(adapter);
                }else {
                    Toast.makeText(ShowDetailActivity.this,"没有该项数据记录",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
