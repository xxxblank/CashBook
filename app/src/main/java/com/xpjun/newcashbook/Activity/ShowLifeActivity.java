package com.xpjun.newcashbook.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xpjun.newcashbook.R;
import com.xpjun.newcashbook.model.adapter.MyAdapterOfExpenditure;
import com.xpjun.newcashbook.model.ExpenditureBean;
import com.xpjun.newcashbook.model.IncomeBean;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by U-nookia on 2016/8/20.
 */
public class ShowLifeActivity extends Activity {
    private ListView listView;
    private MyAdapterOfExpenditure adapterOfExpenditure;
    private MyAdapter adapterOfIncome;
    private List<IncomeBean> mdata_income;
    private List<ExpenditureBean> mdata_expenditure;
    private TextView title;
    int key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        listView = (ListView) findViewById(R.id.listView);
        title = (TextView) findViewById(R.id.add_list_title_activity);
        title.setText("人事明细");
        Intent intent = getIntent();
        key = intent.getIntExtra("key",1);
        if (key==0){
            BmobQuery<ExpenditureBean> query = new BmobQuery<ExpenditureBean>();
            query.addWhereEqualTo("thing","其他");
            query.setLimit(1000);
            query.findObjects(new FindListener<ExpenditureBean>() {
                @Override
                public void done(List<ExpenditureBean> list, BmobException e) {
                    mdata_expenditure = list;
                    adapterOfExpenditure = new MyAdapterOfExpenditure(ShowLifeActivity.this,mdata_expenditure);
                    listView.setAdapter(adapterOfExpenditure);
                }
            });
        }else if (key==1){
            BmobQuery<IncomeBean> query = new BmobQuery<IncomeBean>();
            query.addWhereEqualTo("thing","其他");
            query.setLimit(1000);
            query.findObjects(new FindListener<IncomeBean>() {
                @Override
                public void done(List<IncomeBean> list, BmobException e) {
                    mdata_income = list;
                    adapterOfIncome = new MyAdapter(ShowLifeActivity.this,mdata_income);
                    listView.setAdapter(adapterOfIncome);
                }
            });
        }else if (key==2){
            BmobQuery<IncomeBean> query = new BmobQuery<IncomeBean>();
            query.addWhereEqualTo("thing","工资");
            query.setLimit(1000);
            query.findObjects(new FindListener<IncomeBean>() {
                @Override
                public void done(List<IncomeBean> list, BmobException e) {
                    mdata_income = list;
                    adapterOfIncome = new MyAdapter(ShowLifeActivity.this,mdata_income);
                    listView.setAdapter(adapterOfIncome);
                }
            });
        }

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (key==0){
                    new AlertDialog.Builder(ShowLifeActivity.this).setTitle("提示")
                            .setMessage("是否删除该项")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String id = mdata_expenditure.get(position).getObjectId();
                                    ExpenditureBean expenditure = new ExpenditureBean();
                                    expenditure.setObjectId(id);
                                    expenditure.delete(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                Toast.makeText(ShowLifeActivity.this, "已删除该项目", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(ShowLifeActivity.this, "删除失败" + e, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    mdata_expenditure.remove(position);
                                    adapterOfExpenditure.notifyDataSetChanged();
                                }
                            }).setCancelable(true).show();
                }else if (key==1||key==2){
                    new AlertDialog.Builder(ShowLifeActivity.this).setTitle("提示")
                            .setMessage("是否删除该项")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String id = mdata_income.get(position).getObjectId();
                                    IncomeBean income = new IncomeBean();
                                    income.setObjectId(id);
                                    income.delete(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                Toast.makeText(ShowLifeActivity.this, "已删除该项目", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(ShowLifeActivity.this, "删除失败" + e, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    mdata_income.remove(position);
                                    adapterOfIncome.notifyDataSetChanged();
                                }
                            }).setCancelable(true).show();
                }
                return false;
            }
        });
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
