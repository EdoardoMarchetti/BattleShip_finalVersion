package com.edomar.battleship.logic.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.edomar.battleship.logic.Transform;
import com.edomar.battleship.logic.specifications.ObjectSpec;
import com.edomar.battleship.view.Grid;

import androidx.constraintlayout.widget.ConstraintSet;

public interface InterfaceGraphicsComponent { //pagina 519 e 627

    void initialize (Context c , ObjectSpec spec, PointF objectSize); //objectSize è già in proporzionato in funzione della dimensione dei blocchi

    void draw (Canvas canvas, Paint paint, Transform t, Grid grid); //grid forse non serve
}
