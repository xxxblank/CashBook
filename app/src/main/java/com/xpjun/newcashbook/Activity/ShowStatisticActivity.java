package com.xpjun.newcashbook.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.xpjun.newcashbook.R;
import com.xpjun.newcashbook.model.IncomeBean;
import com.xpjun.newcashbook.view.custom.PieChart;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by U-nookia on 2016/8/24.
 */
public class ShowStatisticActivity extends Activity implements AdapterView.OnItemSelectedListener,View.OnClickListener{
    private LinearLayout life_money,credit,red_packet,reward,borrow_money,other;
    private TextView lifePrice,repayPrice,redPacketPrice,rewardPrice,borrowPrice,otherPrice;
    private Spinner spinner_year,spinner_month;
    private PieChart.OnItemClickListener listener;
    private ArrayAdapter<CharSequence> adapter_month,adapter_year;
    private TextView title;
    private PieChart pieChart;
    private String[] colors = {"#ffFF00","#421010","#20B2AA","#FFB1B0","#020006","#0e04a1"};
    private int year,month;
    private String start,end;
    private int life_num = 0,credit_num = 0,redPacket_num = 0,reward_num = 0,borrow_num = 0,other_num = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_statistic);

        initView();
        setClickListener();
        setSpinner();

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        if (month==12) spinner_month.setSelection(0,true);
        else spinner_month.setSelection(month,true);
        spinner_year.setSelection(year-2015,true);
        month=month+1;
        if (month==13) month = 1;

        initMsg(year,month);
        getMsgFromBmob();
    }

    private void setSpinner() {
        adapter_month = ArrayAdapter.createFromResource(this,R.array.place,R.layout.support_simple_spinner_dropdown_item);
        adapter_year = ArrayAdapter.createFromResource(this, R.array.year, R.layout.support_simple_spinner_dropdown_item);
        adapter_month.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        adapter_year.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_month.setAdapter(adapter_month);
        spinner_year.setAdapter(adapter_year);
    }

    private void setClickListener() {
        spinner_year.setOnItemSelectedListener(this);
        spinner_month.setOnItemSelectedListener(this);
        life_money.setOnClickListener(this);
        credit.setOnClickListener(this);
        reward.setOnClickListener(this);
        red_packet.setOnClickListener(this);
        borrow_money.setOnClickListener(this);
        other.setOnClickListener(this);
    }

    private void initView() {
        life_money = (LinearLayout) findViewById(R.id.life_money);
        credit = (LinearLayout) findViewById(R.id.credit);
        red_packet = (LinearLayout) findViewById(R.id.red_packet);
        reward = (LinearLayout) findViewById(R.id.reward);
        borrow_money = (LinearLayout) findViewById(R.id.borrow_money);
        other = (LinearLayout) findViewById(R.id.other);
        pieChart = (PieChart) findViewById(R.id.pieChat);
        title = (TextView) findViewById(R.id.title_activity);
        title.setText("资产查询");
        spinner_month = (Spinner) findViewById(R.id.spinner_month);
        spinner_year = (Spinner) findViewById(R.id.spinner_year);

        lifePrice = (TextView) findViewById(R.id.priceOfLife);
        repayPrice = (TextView) findViewById(R.id.priceOfRepay);
        redPacketPrice = (TextView) findViewById(R.id.priceOfRedPacket);
        rewardPrice = (TextView) findViewById(R.id.priceOfReward);
        borrowPrice = (TextView) findViewById(R.id.priceOfBorrow);
        otherPrice = (TextView) findViewById(R.id.priceOfOther);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        year = spinner_year.getSelectedItemPosition()+2015;
        month = spinner_month.getSelectedItemPosition()+1;
        initMsg(year, month);
        getMsgFromBmob();
        //Toast.makeText(ShowStatisticActivity.this,"hhh"+year+month,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(ShowStatisticActivity.this,ShowDetailActivity.class);
        Bundle bundle = new Bundle();
        switch (v.getId()){
            case R.id.life_money:
                bundle.putInt("show",0);
                break;
            case R.id.credit:
                bundle.putInt("show",1);
                break;
            case R.id.red_packet:
                bundle.putInt("show",2);
                break;
            case R.id.reward:
                bundle.putInt("show",3);
                break;
            case R.id.borrow_money:
                bundle.putInt("show",4);
                break;
            case R.id.other:
                bundle.putInt("show", 5);
                break;
        }
        bundle.putInt("year",year);
        bundle.putInt("month",month);
        intent.putExtras(bundle);
        startActivity(intent);
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

    public void getMsgFromBmob() {
        BmobQuery<IncomeBean> query = new BmobQuery<IncomeBean>();
        List<BmobQuery<IncomeBean>> and = new ArrayList<BmobQuery<IncomeBean>>();
        BmobQuery<IncomeBean> q1 = new BmobQuery<IncomeBean>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date data = null;
        try {
            data = sdf.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q1.addWhereGreaterThanOrEqualTo("createdAt",new BmobDate(data));
        and.add(q1);

        BmobQuery<IncomeBean> q2 = new BmobQuery<IncomeBean>();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1  = null;
        try {
            date1 = sdf1.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q2.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date1));
        and.add(q2);

        query.and(and);
        query.findObjects(new FindListener<IncomeBean>() {
            @Override
            public void done(List<IncomeBean> list, BmobException e) {
                setPieChart(list);
            }
        });
    }

    private void setPieChart(List<IncomeBean> list) {
        if (list != null) {
            life_num=0;
            borrow_num = 0;
            credit_num = 0;
            other_num = 0;
            reward_num = 0;
            redPacket_num = 0;

            for (int i = 0; i < list.size(); i++) {
                IncomeBean income = list.get(i);
                if (income.getThing().equals("生活费")) {
                    life_num = life_num + Integer.parseInt(income.getPrice());
                } else if (income.getThing().equals("工资")) {
                    credit_num = credit_num + Integer.parseInt(income.getPrice());
                } else if (income.getThing().equals("红包")) {
                    redPacket_num = redPacket_num + Integer.parseInt(income.getPrice());
                } else if (income.getThing().equals("奖金")) {
                    reward_num = reward_num + Integer.parseInt(income.getPrice());
                }else if (income.getThing().equals("借款")) {
                    borrow_num = borrow_num + Integer.parseInt(income.getPrice());
                }else if (income.getThing().equals("其他")) {
                    other_num = other_num + Integer.parseInt(income.getPrice());
                }
            }
            float[] item = new float[]{life_num, credit_num, redPacket_num, reward_num,borrow_num,other_num};

            pieChart.setRadius(150);
            pieChart.setItem(item);
            pieChart.initSrc(item, colors, listener);
            pieChart.notifyDraw();

            lifePrice.setText(life_num+"元");
            repayPrice.setText(credit_num+"元");
            redPacketPrice.setText(redPacket_num+"元");
            rewardPrice.setText(reward_num+"元");
            borrowPrice.setText(borrow_num+"元");
            otherPrice.setText(other_num+"元");
        } else {
            Toast.makeText(ShowStatisticActivity.this, "本月没收入数据哦", Toast.LENGTH_SHORT).show();
        }
    }
}
