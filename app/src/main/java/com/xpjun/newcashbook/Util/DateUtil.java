package com.xpjun.newcashbook.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by U-nookia on 2016/11/24.
 */

public class DateUtil {

    public static List<String> getListFromYearAndMonth(int year, int month){
        List<String> list_data = new ArrayList<>();
        if (year%4==0&&year%100!=0 || year % 400 ==0){
            if (month==1||month==3||month==5||month==7||month==8||month==10||month==12){
                addList(list_data,32);
            }else if (month==4||month == 6||month == 9 ||month==11){
                addList(list_data,31);
            }else if (month==2){
                addList(list_data,30);
            }
        }else {
            if (month==1||month==3||month==5||month==7||month==8||month==10||month==12){
                addList(list_data,32);
            }else if (month==4||month == 6||month == 9 ||month==11){
                addList(list_data,31);
            }else if (month==2){
                addList(list_data,29);
            }
        }
        return list_data;
    }

    private static void addList(List<String> list_data, int n) {
        for (int i = 1;i<n;i++){
            list_data.add(i+"日");
        }
    }
}
