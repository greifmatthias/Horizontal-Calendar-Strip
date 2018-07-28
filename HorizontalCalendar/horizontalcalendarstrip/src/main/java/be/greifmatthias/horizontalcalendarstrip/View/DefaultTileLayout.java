package be.greifmatthias.horizontalcalendarstrip.View;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

import be.greifmatthias.horizontalcalendarstrip.DateItem;
import be.greifmatthias.horizontalcalendarstrip.R;

public class DefaultTileLayout extends TileLayout {
    private int _selected;
    private int _default;

    public DefaultTileLayout(){
        this._selected = 0;
        this._default = 0;
    }

    public DefaultTileLayout(@Nullable int selectedLayout, @Nullable int defaultLayout){
        this._selected = selectedLayout;
        this._default = defaultLayout;
    }

    public int getSelectedLayout(){
        if(this._selected != 0) {
            return this._selected;
        }

        return R.layout.tile_day_selected;
    }

    public int getDefaultLayout(){
        if(this._default != 0) {
            return this._default;
        }

        return R.layout.tile_day_default;
    }

    public View getSelectedView(Context context){
        return View.inflate(context, getSelectedLayout(), null);
    }

    public View getDefaultView(Context context){
        return View.inflate(context, getDefaultLayout(), null);
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
