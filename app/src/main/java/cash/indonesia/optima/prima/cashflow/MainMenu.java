package cash.indonesia.optima.prima.cashflow;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
                    View transaction= inflate.inflate(R.layout.layout_calender_one,null);
                    layout.addView(transaction);
                    return true;
                case R.id.navigation_notifications:
                    layout.removeAllViews();
                    View report = inflate.inflate(R.layout.layout_calender_one,null);
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

        layout = findViewById(R.id.rlmainmenu);
        inflate = LayoutInflater.from(MainMenu.this);
        layout.removeAllViews();
        View calenders = inflate.inflate(R.layout.layout_calender_one,null);
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
}
