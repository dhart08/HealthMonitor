package com.example.daveh.healthmonitor;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

/**
 * Created by daveh on 8/24/2017.
 */

public class CustomButton extends View {

    private String text;
    private Paint paint = new Paint();

    public CustomButton(Context context){
        super(context);

        this.text = "Run";
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawRect(100, 100, 200, 200, paint);
    }
}
