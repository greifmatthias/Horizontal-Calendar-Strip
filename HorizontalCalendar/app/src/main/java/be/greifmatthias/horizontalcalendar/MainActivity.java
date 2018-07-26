package be.greifmatthias.horizontalcalendar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import be.greifmatthias.horizontalcalendarstrip.HorizontalCalendar;
import be.greifmatthias.horizontalcalendarstrip.RecyclerViewTouchHandler;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Calendar
        final HorizontalCalendar calendar = (HorizontalCalendar)findViewById(R.id.hcCalendar);

        calendar.setTouchHandler(new RecyclerViewTouchHandler.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                TextView t = (TextView)findViewById(R.id.tvTest);
                if(calendar.getSelected() != null) {
                    t.setText(calendar.getSelected().toLocaleString());
                }else{
                    t.setText("Rip!!");
                }
            }

            @Override
            public void onLongClick(View view, int position) {

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
