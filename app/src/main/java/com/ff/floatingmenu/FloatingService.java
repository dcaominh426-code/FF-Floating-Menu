
package com.ff.floatingmenu;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FloatingService extends Service {
    private WindowManager wm;
    private LinearLayout menu;
    private WindowManager.LayoutParams params;
    private boolean expanded = false;

    @Override
    public void onCreate() {
        super.onCreate();
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        menu = new LinearLayout(this);
        menu.setOrientation(LinearLayout.VERTICAL);
        menu.setPadding(18, 12, 18, 12);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.rgb(20, 20, 25));
        bg.setCornerRadius(25);
        bg.setStroke(2, Color.RED);
        menu.setBackground(bg);

        Button toggle = new Button(this);
        toggle.setText("FF");
        menu.addView(toggle);

        LinearLayout options = new LinearLayout(this);
        options.setOrientation(LinearLayout.VERTICAL);
        options.setVisibility(View.GONE);

        TextView title = new TextView(this);
        title.setText("FF AIM MENU");
        title.setTextColor(Color.RED);
        title.setTextSize(20);
        options.addView(title);

        Button training = new Button(this);
        training.setText("AIM TRAINING");
        training.setOnClickListener(v ->
            Toast.makeText(this,
                "Luyen aim trong app rieng",
                Toast.LENGTH_SHORT).show());
        options.addView(training);

        Button close = new Button(this);
        close.setText("CLOSE MENU");
        close.setOnClickListener(v -> stopSelf());
        options.addView(close);

        menu.addView(options);

        toggle.setOnClickListener(v -> {
            expanded = !expanded;
            options.setVisibility(expanded ?
                View.VISIBLE : View.GONE);
        });

        params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 30;
        params.y = 180;

        toggle.setOnTouchListener(new View.OnTouchListener() {
            int startX, startY;
            float touchX, touchY;
            boolean moved;

            @Override
            public boolean onTouch(View v, MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = params.x;
                        startY = params.y;
                        touchX = e.getRawX();
                        touchY = e.getRawY();
                        moved = false;
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        int dx = (int)(e.getRawX() - touchX);
                        int dy = (int)(e.getRawY() - touchY);
                        if (Math.abs(dx) > 8 ||
                            Math.abs(dy) > 8) moved = true;
                        params.x = startX + dx;
                        params.y = startY + dy;
                        wm.updateViewLayout(menu, params);
                        return true;

                    case MotionEvent.ACTION_UP:
                        if (!moved) toggle.performClick();
                        return true;
                }
                return false;
            }
        });

        wm.addView(menu, params);
    }

    @Override
    public void onDestroy() {
        if (menu != null) wm.removeView(menu);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
