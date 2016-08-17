package com.example.questdot.audioplayerexample;

import android.app.ProgressDialog;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    static final String AUDIO_PATH =
            "http://www.tonycuffe.com/mp3/tail%20toddle.mp3";
    private MediaPlayer mediaPlayer  = new MediaPlayer();
    private Button btnPlay,btnStop,btnPause,btnRestart;
    private int playbackPosition=0;
    ProgressBar pb;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb = (ProgressBar)findViewById(R.id.progressBar);
        btnPlay = (Button)findViewById(R.id.btnPlay);
        btnStop = (Button)findViewById(R.id.btnStop);
        btnPause = (Button)findViewById(R.id.btnPause);
        btnRestart = (Button)findViewById(R.id.btnRestart);

        btnPlay.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnRestart.setOnClickListener(this);



    }

    private void playAudio(String url) throws Exception
    {
        killMediaPlayer();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Buffering.....");
        progressDialog.show();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setDataSource(url);
      //  mediaPlayer.prepare();
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnCompletionListener(this);
       // mediaPlayer.start();

        //pb.setVisibility(View.VISIBLE);
    }

    private void playLocalAudio() throws Exception
    {
        mediaPlayer = MediaPlayer.create(this, R.raw.pokemon);
        mediaPlayer.start();
    }

    private void playLocalAudio_UsingDescriptor() throws Exception {

        AssetFileDescriptor fileDesc = getResources().openRawResourceFd(
                R.raw.pokemon);
        if (fileDesc != null) {

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(fileDesc.getFileDescriptor(), fileDesc
                    .getStartOffset(), fileDesc.getLength());

            fileDesc.close();

            mediaPlayer.prepare();
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        killMediaPlayer();
    }

    private void killMediaPlayer() {
        if(mediaPlayer!=null) {
            try {
                mediaPlayer.release();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPlay:
                try {

                   // pb.setVisibility(View.VISIBLE);
                    playAudio(AUDIO_PATH);
                     //playLocalAudio();
                     // playLocalAudio_UsingDescriptor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnPause:
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    playbackPosition = mediaPlayer.getCurrentPosition();
                    mediaPlayer.pause();
                }
                break;
            case R.id.btnRestart:
                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(playbackPosition);
                    mediaPlayer.start();
                }
                break;
            case R.id.btnStop:
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    playbackPosition = 0;
                }
                break;
        }
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        progressDialog.dismiss();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
       // Log.i("StreamAudioDemo", "prepare finished");
        progressDialog.setMessage("Playing.....");
        mediaPlayer.start();

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        progressDialog.dismiss();
    }
}
