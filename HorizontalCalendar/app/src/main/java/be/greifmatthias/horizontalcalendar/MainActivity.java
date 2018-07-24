package be.greifmatthias.horizontalcalendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import be.greifmatthias.horizontalcalendarstrip.HorizontalCalendar;
import be.greifmatthias.horizontalcalendarstrip.RecyclerViewTouchHandler;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final HorizontalCalendar calendar = (HorizontalCalendar)findViewById(R.id.hcCalendar);

        calendar.setTouchHandler(new RecyclerViewTouchHandler.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                TextView t = (TextView)findViewById(R.id.tvTest);
                t.setText(calendar.getSelected().toLocaleString() + " clicked.");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
    }
}
