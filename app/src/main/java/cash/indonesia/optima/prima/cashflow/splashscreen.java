package cash.indonesia.optima.prima.cashflow;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class splashscreen extends AppCompatActivity {
    ProgressBar barbanner;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        barbanner = (ProgressBar) findViewById(R.id.barbanner);
        barbanner.setVisibility(View.GONE);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                barbanner.setVisibility(View.VISIBLE);
                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences prefs = getSharedPreferences("primacash", MODE_PRIVATE);
                        generator.ip = prefs.getString("ip", "");

                            Intent i = new Intent(splashscreen.this,MainMenu.class);
                            startActivity(i);
                            finish();
                    }
                }, 2000);
            }
        }, 2000);

        /**/
        // Example of a call to a native method


    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
