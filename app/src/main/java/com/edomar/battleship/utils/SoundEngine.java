package com.edomar.battleship.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import com.edomar.battleship.R;

import java.io.IOException;

public class SoundEngine {

    private static final String TAG = "Sound Engine";

    /**Instance*/
    private static SoundEngine sInstance;


    private static SoundPool mSP;
    public static boolean isSoundEffectOn = false;

    /**Sound ids**/
    private static int mSoundSample_ID = -1;
    private static int mExplosion_ID = -1;
    private static int mSplash_ID = -1;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Context context;

    /*
    Constructor
     */
    private SoundEngine(Context c){
        sp = c.getSharedPreferences(c.getString(R.string.configuration_preference_key), Context.MODE_PRIVATE);
        editor = sp.edit();
        context = c;

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

            descriptor = assetManager.openFd("explosion.ogg");
            mExplosion_ID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("splash.ogg");
            mSplash_ID = mSP.load(descriptor, 0);
            Log.d("Sound check", "SoundEngine: sample loaded, sample_id="+mSoundSample_ID);

        } catch (IOException e) {
            e.printStackTrace();
        }

        isSoundEffectOn = sp.getBoolean(c.getString(R.string.animation_sound_key), true);
    }

    public  static SoundEngine getInstance(Context c){
        if(sInstance == null){
            sInstance = new SoundEngine(c);
        }
        return sInstance;
    }

    public static void playShoot(){
        if(isSoundEffectOn) {
            mSP.play(mSoundSample_ID, 1, 1, 1, 0, 1);
            Log.d("Sound check", "playShoot: ");
        }
    }

    public static void playExplosion(){
        if(isSoundEffectOn){
            mSP.play(mExplosion_ID, 1,1,1,0,1);
            Log.d("Sound check", "playExplosion: ");
        }
    }

    public static void playSplash() {
        if(isSoundEffectOn){
            mSP.play(mSplash_ID, 1,1,1,0,1);
            Log.d("Sound check", "playExplosion: ");
        }
    }


    public static void enableSoundEffect(){
        isSoundEffectOn = true;
    }

    public static void disableSoundEffect(){
        isSoundEffectOn = false;
    }


}
