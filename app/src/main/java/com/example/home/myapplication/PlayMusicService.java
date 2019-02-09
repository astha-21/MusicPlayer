package com.example.home.myapplication;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

public class PlayMusicService extends Service {

    MediaPlayer mp;
    int i=1;
    final MyBinder mb= new MyBinder();
    public PlayMusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mb;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String path = intent.getStringExtra("path");
        mp=MediaPlayer.create(this, Uri.parse(path));
        if(mp!=null)
        {
            mp.setLooping(true);
            mp.setVolume(100,100);
            mp.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public int getMediaPosition()
    {
        if(mp!=null)
        {
            mp.getCurrentPosition();
        }
        i++;
        return i;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mp!=null)
        {
            mp.stop();
            mp.release();
        }
    }

    public class MyBinder extends Binder{
        public PlayMusicService getService()
        {
            return PlayMusicService.this;
        }
    }
}
