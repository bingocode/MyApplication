package com.example.admin.fisrtdemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.admin.fisrtdemo.FruitActivity;
import com.example.admin.fisrtdemo.R;
import com.example.admin.fisrtdemo.model.Fruit;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by admin on 2017/7/9.
 */

public class FruitAdapter extends RecyclerView.Adapter <FruitAdapter.ViewHolder>{
    private Context mContext;
    private List<Fruit> mFruitList;
    static  class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView fruitImage;
        TextView fruitname;
        public ViewHolder(View v){
            super(v);
            cardView=(CardView)v;
            fruitImage=(ImageView) v.findViewById(R.id.fruit_image);
            fruitname=(TextView)v.findViewById(R.id.fruit_name);
        }

    }

    public FruitAdapter(List<Fruit> fruitList){
        mFruitList=fruitList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null)
            mContext=parent.getContext();
        View view= LayoutInflater.from(mContext).inflate(R.layout.fruit_layout,parent,false);
        final  ViewHolder holder=new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Fruit fruit=mFruitList.get(position);
                Intent intent=new Intent(mContext,FruitActivity.class);
                intent.putExtra(FruitActivity.FRUIT_NAME,fruit.getName());
                intent.putExtra(FruitActivity.FRUIT_IMG_ID,fruit.getImageid());
                mContext.startActivity(intent);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Fruit fruit=mFruitList.get(position);
        holder.fruitname.setText(fruit.getName());
        Glide.with(mContext).load(fruit.getImageid()).into(holder.fruitImage);
    }

    @Override
    public int getItemCount() {
        return mFruitList.size();
    }
}
