package com.edomar.battleship.logic.components.interfaces;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.edomar.battleship.logic.transform.Transform;
import com.edomar.battleship.logic.specifications.ObjectSpec;

public interface GraphicsComponent { //pagina 519 e 627

    /** Usato in GameObjectFactory**/
    void initialize (Context c , ObjectSpec spec, PointF objectSize); //objectSize è già in proporzionato in funzione della dimensione dei blocchi

    /** Invocato dal gameObject e non dal renderer **/
    void draw (Canvas canvas, Paint paint, Transform t); //grid forse non serve
}
