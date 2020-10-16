package com.media.mediaplayer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    static ArrayList<Songs> song;
    Cursor musicCursor;
    ListView songView;
    MediaController controller;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getSongList();
            Collections.sort(song, new Comparator<Songs>() {
                @Override
                public int compare(Songs o1, Songs o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            SongAdapter songAdapter = new SongAdapter(this, song);
            songView.setAdapter(songAdapter);
        } else {
            Toast.makeText(this, "Permission Not Given", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songView = findViewById(R.id.song_list);
        song = new ArrayList<Songs>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                getSongList();
                Collections.sort(song, new Comparator<Songs>() {
                    @Override
                    public int compare(Songs o1, Songs o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                SongAdapter songAdapter = new SongAdapter(this, song);
                songView.setAdapter(songAdapter);
            }
        }

    }

    private void getSongList() {
        ContentResolver musicResolver = getContentResolver();
        Uri audiouri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        musicCursor = musicResolver.query(audiouri, null, null, null, null);
        System.out.println(musicCursor.getCount());
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                Songs s = new Songs(thisId, thisTitle, thisArtist);
                song.add(s);
            } while (musicCursor.moveToNext());
        }
    }

    public void songPicked(View view) {
        Intent i = new Intent(this,PlaySong.class);
        int s = Integer.parseInt(view.getTag().toString());
        Songs s1 = song.get(s);
        String a = s1.getName();
        i.putExtra("Name", a);
        i.putExtra("Tag", view.getTag().toString());
        //Log.i("Name: ",a);
        startActivity(i);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}