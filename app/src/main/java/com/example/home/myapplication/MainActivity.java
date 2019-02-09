package com.example.home.myapplication;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    String path;
    TextView path_selected;
    TextView music_length;
    Timer timer;
    TimerTask timerTask;
    Handler handler = new Handler();
    PlayMusicService playMusicService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button select_file= findViewById(R.id.select_file);

        Button start_service= findViewById(R.id.start_service);

        Button stop_service= findViewById(R.id.stop_service);

        path_selected=  findViewById(R.id.selected_file_path);
        music_length= findViewById(R.id.music_length);
        select_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("media/mp3");
                startActivityForResult(i,1);
            }
        });

        start_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  i = new Intent(v.getContext(),PlayMusicService.class);
                bindService(i,mServiceConnection, Context.BIND_AUTO_CREATE);
                i.putExtra("path",path);
                startService(i);
            }
        });

        stop_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(mServiceConnection);
                Intent  i = new Intent(v.getContext(),PlayMusicService.class);
                stopService(i);
                timer.cancel();
            }
        });

        timer=new Timer();
        timerTask=new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        music_length.setText(Integer.toString(playMusicService.getMediaPosition()));
                    }
                });

            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        path=data.getData().getPath();
        path_selected.setText(path);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayMusicService.MyBinder b = (PlayMusicService.MyBinder) service;
            playMusicService = b.getService();
            timer.schedule(timerTask,0,1000);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
