package com.edomar.battleship.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

import java.util.HashMap;
import java.util.Map;

public class BitmapStore {

    private static Map <String, Bitmap> sVerticalBitmapsMap;
    private static Map <String, Bitmap> sHorizontalBitmapsMap;
    private static BitmapStore sInstance;


    public static BitmapStore getInstance(Context context){
        if(sInstance == null){
            sInstance = new BitmapStore(context);
        }
        return sInstance;
    }

    private BitmapStore (Context context){

        sVerticalBitmapsMap = new HashMap<>();
        sHorizontalBitmapsMap = new HashMap<>();

        //Add the default bitmap in each map

    }

    public static Bitmap getVerticalBitmap(String bitmapName){
        return sVerticalBitmapsMap.get(bitmapName);
        //da aggiungere un if else con l'immagine di default
    }

    public static Bitmap getHorizontalBitmap(String bitmapName){
        return sHorizontalBitmapsMap.get(bitmapName);
        //da aggiungere un if else con l'immagine di default
    }

    public static void addBitmap(Context c,
                                 String bitmapName,
                                 PointF blocksOccupied,
                                 float blockDimens,
                                 boolean needHorizontal){

        Bitmap bitmap;
        Bitmap verticalBitmap;
        Bitmap horizontalBitmap;

        // Make a resource id out of the string of the file name
        int resID = c.getResources().getIdentifier(bitmapName,
                "drawable",
                c.getPackageName());

        //Load the bitmap using id
        bitmap = BitmapFactory.
                decodeResource(c.getResources(), resID);

        verticalBitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (blocksOccupied.x *blockDimens),
                (int) (blocksOccupied.y * blockDimens),
                false);

        sVerticalBitmapsMap.put(bitmapName, verticalBitmap);

        if(needHorizontal){
            //create a horizontal image of bitmap
            horizontalBitmap = Bitmap.createScaledBitmap(bitmap,
                    (int) (blocksOccupied.y * blockDimens),
                    (int) (blocksOccupied.x * blockDimens),
                    false);

            sHorizontalBitmapsMap.put(bitmapName, horizontalBitmap);
        }

    }

    public  static void clearStore(){
        sVerticalBitmapsMap.clear();
        sHorizontalBitmapsMap.clear();
    }

}
