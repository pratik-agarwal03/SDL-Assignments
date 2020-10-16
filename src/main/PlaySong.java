package com.media.mediaplayer;

import android.content.ContentUris;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static com.media.mediaplayer.MainActivity.song;

public class PlaySong extends AppCompatActivity implements MediaController.MediaPlayerControl, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    TextView songTitle;
    int tag;
    MediaController musicController;
    MediaPlayer player;
    int songPosn;

    public void initMusicplayer() {
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void playsong() {
        player.reset();
        Songs playSong = song.get(songPosn);
        songTitle.setText(playSong.getName());
        long currSong = playSong.getId();
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);
        try {
            player.setDataSource(getApplicationContext(), trackUri);
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        player.prepareAsync();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        songTitle = findViewById(R.id.songName);
        songPosn = 0;
        String name = getIntent().getStringExtra("Name");
        String t = getIntent().getStringExtra("Tag");
        tag = Integer.parseInt(t);
        songPosn = tag;
        songTitle.setText(name);
        player = new MediaPlayer();
        setMusicController();
        initMusicplayer();
        playsong();

    }

    @Override
    public void start() {
        player.start();

    }

    @Override
    public void pause() {
        player.pause();
        musicController.hide();
    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public void seekTo(int posn) {
        player.seekTo(posn);
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    public void setMusicController() {
        musicController = new MediaController(this);
        musicController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
        musicController.setMediaPlayer(this);
        musicController.setAnchorView(findViewById(R.id.relativeSong));
        musicController.setEnabled(true);
    }

    private void playNext() {
        if (songPosn == song.size() - 1) songPosn = 0;
        else
            songPosn++;
        playsong();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void playPrev() {
        if (songPosn == 0) songPosn = song.size() - 1;
        else songPosn--;
        playsong();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        playNext();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        musicController.show(5000);
        return super.onTouchEvent(event);
    }
}