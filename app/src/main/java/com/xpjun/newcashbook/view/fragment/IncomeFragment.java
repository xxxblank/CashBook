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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xpjun.newcashbook.Activity.AddIncomeActivity;
import com.xpjun.newcashbook.Activity.ShowCreditActivity;
import com.xpjun.newcashbook.Activity.ShowLifeActivity;
import com.xpjun.newcashbook.R;
import com.xpjun.newcashbook.Util.DateUtil;
import com.xpjun.newcashbook.model.IncomeBean;

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
import cn.bmob.v3.listener.UpdateListener;

/**
 * 收入fragment
 */
public class IncomeFragment extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener{
    private ListView listView_data,listView;
    private ArrayAdapter adapter_data;
    private MyAdapter adapter;
    private List<String> list_data;
    private List<IncomeBean> mdata;
    private int year,month,date;
    private boolean flag = false;
    private LocalBroadcastManager manager;
    private BroadcastReceiver receiver;
    private RelativeLayout write,people,borrow_in;
    private String start,end;

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
        View view = inflater.inflate(R.layout.fragment_income_layout, container, false);
        listView_data = (ListView) view.findViewById(R.id.data_list_income);
        write = (RelativeLayout) view.findViewById(R.id.write);
        people = (RelativeLayout) view.findViewById(R.id.people);
        borrow_in = (RelativeLayout) view.findViewById(R.id.borrow_in);
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setEmptyView(view.findViewById(R.id.img_background));
        write.setOnClickListener(this);
        people.setOnClickListener(this);
        borrow_in.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        if (month==13) month=1;
        //date = calendar.get(calendar.DAY_OF_MONTH);

        initMsg(year, month, date);
        getMsgFromBmob();

        listView_data.setOnItemClickListener(this);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getContext()).setTitle("提示")
                        .setMessage("是否删除该项")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String id = mdata.get(position).getObjectId();
                                IncomeBean income = new IncomeBean();
                                income.setObjectId(id);
                                income.delete(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            Toast.makeText(getContext(), "已删除该项目", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), "删除失败" + e, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                mdata.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        }).setCancelable(true).show();
                return false;
            }
        });

        return view;
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
                if (list != null) {
                    mdata = list;
                    adapter = new MyAdapter(getContext(), mdata);
                    listView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "今日还没有收入记录哦", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void initMsg(int year,int month,int date) {
        list_data = new ArrayList<>();
        list_data = DateUtil.getListFromYearAndMonth(year, month);
        adapter_data = new ArrayAdapter(getContext(),R.layout.list_data_item,list_data);
        listView_data.setAdapter(adapter_data);

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

    @Override
    public void onDestroy() {
        manager.unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.write:
                Intent intent  = new Intent(getContext(), AddIncomeActivity.class);
                startActivityForResult(intent, 2);
                break;
            case R.id.people:
                Intent intent1 = new Intent(getContext(), ShowLifeActivity.class);
                intent1.putExtra("key",1);
                startActivity(intent1);
                break;
            case R.id.borrow_in:
                Intent intent2 = new Intent(getContext(), ShowCreditActivity.class);
                intent2.putExtra("key",1);
                startActivity(intent2);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        initMsg(year,month,position+1);
        getMsgFromBmob();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode==2){
                IncomeBean income = new IncomeBean();
                income.setThing((String) data.getCharSequenceExtra("thing"));
                income.setPrice((String) data.getCharSequenceExtra("price"));
                income.setNote((String) data.getCharSequenceExtra("note"));
                if (mdata!=null){
                    mdata.add(income);
                    adapter.notifyDataSetChanged();
                }else {
                    getMsgFromBmob();
                }
                flag = true;
            }
        }
    }

    public class MyAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private Context context;
        private List<IncomeBean> mdata;

        public MyAdapter(Context context, List<IncomeBean> mdata) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.mdata = mdata;
        }

        @Override
        public int getCount() {
            return mdata.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null){
                convertView = inflater.inflate(R.layout.list_item,null);
                holder = new ViewHolder();
                holder.thing = (TextView) convertView.findViewById(R.id.expenditure_thing);
                holder.price = (TextView) convertView.findViewById(R.id.expenditure_price);
                holder.note = (TextView) convertView.findViewById(R.id.expenditure_note);
                holder.img = (ImageView) convertView.findViewById(R.id.list_item_img);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.thing.setText(mdata.get(position).getThing());
            holder.price.setText(mdata.get(position).getPrice());
            holder.note.setText(mdata.get(position).getNote());

            String thing = mdata.get(position).getThing();
            if (thing.equals("工资")){
                holder.img.setImageResource(R.drawable.salary);
            }else if (thing.equals("红包")){
                holder.img.setImageResource(R.drawable.redpacket);
            }else if (thing.equals("奖金")){
                holder.img.setImageResource(R.drawable.reward);
            }else if (thing.equals("房租")){
                holder.img.setImageResource(R.drawable.family);
            }else if (thing.equals("借款")){
                holder.img.setImageResource(R.drawable.borrow);
            }else if (thing.equals("其他")){
                holder.img.setImageResource(R.drawable.other);
            }

            return convertView;
        }

        public final class ViewHolder{
            public TextView thing,price,note;
            public ImageView img;
        }
    }
}
