package com.ethwillz.ethan.easeofuse;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends BaseAdapter {
    List<ProductInformation> items;
    private Context mContext;

    public GridAdapter(Context context, ArrayList<ProductInformation> items){
        mContext = context;
        this.items = items;
    }

    public int getCount(){
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ImageView pic;

        if(view == null){
            pic = new ImageView(mContext);
            pic.setLayoutParams(new GridView.LayoutParams(250, 250));
        }
        else
            pic = (ImageView) view;

        Picasso.with(mContext).load(items.get(i).getImageUrl()).placeholder(R.drawable.logo).into(pic);
        return pic;
    }
}
