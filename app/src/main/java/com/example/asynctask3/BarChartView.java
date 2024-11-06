package com.example.asynctask3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

public class BarChartView extends View {

    private List<Integer> bars;
    private Paint paint;

    public BarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
    }

    public void setData(List<Integer> data) {
        this.bars = data;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bars == null || bars.isEmpty()) return;

        float barWidth = (float) getWidth() / bars.size();
        float maxHeight = getHeight();

        for (int i = 0; i < bars.size(); i++) {
            int value = bars.get(i);
            float barHeight = (value / 100f) * maxHeight;

            float left = i * barWidth;
            float top = maxHeight - barHeight;
            float right = left + barWidth;
            float bottom = maxHeight;

            canvas.drawRect(left, top, right, bottom, paint);
        }
    }
}
