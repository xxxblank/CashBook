package com.xpjun.newcashbook.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xpjun.newcashbook.R;
import com.xpjun.newcashbook.model.adapter.MyShowDetailsExpenAdapter;
import com.xpjun.newcashbook.model.ExpenditureBean;

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
 * Created by U-nookia on 2016/8/25.
 */
public class ShowDetailExpenActivity extends Activity {
    private TextView title;
    private ListView listView;
    private String start,end;
    private MyShowDetailsExpenAdapter adapter;
    private List<String> msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        title = (TextView) findViewById(R.id.add_list_title_activity);
        listView = (ListView) findViewById(R.id.listView);
        msg = new ArrayList<>();
        Intent intent = getIntent();
        int key = intent.getIntExtra("show", -1);
        int year = intent.getIntExtra("year",-1);
        int month= intent.getIntExtra("month",-1);
        initMsg(year,month);
        setListFromKey(key);
    }

    private void setListFromKey(int key) {
        switch (key){
            case 0:
                title.setText("饮食费用明细");
                msg.add("饮食");
                getMsgFromBmob(msg);
                break;
            case 1:
                title.setText("娱乐明细");
                msg.add("电影");
                msg.add("KTV");
                msg.add("聚餐");
                getMsgFromBmob(msg);
                break;
            case 2:
                title.setText("生活费用明细");
                msg.add("衣服");
                msg.add("果蔬");
                msg.add("话费");
                msg.add("交通");
                msg.add("生活用品");
                getMsgFromBmob(msg);
                break;
            case 3:
                title.setText("医疗费用明细");
                msg.add("医疗");
                getMsgFromBmob(msg);
                break;
            case 4:
                title.setText("借出费用明细");
                msg.add("借出");
                getMsgFromBmob(msg);
                break;
            case 5:
                title.setText("还贷费用明细");
                msg.add("还贷");
                getMsgFromBmob(msg);
                break;
            case 6:
                title.setText("其他费用明细");
                msg.add("其他");
                msg.add("C");
                getMsgFromBmob(msg);
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

    public void getMsgFromBmob(List<String> msg) {
        BmobQuery<ExpenditureBean> query = new BmobQuery<ExpenditureBean>();
        List<BmobQuery<ExpenditureBean>> and = new ArrayList<BmobQuery<ExpenditureBean>>();
        BmobQuery<ExpenditureBean> q1 = new BmobQuery<ExpenditureBean>();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date data = null;
        try {
            data = sdf1.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q1.addWhereGreaterThanOrEqualTo("createdAt",new BmobDate(data));
        and.add(q1);

        BmobQuery<ExpenditureBean> q2 = new BmobQuery<ExpenditureBean>();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1  = null;
        try {
            date1 = sdf2.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q2.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date1));
        and.add(q2);

        if (msg.size()==1){
            BmobQuery<ExpenditureBean> q3 = new BmobQuery<ExpenditureBean>();
            q3.addWhereEqualTo("thing", msg.get(0));
            q3.setLimit(1000);
            and.add(q3);
        }else if (msg.size()==5){
            BmobQuery<ExpenditureBean> q4 = new BmobQuery<ExpenditureBean>();
            q4.addWhereEqualTo("thing",msg.get(0));
            BmobQuery<ExpenditureBean> q5 = new BmobQuery<ExpenditureBean>();
            q5.addWhereEqualTo("thing",msg.get(1));
            BmobQuery<ExpenditureBean> q6 = new BmobQuery<ExpenditureBean>();
            q6.addWhereEqualTo("thing",msg.get(2));
            BmobQuery<ExpenditureBean> q7 = new BmobQuery<ExpenditureBean>();
            q7.addWhereEqualTo("thing",msg.get(3));
            BmobQuery<ExpenditureBean> q8 = new BmobQuery<ExpenditureBean>();
            q8.addWhereEqualTo("thing", msg.get(4));
            List<BmobQuery<ExpenditureBean>> list = new ArrayList<BmobQuery<ExpenditureBean>>();
            list.add(q4);
            list.add(q5);
            list.add(q6);
            list.add(q7);
            list.add(q8);
            BmobQuery<ExpenditureBean> orSum = new BmobQuery<ExpenditureBean>();
            orSum.or(list);
            orSum.setLimit(1000);
            and.add(orSum);
        }else if (msg.size()==2){
            BmobQuery<ExpenditureBean> q4 = new BmobQuery<ExpenditureBean>();
            q4.addWhereEqualTo("thing",msg.get(0));
            BmobQuery<ExpenditureBean> q5 = new BmobQuery<ExpenditureBean>();
            q5.addWhereEqualTo("thing",msg.get(1));
            List<BmobQuery<ExpenditureBean>> list = new ArrayList<BmobQuery<ExpenditureBean>>();
            list.add(q4);
            list.add(q5);
            BmobQuery<ExpenditureBean> orSum = new BmobQuery<ExpenditureBean>();
            orSum.or(list);
            orSum.setLimit(1000);
            and.add(orSum);
        }else if (msg.size()==3){
            BmobQuery<ExpenditureBean> q4 = new BmobQuery<ExpenditureBean>();
            q4.addWhereEqualTo("thing",msg.get(0));
            BmobQuery<ExpenditureBean> q5 = new BmobQuery<ExpenditureBean>();
            q5.addWhereEqualTo("thing",msg.get(1));
            BmobQuery<ExpenditureBean> q6 = new BmobQuery<ExpenditureBean>();
            q6.addWhereEqualTo("thing",msg.get(2));
            List<BmobQuery<ExpenditureBean>> list = new ArrayList<BmobQuery<ExpenditureBean>>();
            list.add(q4);
            list.add(q5);
            list.add(q6);
            BmobQuery<ExpenditureBean> orSum = new BmobQuery<ExpenditureBean>();
            orSum.or(list);
            orSum.setLimit(1000);
            and.add(orSum);
        }

        query.and(and);
        query.findObjects(new FindListener<ExpenditureBean>() {
            @Override
            public void done(List<ExpenditureBean> list, BmobException e) {
                if (list!=null){
                    adapter = new MyShowDetailsExpenAdapter(list,ShowDetailExpenActivity.this);
                    listView.setAdapter(adapter);
                }else {
                    Toast.makeText(ShowDetailExpenActivity.this, "没有该项数据记录", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
