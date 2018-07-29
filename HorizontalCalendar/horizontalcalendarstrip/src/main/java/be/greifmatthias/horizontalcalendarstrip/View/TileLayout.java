package be.greifmatthias.horizontalcalendarstrip.View;

import android.content.Context;
import android.view.View;

import be.greifmatthias.horizontalcalendarstrip.DateItem;
import be.greifmatthias.horizontalcalendarstrip.R;

public abstract class TileLayout {
    private int _defaultLayout;
    private int _selectedLayout;

//    Default constructor
    public TileLayout(){ }

//    Constructor for custom tile layout
    public TileLayout(int selectedLayout, int defaultLayout){
        this._defaultLayout = defaultLayout;
        this._selectedLayout = selectedLayout;
    }

    public View getSelectedView(Context context){
        if(this._selectedLayout != 0) {
            return View.inflate(context, this._selectedLayout, null);
        }

        return View.inflate(context, R.layout.tile_day_selected, null);
    }

    public View getDefaultView(Context context){
        if(this._defaultLayout != 0) {
            return View.inflate(context, this._defaultLayout, null);
        }

        return View.inflate(context, R.layout.tile_day_default, null);
    }

    public abstract void bind(View tile, DateItem item, boolean selected);
}
