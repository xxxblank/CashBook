package com.xpjun.newcashbook.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.xpjun.newcashbook.Activity.iactivity.IMainActivity;
import com.xpjun.newcashbook.presenter.MainActivityPresenter;
import com.xpjun.newcashbook.R;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 实现主要操作的总activity
 */

public class MainActivity extends FragmentActivity implements
        AdapterView.OnItemSelectedListener,
        IMainActivity{

    @Bind(R.id.spinner_year)
    Spinner spinner_year;
    @Bind(R.id.spinner_month)
    Spinner spinner_month;
    @Bind(R.id.expenditure)
    RelativeLayout fra_expenditure;
    @Bind(R.id.income)
    RelativeLayout fra_income;
    @Bind(R.id.statistics)
    RelativeLayout fra_statistic;
    @Bind(R.id.expenditure_img)
    ImageView expenditure_img;
    @Bind(R.id.income_img)
    ImageView income_img;
    @Bind(R.id.statistics_img)
    ImageView statistic_img;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    private MainActivityPresenter presenter;
    private Calendar calendar;
    private int year,month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
        initFragments();
        setDefaultFragment();          //设置默认第一个fragment
    }

    private void init() {
        presenter = new MainActivityPresenter(this,this);
        expenditure_img.setImageResource(R.drawable.expenditure_click);
        setCalendarToday();
        setYearSpinner();
        setMonthSpinner();
    }

    @Override
    public void setCalendarToday(){
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
    }

    @Override
    public void setYearSpinner(){
        spinner_year.setOnItemSelectedListener(this);
        spinner_year.setAdapter(presenter.getYearSpinnerAdapter());
        spinner_year.setSelection(year-2015,true);
    }

    @Override
    public void setMonthSpinner(){
        spinner_month.setOnItemSelectedListener(this);
        spinner_month.setAdapter(presenter.getMonthSpinnerAdapter());
        spinner_month.setSelection(month,true);
    }

    @Override
    public void initFragments(){
        presenter.initFragments();
    }

    @Override
    public ViewPager getViewPager(){
        return viewPager;
    }

    @Override
    public ImageView getExpenditure_img() {
        return expenditure_img;
    }

    @Override
    public ImageView getIncome_img() {
        return income_img;
    }

    @Override
    public ImageView getStatistic_img() {
        return statistic_img;
    }


    private void setDefaultFragment() {
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        year = spinner_year.getSelectedItemPosition()+2015;
        month = spinner_month.getSelectedItemPosition()+1;

        Intent intent = new Intent("android.intent.action.CASH_BROADCAST");
        intent.putExtra("year",year);
        intent.putExtra("month",month);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    @OnClick(R.id.expenditure)
    public void expenditure(){
        presenter.setAllImgDefault();
        expenditure_img.setImageResource(R.drawable.expenditure);
        viewPager.setCurrentItem(0);
    }

    @OnClick(R.id.income)
    public void income(){
        presenter.setAllImgDefault();
        income_img.setImageResource(R.drawable.income_click);
        viewPager.setCurrentItem(1);
    }

    @OnClick(R.id.statistics)
    public void statistics(){
        presenter.setAllImgDefault();
        statistic_img.setImageResource(R.drawable.statistic_click);
        viewPager.setCurrentItem(2);
    }
}
