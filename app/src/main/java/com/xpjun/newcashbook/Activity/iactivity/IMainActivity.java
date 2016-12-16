package com.xpjun.newcashbook.Activity.iactivity;

import android.support.v4.view.ViewPager;
import android.widget.ImageView;

/**
 * Created by U-nookia on 2016/11/23.
 */

public interface IMainActivity {

    void setCalendarToday();

    void setYearSpinner();

    void setMonthSpinner();

    ViewPager getViewPager();

    ImageView getExpenditure_img();

    ImageView getIncome_img();

    ImageView getStatistic_img();

    void initFragments();
}
