package be.greifmatthias.horizontalcalendar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import be.greifmatthias.horizontalcalendarstrip.HorizontalCalendar;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Calendar
        final HorizontalCalendar calendar = (HorizontalCalendar)findViewById(R.id.hcCalendar);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -35);
        calendar.setSelected(c.getTime());

//        TileLayout layout = new TileLayout(R.layout.sample_selected, R.layout.sample_default) {
//            @Override
//            public void bind(View tile, DateItem item, boolean selected) {
//                Calendar c = item.getDate();
//                ((TextView)tile.findViewById(R.id.tvDate)).setText(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
//            }
//        };
//        calendar.setTileLayout(layout);

        calendar.setOnChanged(new HorizontalCalendar.onChangeListener() {
            @Override
            public void selectChanged(Date date) {
                Calendar c = Calendar.getInstance();
                c.setTime(date);

                ((TextView)findViewById(R.id.tvTest)).setText(c.getTime().toLocaleString());
            }

            @Override
            public void labelChanged(Date sourceDate) {

            }
        });

//        Github button
        Button btnGit = findViewById(R.id.btnGit);
        btnGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/greifmatthias/Horizontal-Calendar-Strip"));
                startActivity(browserIntent);
            }
        });
    }
}
