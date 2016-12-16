package com.xpjun.newcashbook.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xpjun.newcashbook.R;
import com.xpjun.newcashbook.model.ExpenditureBean;

import java.util.List;

/**
 * Created by U-nookia on 2016/8/25.
 */
public class MyShowDetailsExpenAdapter extends BaseAdapter {
    private List<ExpenditureBean> list;
    private Context context;
    private LayoutInflater inflater;

    public MyShowDetailsExpenAdapter(List<ExpenditureBean> list, Context context) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
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
        if (thing.equals("饮食")){
            holder.img.setImageResource(R.drawable.eat);
        }else if (thing.equals("电影")){
            holder.img.setImageResource(R.drawable.film);
        }else if (thing.equals("衣服")){
            holder.img.setImageResource(R.drawable.cloth);
        }else if (thing.equals("交通")){
            holder.img.setImageResource(R.drawable.traffic);
        }else if (thing.equals("果蔬")){
            holder.img.setImageResource(R.drawable.fruit);
        }else if (thing.equals("话费")){
            holder.img.setImageResource(R.drawable.phone);
        }else if (thing.equals("借出")){
            holder.img.setImageResource(R.drawable.borrow);
        }else if (thing.equals("还贷")){
            holder.img.setImageResource(R.drawable.repay);
        }else if (thing.equals("生活")){
            holder.img.setImageResource(R.drawable.life);
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
