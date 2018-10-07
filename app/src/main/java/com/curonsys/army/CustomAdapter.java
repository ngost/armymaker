package com.curonsys.army;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<ContentsListModel> dataSet;
    private Bitmap setbm = null;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewVersion;
        ImageView imageViewIcon;
        LinearLayout linearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.linearLayout = itemView.findViewById(R.id.card_item_linear);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.textViewVersion = (TextView) itemView.findViewById(R.id.textViewVersion);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

    public CustomAdapter(ArrayList<ContentsListModel> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        view.setOnClickListener(ContentsChoiceFragment.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewVersion = holder.textViewVersion;
        ImageView imageView = holder.imageViewIcon;
        LinearLayout linearLayout = holder.linearLayout;

        textViewName.setText(dataSet.get(listPosition).getName());
        textViewVersion.setText(dataSet.get(listPosition).getDescribe());
        try
        {
            URL url = new URL(dataSet.get(listPosition).getImageURL());
            imageView.setImageBitmap(setBmpFromUrl(url));
        } catch(MalformedURLException e){}


        linearLayout.setBackgroundColor(Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public Bitmap setBmpFromUrl(final URL url) {

        Thread tThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URLConnection conn = url.openConnection();
                    conn.connect();
                    InputStream is = conn.getInputStream();

                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    setbm = BitmapFactory.decodeStream(is);
                    is.close();

                } catch (IOException e) {

                    Log.e("DEBUGTAG", "이 이미지는 못가져왔어", e);

                }
            }
        });

        tThread.start();
        try {
            tThread.join();
        } catch (Exception e) {
            Log.d("error", "error");
        }
        return setbm;
    }
}
