package be.greifmatthias.horizontalcalendarstrip.View;

import android.content.Context;
import android.view.View;

import be.greifmatthias.horizontalcalendarstrip.DateItem;

public abstract class TileLayout {
    public abstract int getSelectedLayout();
    public abstract int getDefaultLayout();
    public abstract View getSelectedView(Context context);
    public abstract View getDefaultView(Context context);
    public abstract void bind(View tile, DateItem item, boolean selected);
}
