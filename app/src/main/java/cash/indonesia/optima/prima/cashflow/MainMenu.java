package cash.indonesia.optima.prima.cashflow;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.xml.transform.Result;

import pl.rafman.scrollcalendar.ScrollCalendar;
import pl.rafman.scrollcalendar.contract.MonthScrollListener;
import pl.rafman.scrollcalendar.contract.OnDateClickListener;

public class MainMenu extends AppCompatActivity {

    RelativeLayout layout;
    LayoutInflater inflate;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    layout.removeAllViews();
                    View calenders = inflate.inflate(R.layout.layout_calender_one,null);
                    calendermethod(calenders);
                    layout.addView(calenders);
                    return true;
                case R.id.navigation_dashboard:
                    layout.removeAllViews();
                    View transaction= inflate.inflate(R.layout.layout_transaction_two,null);
                    layout.addView(transaction);
                    return true;
                case R.id.navigation_notifications:
                    layout.removeAllViews();
                    View report = inflate.inflate(R.layout.layout_report_three,null);
                    layout.addView(report);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        if(generator.ip.equals("")) {
            initip();
        }
        else {
            initializelogin();
        }



        layout = findViewById(R.id.rlmainmenu);
        inflate = LayoutInflater.from(MainMenu.this);
        layout.removeAllViews();
        View calenders = inflate.inflate(R.layout.layout_calender_one,null);
        calendermethod(calenders);
        layout.addView(calenders);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void calendermethod(View v){
        ScrollCalendar scrollCalendar = (ScrollCalendar) v.findViewById(R.id.scrollCalendar);
        scrollCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onCalendarDayClicked(int year, int month, int day) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainMenu.this).create();
                alertDialog.setTitle(String.valueOf(day)+"/"+String.valueOf(month)+"/"+String.valueOf(year));
                alertDialog.setMessage("Alert message to be shown");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });


        scrollCalendar.setMonthScrollListener(new MonthScrollListener() {
            @Override
            public boolean shouldAddNextMonth(int lastDisplayedYear, int lastDisplayedMonth) {
                // return false if you don't want to show later months
                return true;
            }
            @Override
            public boolean shouldAddPreviousMonth(int firstDisplayedYear, int firstDisplayedMonth) {
                // return false if you don't want to show previous months
                return true;
            }
        });
    }
    public void initializelogin(){

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(MainMenu.this,R.style.AppCompatAlertDialogStyle);
        } else {
            builder = new AlertDialog.Builder(MainMenu.this);
        }
        View view = getLayoutInflater().inflate( R.layout.layout_minilogin, null );
        final EditText user,pass;
        user = view.findViewById(R.id.dialogusername);
        pass = view.findViewById(R.id.dialogpassword);
        builder.setTitle("Login")
                .setView(view)
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        generator.userlogin=user.getText().toString();
                        Logindata a = new Logindata(MainMenu.this,1,user.getText().toString(),pass.getText().toString());
                        a.execute();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(MainMenu.this, R.style.AppCompatAlertDialogStyle);
                        } else {
                            builder = new AlertDialog.Builder(MainMenu.this
                            );
                        }
                        builder.setTitle("Terminate Aplication")
                                .setMessage("Are you sure you want to Terminate Application?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        initializelogin();
                                    }
                                })
                                .setIcon(R.drawable.dialoginfo)
                                .show();

                    }
                })
                .setIcon(R.drawable.logininfocolored)
                .show();
    }

    private class Logindata extends AsyncTask<String,String,String>{
            private TextView statusField,roleField;
            private Context context;
            private Boolean connection = false;
            private int byGetOrPost = 0;
            private String user="";
            private String pass="";
            private ProgressDialog dialog = new ProgressDialog(MainMenu.this);

            //flag 0 means get and 1 means post.(By default it is get.)
           /* public Logindata(Context context,TextView statusField,TextView roleField,int flag) {
                this.context = context;
                this.statusField = statusField;
                this.roleField = roleField;
                byGetOrPost = flag;
            }*/

        public Logindata(Context context,int flag,String user , String pass) {
            this.context = context;

            byGetOrPost = flag;
            this.user=user;
            this.pass=pass;
        }

            protected void onPreExecute(){
                this.dialog.setMessage("Connecting");
                this.dialog.show();
            }

            @Override
            protected String doInBackground(String... arg0) {
                    try{
                        String username = user;
                        String password = pass;

                        String link="http://"+generator.ip+"/android-db/select_user.php";
                        String data  = URLEncoder.encode("username", "UTF-8") + "=" +
                                URLEncoder.encode(username, "UTF-8");
                        data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                                URLEncoder.encode(password, "UTF-8");
                        Log.e("IP", generator.ip);
                        URL url = new URL(link);
                        URLConnection conn = url.openConnection();

                        conn.setDoOutput(true);
                        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                        wr.write( data );
                        wr.flush();

                        BufferedReader reader = new BufferedReader(new
                                InputStreamReader(conn.getInputStream()));

                        StringBuilder sb = new StringBuilder();
                        String line = null;

                        // Read Server Response
                        while((line = reader.readLine()) != null) {
                            sb.append(line);
                            break;
                        }
                        return sb.toString();
                    } catch(Exception e){
                        Log.e("Exception",e.getMessage());
                        return new String("Exception: " + e.getMessage());
                    }
                }


            @Override
            protected void onPostExecute(String result){
                //this.statusField.setText("Login Successful");
                //this.roleField.setText(result);
                if(result.equals(generator.userlogin)){
                    this.dialog.dismiss();
                    Log.e("Result", result);
                    Toast.makeText(MainMenu.this,"Login as "+generator.userlogin,Toast.LENGTH_LONG);
                }
                else {

                }

            }
        }
        public void initip(){

            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(MainMenu.this, R.style.AppCompatAlertDialogStyle);
            } else {
                builder = new AlertDialog.Builder(MainMenu.this);
            }
            View view = getLayoutInflater().inflate(R.layout.layout_miniip, null);
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

            builder.setTitle("Reset Ip")
                    .setView(view)
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = getSharedPreferences("primacash", MODE_PRIVATE).edit();
                            editor.putString("ip", ip.getText().toString());
                            editor.apply();
                            initializelogin();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            initializelogin();
                        }
                    })
                    .setIcon(R.drawable.logininfocolored)
                    .show();
        }
    }

