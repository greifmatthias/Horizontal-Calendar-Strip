package be.greifmatthias.horizontalcalendarstrip.View;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

import be.greifmatthias.horizontalcalendarstrip.DateItem;
import be.greifmatthias.horizontalcalendarstrip.R;

public class DefaultTileLayout extends TileLayout {

    public DefaultTileLayout(){
        super();
    }

    public DefaultTileLayout(@Nullable int selectedLayout, @Nullable int defaultLayout){
        super(selectedLayout, defaultLayout);
    }

    public void bind(View tile, DateItem item, boolean selected){
//        Get date
        Calendar c = item.getDate();

//        Set Text
        ((TextView)tile.findViewById(R.id.tvDate)).setText(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));

        DateFormatSymbols dfs = new DateFormatSymbols(Locale.getDefault());
        ((TextView)tile.findViewById(R.id.tvDay)).setText(dfs.getShortWeekdays()[c.get(Calendar.DAY_OF_WEEK)]);
    }
}
