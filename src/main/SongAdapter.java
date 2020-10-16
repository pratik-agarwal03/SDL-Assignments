package com.media.mediaplayer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SongAdapter extends BaseAdapter{
    ArrayList<Songs> song;
    LayoutInflater songInf;

    public SongAdapter(Context context,ArrayList<Songs> song){
        this.song = song;
        songInf = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return song.size();
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
        LinearLayout songLay = (LinearLayout)songInf.inflate(R.layout.row_list,parent,false);
        TextView songView = (TextView)songLay.findViewById(R.id.song_title);
        TextView artistView = (TextView)songLay.findViewById(R.id.song_artist);
        Songs currSong = song.get(position);
        songView.setText(currSong.getName());
        artistView.setText(currSong.getArtist());
        songLay.setTag(position);
        return songLay;
    }
}