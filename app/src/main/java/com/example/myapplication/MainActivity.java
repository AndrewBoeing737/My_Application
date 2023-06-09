package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements Runnable{
MediaPlayer mediaPlayer;
TextView textView;
boolean looping=false;
SeekBar seekBar;
FloatingActionButton floatingActionButton;
FloatingActionButton nextsong;
boolean playing =false;
String metadata;
int numerofsong=0;
TextView meta;
FloatingActionButton Back;
FloatingActionButton loop;
FloatingActionButton Foward;
String[] assetManager={"Bl$$D - F1AN1T.mp3" , "songt.mp3" , "Ярослав Кондратьев - ☆☭ЗАВОД☭☆.mp3"};
@Override
    protected void onCreate(Bundle savedInstanceState)  {

    super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        floatingActionButton=findViewById(R.id.floatingActionButton);
        seekBar=findViewById(R.id.seekBar);
        textView=findViewById(R.id.textView);
        meta=findViewById(R.id.textView2);
        Back=findViewById(R.id.back);
        Back.setOnClickListener(listener);
        loop=findViewById(R.id.floatingActionButton4);
        loop.setOnClickListener(listener);
        nextsong=findViewById(R.id.Nextsong);
        nextsong.setOnClickListener(listener);
        Foward=findViewById(R.id.foward);
        Foward.setOnClickListener(listener);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view)  {
                Play();
        }});
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int time = (int)(i/1000);
                textView.setText(String.valueOf(time));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer!=null){
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            }
        });
    }
 public void Play(){
     try {
         if(!playing ||mediaPlayer==null ){
             if(mediaPlayer==null){
                 mediaPlayer=new MediaPlayer();
                AssetFileDescriptor fileDescriptor=getAssets().openFd(assetManager[numerofsong]);
                 mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),fileDescriptor.getStartOffset(),fileDescriptor.getLength());
                 MediaMetadataRetriever metadataRetriever=new MediaMetadataRetriever();
                 metadataRetriever.setDataSource(fileDescriptor.getFileDescriptor(),fileDescriptor.getStartOffset(),fileDescriptor.getLength());
                 fileDescriptor.close();
                 metadata=metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                 metadata=metadata+" "+metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR);
                 meta.setText(metadata);
                 mediaPlayer.prepare();
                 new Thread(this).start();
             }
             floatingActionButton.setImageDrawable(ContextCompat.getDrawable(MainActivity.this,android.R.drawable.ic_media_pause));
              mediaPlayer.start();
             seekBar.setMax(mediaPlayer.getDuration());

             playing=true;
         }
         else{
             mediaPlayer.pause();
             floatingActionButton.setImageDrawable(ContextCompat.getDrawable(MainActivity.this,android.R.drawable.ic_media_play));
             playing=false;
         }
     }catch(IOException e){e.printStackTrace();}
 }
View.OnClickListener listener=new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.floatingActionButton4:
                mediaPlayer.setLooping(!mediaPlayer.isLooping());
                if(mediaPlayer.isLooping()) {loop.setColorFilter(Color.RED);}else{loop.setColorFilter(Color.GREEN);}
                break;
            case R.id.back:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-5000);
                break;
            case R.id.foward:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+5000);
            break;
            case R.id.Nextsong:
                numerofsong++;
                if (numerofsong>2){numerofsong=0;}
                mediaPlayer.pause();
                mediaPlayer.stop();
                mediaPlayer=null;
                Play();

            break;
        }
    }
};
    @Override
    public void run() {
        int pos= mediaPlayer.getCurrentPosition();
        int total= mediaPlayer.getDuration();
        while(mediaPlayer!=null && pos<total){
            try {
                Thread.sleep(1000);
                pos=mediaPlayer.getCurrentPosition();

            }catch(InterruptedException e){
                e.printStackTrace();
            }
            seekBar.setProgress(pos);
        }
    }
}