package com.xpjun.newcashbook.view.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.xpjun.newcashbook.Activity.AddListActivity;
import com.xpjun.newcashbook.Activity.ShowCreditActivity;
import com.xpjun.newcashbook.Activity.ShowLifeActivity;
import com.xpjun.newcashbook.R;
import com.xpjun.newcashbook.model.adapter.MyAdapterOfExpenditure;
import com.xpjun.newcashbook.model.ExpenditureBean;
import com.xpjun.newcashbook.presenter.ExpenditurePresenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 支出页面的fragment
 */
public class ExpenditureFragment extends Fragment {

    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.data_list_expenditure)
    ListView listView_data;
    @Bind(R.id.write)
    RelativeLayout write;
    @Bind(R.id.people)
    RelativeLayout people;
    @Bind(R.id.borrow_out)
    RelativeLayout borrow_out;

    private ArrayAdapter adapter_data;
    private MyAdapterOfExpenditure adapter;
    private List<String> list_data;
    private List<ExpenditureBean> mdata;
    private int year,month,date;
    private LocalBroadcastManager manager;
    private BroadcastReceiver receiver;
    private String start,end;

    private ExpenditurePresenter presenter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        presenter = new ExpenditurePresenter();
        initYearMonthAndData();
        registerReceiver();
        super.onActivityCreated(savedInstanceState);
    }

    private void registerReceiver() {
        manager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.CASH_BROADCAST");
        receiver = new MyReceiver();
        manager.registerReceiver(receiver, filter);
    }

    private void initYearMonthAndData() {
        Calendar calendar = Calendar.getInstance();
        date = calendar.get(calendar.DAY_OF_MONTH);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        if (month==13) month=1;
    }

    public class MyReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            year = intent.getIntExtra("year",-1);
            month = intent.getIntExtra("month",-1);
            initMsg(year, month, date);
            try {
                getMsgFromBmob();
            } catch (ParseException e) {
                Toast.makeText(getContext(),"获取信息失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenditure_layout, container, false);
        ButterKnife.bind(this,view);

        listView.setEmptyView(view.findViewById(R.id.img_background));
        initMsg(year, month,date);
        try {
            getMsgFromBmob();
        } catch (ParseException e) {
            Toast.makeText(getContext(),"获取信息失败",Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    public void getMsgFromBmob() throws ParseException{
        BmobQuery<ExpenditureBean> query = new BmobQuery<ExpenditureBean>();
        List<BmobQuery<ExpenditureBean>> and = new ArrayList<BmobQuery<ExpenditureBean>>();
        BmobQuery<ExpenditureBean> q1 = new BmobQuery<ExpenditureBean>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date data = null;
        data = sdf.parse(start);
        q1.addWhereGreaterThanOrEqualTo("createdAt",new BmobDate(data));
        and.add(q1);

        BmobQuery<ExpenditureBean> q2 = new BmobQuery<ExpenditureBean>();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1  = null;
        date1 = sdf1.parse(end);
        q2.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date1));
        and.add(q2);

        query.and(and);
        query.findObjects(new FindListener<ExpenditureBean>() {
            @Override
            public void done(List<ExpenditureBean> list, BmobException e) {
                if (list!=null){
                    mdata = list;
                    adapter = new MyAdapterOfExpenditure(getContext(), mdata);
                    listView.setAdapter(adapter);
                }else {
                    Toast.makeText(getContext(),"今日还没有消费记录哦",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initMsg(int year,int month,int date) {
        list_data = new ArrayList<>();
        list_data = getListFromYearAndMonth(year, month);
        adapter_data = new ArrayAdapter(getContext(),R.layout.list_data_item,list_data);
        listView_data.setAdapter(adapter_data);

        setStartAndEndForBmob(year,month,date);
    }

    private void setStartAndEndForBmob(int year, int month, int date) {
        if (month<10&&date<10){
            start = year + "-0" + month + "-0" + date + " " + "00:00:00";
            end = year + "-0" + month + "-0" + date + " " + "23:59:59";
        }else if (month<10&&date>10){
            start = year + "-0" + month + "-" + date + " " + "00:00:00";
            end = year + "-0" + month + "-" + date + " " + "23:59:59";
        }else if (month>10&&date<10){
            start = year + "-" + month + "-0" + date + " " + "00:00:00";
            end = year + "-" + month + "-0" + date + " " + "23:59:59";
        }else {
            start = year + "-" + month + "-" + date + " " + "00:00:00";
            end = year + "-" + month + "-" + date + " " + "23:59:59";
        }
    }

    private List<String> getListFromYearAndMonth(int year, int month) {
        if (year%4==0&&year%100!=0 || year % 400 ==0){
            if (month==1||month==3||month==5||month==7||month==8||month==10||month==12){
                addList(32);
            }else if (month==4||month == 6||month == 9 ||month==11){
                addList(31);
            }else if (month==2){
                addList(30);
            }
        }else {
            if (month==1||month==3||month==5||month==7||month==8||month==10||month==12){
                addList(32);
            }else if (month==4||month == 6||month == 9 ||month==11){
                addList(31);
            }else if (month==2){
                addList(29);
            }
        }
        return list_data;
    }

    @Override
    public void onDestroy() {
        manager.unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void addList(int n) {
        for (int i = 1;i<n;i++){
            list_data.add(i+"日");
        }
    }

    @OnItemLongClick(R.id.listView)
    public boolean longClickList(final int position, long id){
        new AlertDialog.Builder(getContext()).setTitle("提示")
                .setMessage("是否删除该项")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteData(position);
                    }
                }).setCancelable(true).show();
        return false;
    }

    private void deleteData(final int position) {
        String id = mdata.get(position).getObjectId();
        ExpenditureBean expenditure = new ExpenditureBean();
        expenditure.setObjectId(id);
        expenditure.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(getContext(), "已删除该项目", Toast.LENGTH_SHORT).show();
                    mdata.remove(position);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "删除失败" + e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @OnItemClick(R.id.data_list_expenditure)
    public void clickListData(int position, long id){
        initMsg(year,month,position+1);
        try {
            getMsgFromBmob();
        } catch (ParseException e) {
            Toast.makeText(getContext(),"获取信息失败",Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.write)
    public void clickWrite(){
        Intent intent  = new Intent(getContext(), AddListActivity.class);
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.people)
    public void clickPeople(){
        Intent intent1 = new Intent(getContext(), ShowLifeActivity.class);
        intent1.putExtra("key",0);
        startActivity(intent1);
    }

    @OnClick(R.id.borrow_out)
    public void click_borrow(){
        Intent intent2 = new Intent(getContext(), ShowCreditActivity.class);
        intent2.putExtra("key",0);
        startActivity(intent2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode==1){
                ExpenditureBean expenditure = new ExpenditureBean();
                expenditure.setThing((String) data.getCharSequenceExtra("thing"));
                expenditure.setPrice((String) data.getCharSequenceExtra("price"));
                expenditure.setNote((String) data.getCharSequenceExtra("note"));
                if (mdata!=null){
                    mdata.add(expenditure);
                    adapter.notifyDataSetChanged();
                }else {
                    try {
                        getMsgFromBmob();
                    } catch (ParseException e) {
                        Toast.makeText(getContext(),"获取信息失败",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
