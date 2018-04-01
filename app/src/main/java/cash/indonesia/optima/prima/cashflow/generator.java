package cash.indonesia.optima.prima.cashflow;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by rwina on 3/26/2018.
 */

public class generator {
    public static TextView statusConnection = null;
    public static String userlogin="";
    public static String ip="";

    public void createdialod(Context context,String title , String message ,String yesmsg,String nomsg){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(R.drawable.dialoginfo)
                .show();
    }
    public static void recallipsettings (final Activity context){
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
            } else {
                builder = new AlertDialog.Builder(context);
            }
            View view = context.getLayoutInflater().inflate(R.layout.layout_miniip, null);
            final EditText ip;
            ip = view.findViewById(R.id.dialogip);

        ip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(ip.getText().toString().length()==15){
                    ip.setText(ip.getText().toString().substring(0,ip.getText().toString().length()-1));
                    ip.setSelection(ip.getText().length());
                }
                else {
                    try {
                        String[] extensionRemoved = ip.getText().toString().split("\\.");
                        int ipnum = 0, base = 3, declare = 4;
                        for (base = 3; base < 12; base += declare) {
                            if (ip.getText().toString().length() <= base) {
                                ipnum++;
                            }
                        }
                        int[] ipdata = new int[ipnum];
                        String[] parts = ip.getText().toString().split("\\.");
                        if (Integer.parseInt(parts[parts.length-1]) > 1000) {
                            ip.setText(ip.getText().toString().substring(0, ip.getText().toString().length() - 4));
                            ip.setSelection(ip.getText().length());
                        }
                        else if(Integer.parseInt(parts[parts.length-1]) > 255) {
                            ip.setText(ip.getText().toString().substring(0, ip.getText().toString().length() - 3));
                            ip.setSelection(ip.getText().length());
                        }

                    }catch (Exception e){
                        Log.e("Eroor", e.getMessage());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

            builder.setTitle("Reset IP")
                    .setView(view)
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = context.getSharedPreferences("primacash", MODE_PRIVATE).edit();
                            editor.putString("ip", ip.getText().toString());
                            editor.apply();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(R.drawable.logininfocolored)
                    .show();
    }
}
