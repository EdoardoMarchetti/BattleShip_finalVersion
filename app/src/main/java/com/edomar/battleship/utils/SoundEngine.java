package com.edomar.battleship.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class SoundEngine {

    private static final String TAG = "Sound Engine";

    private SoundPool mSP;
    public boolean isSoundEffectOn = false;
    private int mSoundSample_ID = -1;

    /*
    Constructor
     */
    public SoundEngine(Context c){
        //initialize the SoundPool
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes =
                    new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build();

            mSP = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else {
            mSP = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        }

        try{
            AssetManager assetManager = c.getAssets();
            AssetFileDescriptor descriptor;
            // Prepare the sounds in memory
            descriptor = assetManager.openFd("shoot.ogg");
            mSoundSample_ID = mSP.load(descriptor, 0);
            Log.d("Sound check", "SoundEngine: sample loaded, sample_id="+mSoundSample_ID);

        } catch (IOException e) {
            e.printStackTrace();
        }

        isSoundEffectOn = true;
    }


    public void playShoot(){
        if(this.isSoundEffectOn) {
            mSP.play(mSoundSample_ID, 1, 1, 1, 0, 1);
            Log.d("Sound check", "playShoot: ");
        }
    }

    public void setSoundEffectOn(boolean soundEffectOn) {
        isSoundEffectOn = soundEffectOn;
        Log.d(TAG, "setSoundEffectOn: modified");
    }
}
