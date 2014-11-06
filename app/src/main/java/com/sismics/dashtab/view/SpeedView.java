package com.sismics.dashtab.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author bgamard.
 */
public class SpeedView extends ImageView {
    private float value = 0f;

    private Paint paint = new Paint();

    public SpeedView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint.setColor(Color.RED);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int angleStart = 130;
        float sweep = value;

        Path p = new Path();
        //move into center of the circle
        p.setLastPoint(257, 346);
        //add line from the center to arc at specified angle
        p.lineTo(257 + (float) Math.cos(Math.toRadians(angleStart)) * 600,
                346 + (float) Math.sin(Math.toRadians(angleStart)) * 600);
        //add arc from start angle with specified sweep
        p.addArc(new RectF(0, 0, 600, 600), angleStart, sweep);
        //from end of arc return to the center of circle
        p.lineTo(257, 346);

        canvas.clipPath(p);
        super.onDraw(canvas);
    }

    public void setValue(float value) {
        this.value = value;
    }
}
