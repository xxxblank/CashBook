package com.xpjun.newcashbook.presenter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.widget.ArrayAdapter;

import com.xpjun.newcashbook.Activity.iactivity.IMainActivity;
import com.xpjun.newcashbook.R;
import com.xpjun.newcashbook.model.adapter.MyFragmentAdapter;
import com.xpjun.newcashbook.view.fragment.ExpenditureFragment;
import com.xpjun.newcashbook.view.fragment.IncomeFragment;
import com.xpjun.newcashbook.view.fragment.StatisticsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by U-nookia on 2016/11/23.
 */

public class MainActivityPresenter {
    private IMainActivity mainActivity;
    private Context context;

    public MainActivityPresenter(IMainActivity mainActivity, Context context) {
        this.mainActivity = mainActivity;
        this.context = context;
    }


    public ArrayAdapter<CharSequence> getYearSpinnerAdapter() {
        ArrayAdapter<CharSequence> adapter_year = ArrayAdapter.createFromResource(context, R.array.year,R.layout.support_simple_spinner_dropdown_item);
        adapter_year.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        return adapter_year;
    }

    public ArrayAdapter<CharSequence> getMonthSpinnerAdapter() {
        ArrayAdapter<CharSequence> adapter_month = ArrayAdapter.createFromResource(context,R.array.place,R.layout.support_simple_spinner_dropdown_item);
        adapter_month.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        return adapter_month;
    }

    public void initFragments() {
        List<Fragment> fragments = new ArrayList<>();
        FragmentManager manager = ((FragmentActivity)context).getSupportFragmentManager();
        fragments.add(new ExpenditureFragment());
        fragments.add(new IncomeFragment());
        fragments.add(new StatisticsFragment());
        MyFragmentAdapter adapter = new MyFragmentAdapter(manager,fragments);
        mainActivity.getViewPager().setAdapter(adapter);
        mainActivity.getViewPager().setOnPageChangeListener(new onPageChangeListener());
    }

    public void setAllImgDefault() {
        mainActivity.getExpenditure_img().setImageResource(R.drawable.expenditure);
        mainActivity.getIncome_img().setImageResource(R.drawable.income);
        mainActivity.getStatistic_img().setImageResource(R.drawable.statistic);
    }

    private class onPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mainActivity.getExpenditure_img().setImageResource(R.drawable.expenditure);
            mainActivity.getIncome_img().setImageResource(R.drawable.income);
            mainActivity.getStatistic_img().setImageResource(R.drawable.statistic);
            switch (position){
                case 0:
                    mainActivity.getExpenditure_img().setImageResource(R.drawable.expenditure_click);
                    break;
                case 1:
                    mainActivity.getIncome_img().setImageResource(R.drawable.income_click);
                    break;
                case 2:
                    mainActivity.getStatistic_img().setImageResource(R.drawable.statistic_click);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
