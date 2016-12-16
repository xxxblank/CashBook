package com.xpjun.newcashbook.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xpjun.newcashbook.R;
import com.xpjun.newcashbook.model.IncomeBean;

import java.util.List;

/**
 * Created by U-nookia on 2016/8/24.
 */
public class MyShowDetailsAdapter extends BaseAdapter {
    private List<IncomeBean> list;
    private Context context;
    private LayoutInflater inflater;

    public MyShowDetailsAdapter(List<IncomeBean> list, Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView==null){
            convertView = inflater.inflate(R.layout.activity_show_list_item,null);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.detail_img);
            holder.time = (TextView) convertView.findViewById(R.id.detail_time);
            holder.thing = (TextView) convertView.findViewById(R.id.detail_thing);
            holder.price = (TextView) convertView.findViewById(R.id.detail_price);
            holder.content = (TextView) convertView.findViewById(R.id.detail_content);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.time.setText(list.get(position).getCreatedAt());
        holder.thing.setText(list.get(position).getThing()+":");
        holder.price.setText(list.get(position).getPrice()+"元");
        holder.content.setText(list.get(position).getNote());

        String thing = list.get(position).getThing();
        if (thing.equals("生活费")){
            holder.img.setImageResource(R.drawable.life);
        }else if (thing.equals("工资")){
            holder.img.setImageResource(R.drawable.repay);
        }else if (thing.equals("红包")){
            holder.img.setImageResource(R.drawable.redpacket);
        }else if (thing.equals("奖金")){
            holder.img.setImageResource(R.drawable.reward);
        }else if (thing.equals("借款")){
            holder.img.setImageResource(R.drawable.borrow);
        }else if (thing.equals("其他")){
            holder.img.setImageResource(R.drawable.other);
        }

        return convertView;
    }

    public class ViewHolder{
        private ImageView img;
        private TextView time,thing,price,content;
    }
}
