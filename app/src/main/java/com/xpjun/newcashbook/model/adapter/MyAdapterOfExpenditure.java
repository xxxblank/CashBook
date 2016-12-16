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
 * Created by U-nookia on 2016/8/20.
 */
public class MyAdapterOfExpenditure extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private List<ExpenditureBean> mdata;

    public MyAdapterOfExpenditure(Context context, List<ExpenditureBean> mdata) {
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

    public final class ViewHolder{
        public TextView thing,price,note;
        public ImageView img;
    }
}
