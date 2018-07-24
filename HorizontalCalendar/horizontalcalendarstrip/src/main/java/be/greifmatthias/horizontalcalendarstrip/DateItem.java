package be.greifmatthias.horizontalcalendarstrip;

import java.util.Calendar;
import java.util.Date;

public class DateItem {
    private Date _date;
    private boolean _selected;

    public DateItem(Date date){
        this._date = date;
        this._selected = false;
    }

    public void select(){
        this._selected = true;
    }

    public void deselect(){
        this._selected = false;
    }

    public boolean isSelected(){
        return this._selected;
    }

    public Calendar getDate(){
        Calendar c = Calendar.getInstance();
        c.setTime(this._date);

        return c;
    }
}
