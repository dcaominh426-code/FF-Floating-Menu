
package com.ff.floatingmenu;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
    String game = "com.dts.freefireth";

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(35, 60, 35, 20);

        Button ff = new Button(this);
        ff.setText("FREE FIRE");
        ff.setOnClickListener(v -> {
            game = "com.dts.freefireth";
            Toast.makeText(this, "Da chon Free Fire",
                Toast.LENGTH_SHORT).show();
        });

        Button max = new Button(this);
        max.setText("FREE FIRE MAX");
        max.setOnClickListener(v -> {
            game = "com.dts.freefiremax";
            Toast.makeText(this, "Da chon FF MAX",
                Toast.LENGTH_SHORT).show();
        });

        Button start = new Button(this);
        start.setText("START GAME");
        start.setOnClickListener(v -> {
            if (!Settings.canDrawOverlays(this)) {
                Intent permission = new Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
                startActivity(permission);
                return;
            }

            startService(new Intent(this, FloatingService.class));

            Intent launch = getPackageManager()
                .getLaunchIntentForPackage(game);

            if (launch != null) {
                startActivity(launch);
            } else {
                Toast.makeText(this, "Chua cai game",
                    Toast.LENGTH_LONG).show();
            }
        });

        layout.addView(ff);
        layout.addView(max);
        layout.addView(start);
        setContentView(layout);
    }
}
