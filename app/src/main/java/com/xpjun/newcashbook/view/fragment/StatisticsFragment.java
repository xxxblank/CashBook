package com.xpjun.newcashbook.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.xpjun.newcashbook.Activity.LogInActivity;
import com.xpjun.newcashbook.Activity.ShowDetailExpenActivity;
import com.xpjun.newcashbook.Activity.ShowStatisticActivity;
import com.xpjun.newcashbook.R;
import com.xpjun.newcashbook.model.ExpenditureBean;
import com.xpjun.newcashbook.view.custom.PieChart;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 统计fragment
 */
public class StatisticsFragment extends Fragment implements View.OnClickListener{
    private RelativeLayout quit,asset;
    private LinearLayout detail_eat,detail_play,detail_life,detail_medital,detail_borrow,detail_credit,detail_other;
    private TextView eatPrice,playPrice,lifePrice,medicalPrice,borrowPrice,creditPrice,otherPrice;
    private PieChart pieChart;
    private String[] colors = {"#ffFF00", "#6A5ACD", "#20B2AA", "#00BFFF","#ff000c","#ff007f","#020006","#0e04a1","#ff5e00","#E26F1C","#D5ED15","#988EA6"};
    private PieChart.OnItemClickListener listener;
    private int year,month,date;
    private String start,end;
    private LocalBroadcastManager manager;
    private BroadcastReceiver receiver;
    private int life=1,play=1,eatAndDrink=1,medical=1,son = 1,husband = 1,loan = 1,credit = 1,other = 1,KTV = 1,haveDinnerTogether = 1,C = 1;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        manager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.CASH_BROADCAST");
        Calendar calendar = Calendar.getInstance();
        date = calendar.get(calendar.DAY_OF_MONTH);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                year = intent.getIntExtra("year",-1);
                month = intent.getIntExtra("month",-1);
                initMsg(year,month,date);
                getMsgFromBmob();
            }
        };
        manager.registerReceiver(receiver, filter);
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics_layout, container, false);
        pieChart = (PieChart) view.findViewById(R.id.pieChat);
        quit = (RelativeLayout) view.findViewById(R.id.quit);
        asset = (RelativeLayout) view.findViewById(R.id.asset);
        detail_eat = (LinearLayout) view.findViewById(R.id.detail_eat);
        detail_borrow = (LinearLayout) view.findViewById(R.id.detail_borrow);
        detail_credit = (LinearLayout) view.findViewById(R.id.detail_credit);
        detail_life = (LinearLayout) view.findViewById(R.id.detail_life);
        detail_medital = (LinearLayout) view.findViewById(R.id.detail_medital);
        detail_play = (LinearLayout) view.findViewById(R.id.detail_play);
        detail_other = (LinearLayout) view.findViewById(R.id.detail_other);
        eatPrice = (TextView) view.findViewById(R.id.priceOfEat);
        lifePrice = (TextView) view.findViewById(R.id.priceOfLife);
        playPrice = (TextView) view.findViewById(R.id.priceOfPlay);
        borrowPrice = (TextView) view.findViewById(R.id.priceOfBorrow);
        creditPrice = (TextView) view.findViewById(R.id.priceOfCredit);
        medicalPrice = (TextView) view.findViewById(R.id.priceOfMedical);
        otherPrice = (TextView) view.findViewById(R.id.priceOfOther);
        Calendar calendar = Calendar.getInstance();
        date = calendar.get(calendar.DAY_OF_MONTH);
        month = calendar.get(calendar.MONTH)+1;
        if (month==13) month=1;
        year = calendar.get(calendar.YEAR);
        initMsg(year, month, date);

        getMsgFromBmob();

        quit.setOnClickListener(this);
        asset.setOnClickListener(this);
        detail_eat.setOnClickListener(this);
        detail_borrow.setOnClickListener(this);
        detail_credit.setOnClickListener(this);
        detail_life.setOnClickListener(this);
        detail_medital.setOnClickListener(this);
        detail_play.setOnClickListener(this);
        detail_other.setOnClickListener(this);
        pieChart.setOnClickListener(this);

        return view;
    }

    public void getMsgFromBmob() {
        BmobQuery<ExpenditureBean> query = new BmobQuery<ExpenditureBean>();
        List<BmobQuery<ExpenditureBean>> and = new ArrayList<BmobQuery<ExpenditureBean>>();
        BmobQuery<ExpenditureBean> q1 = new BmobQuery<ExpenditureBean>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date data = null;
        try {
            data = sdf.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q1.addWhereGreaterThanOrEqualTo("createdAt",new BmobDate(data));
        and.add(q1);

        BmobQuery<ExpenditureBean> q2 = new BmobQuery<ExpenditureBean>();
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
        query.findObjects(new FindListener<ExpenditureBean>() {
            @Override
            public void done(List<ExpenditureBean> list, BmobException e) {
                setPieChart(list);
            }
        });
    }

    private void setPieChart(List<ExpenditureBean> list) {
        if (list != null) {
            eatAndDrink=0;
            play = 0;
            life = 0;
            medical = 0;
            credit = 0;
            loan = 0;
            son = 0;
            husband = 0;
            other = 0;
            C = 0;
            KTV = 0;
            haveDinnerTogether = 0;
            for (int i = 0; i < list.size(); i++) {
                ExpenditureBean expenditure = list.get(i);
                if (expenditure.getThing().equals("饮食")) {
                    eatAndDrink = eatAndDrink + Integer.parseInt(expenditure.getPrice());
                } else if (expenditure.getThing().equals("电影")||expenditure.getThing().equals("KTV")||expenditure.getThing().equals("聚餐")) {
                    play = play + Integer.parseInt(expenditure.getPrice());
                } else if (expenditure.getThing().equals("衣服") || expenditure.getThing().equals("交通") || expenditure.getThing().equals("话费") || expenditure.getThing().equals("果蔬") || expenditure.getThing().equals("生活用品")) {
                    life = life + Integer.parseInt(expenditure.getPrice());
                } else if (expenditure.getThing().equals("医疗")) {
                    medical = medical + Integer.parseInt(expenditure.getPrice());
                }else if (expenditure.getThing().equals("儿子")) {
                    son = son + Integer.parseInt(expenditure.getPrice());
                }else if (expenditure.getThing().equals("丈夫")) {
                    husband = husband + Integer.parseInt(expenditure.getPrice());
                }else if (expenditure.getThing().equals("借出")) {
                    loan = loan + Integer.parseInt(expenditure.getPrice());
                }else if (expenditure.getThing().equals("还贷")) {
                    credit = credit + Integer.parseInt(expenditure.getPrice());
                }else if (expenditure.getThing().equals("其他")||expenditure.getThing().equals("C")) {
                    other = other + Integer.parseInt(expenditure.getPrice());
                }
            }
            float[] item = new float[]{eatAndDrink, play, life, medical,son,husband,loan,credit,other};

            pieChart.setRadius(150);
            pieChart.setItem(item);
            pieChart.initSrc(item, colors, listener);
            pieChart.notifyDraw();

            eatPrice.setText(eatAndDrink + "元");
            playPrice.setText(play+"元");
            lifePrice.setText(life+"元");
            medicalPrice.setText(medical+"元");
            borrowPrice.setText(loan+"元");
            creditPrice.setText(credit+"元");
            otherPrice.setText(other+"元");
        } else {
            Toast.makeText(getContext(), "今日还没有消费记录哦", Toast.LENGTH_SHORT).show();
        }
    }

    private void initMsg(int year,int month,int date) {
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

    @Override
    public void onClick(View v) {
        Intent intent2 = new Intent(getContext(),ShowDetailExpenActivity.class);
        Bundle bundle = new Bundle();
        switch (v.getId()){
            case R.id.quit:
                BmobUser.logOut();
                Toast.makeText(getContext(),"退出登录成功",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), LogInActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.asset:
                Intent intent1 = new Intent(getContext(), ShowStatisticActivity.class);
                intent1.putExtra("key", 2);
                startActivity(intent1);
                break;
            case R.id.detail_eat:
                bundle.putInt("show",0);
                bundle.putInt("year",year);
                bundle.putInt("month",month);
                intent2.putExtras(bundle);
                startActivity(intent2);
                break;
            case R.id.detail_play:
                bundle.putInt("show",1);
                bundle.putInt("year",year);
                bundle.putInt("month",month);
                intent2.putExtras(bundle);
                startActivity(intent2);
                break;
            case R.id.detail_life:
                bundle.putInt("show",2);
                bundle.putInt("year",year);
                bundle.putInt("month",month);
                intent2.putExtras(bundle);
                startActivity(intent2);
                break;
            case R.id.detail_medital:
                bundle.putInt("show",3);
                bundle.putInt("year",year);
                bundle.putInt("month",month);
                intent2.putExtras(bundle);
                startActivity(intent2);
                break;
            case R.id.detail_borrow:
                bundle.putInt("show",4);
                bundle.putInt("year",year);
                bundle.putInt("month",month);
                intent2.putExtras(bundle);
                startActivity(intent2);
                break;
            case R.id.detail_credit:
                bundle.putInt("show",5);
                bundle.putInt("year",year);
                bundle.putInt("month",month);
                intent2.putExtras(bundle);
                startActivity(intent2);
                break;
            case R.id.detail_other:
                bundle.putInt("show",6);
                bundle.putInt("year",year);
                bundle.putInt("month",month);
                intent2.putExtras(bundle);
                startActivity(intent2);
                break;
        }
    }
}
