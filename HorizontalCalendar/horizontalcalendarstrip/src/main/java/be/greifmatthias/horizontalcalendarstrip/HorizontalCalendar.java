package be.greifmatthias.horizontalcalendarstrip;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import be.greifmatthias.horizontalcalendarstrip.View.TileLayout;

public class HorizontalCalendar extends LinearLayout {
    private boolean _showYear;
    private int _labelColor_month;
    private int _labelColor_year;
    private int _labelPosition;

    private boolean _defaultShowYear = false;
    private int _defaultLabelColor_Month = Color.argb(255,0,0,0);
    private int _defaultLabelColor_Year = Color.argb(200,0,0,0);;
    private int _defaultLabelPosition = 0;

    private TextView _tvMonth;
    private TextView _tvYear;
    private RecyclerView _rvDates;

    private TileLayout _tilelayout;

    private RecyclerViewTouchHandler _tileclickHandler;
    private RecyclerView.OnScrollListener _stripscrollHandler;

    public HorizontalCalendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

//        Get styles
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.HorizontalCalendar,
                0, 0);

//        Try to set attributes
        try {
            this._showYear = a.getBoolean(R.styleable.HorizontalCalendar_showYear, this._defaultShowYear);
            this._labelColor_month = a.getInteger(R.styleable.HorizontalCalendar_labelColor_month, this._defaultLabelColor_Month);
            this._labelColor_year = a.getInteger(R.styleable.HorizontalCalendar_labelColor_year, this._defaultLabelColor_Year);
            this._labelPosition = a.getInteger(R.styleable.HorizontalCalendar_labelPosition, this._defaultLabelPosition);
        } finally {
            a.recycle();
        }

        // Set view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.strip_control, this, true);

//        Get controls
        this._tvMonth = (TextView)findViewById(R.id.tvMonth);
        this._tvYear = (TextView) findViewById(R.id.tvYear);
        this._rvDates = (RecyclerView)findViewById(R.id.rvDates);

//        Set recycleview
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        this._rvDates.setLayoutManager(layoutManager);

//        Generate view with data
        this.setupView();
    }

    public void setTouchHandler(RecyclerViewTouchHandler.ClickListener clickListener){
        if(this._rvDates != null) {
//            Add touch handler
            this._rvDates.addOnItemTouchListener(new RecyclerViewTouchHandler(getContext(), this._rvDates, clickListener));
        }
    }

    public Date getSelected(){
        if(((Adapter)this._rvDates.getAdapter()).getSelected() != null) {
            return ((Adapter) this._rvDates.getAdapter()).getSelected().getDate().getTime();
        }

        return null;
    }

    public void setTileLayout(TileLayout layout){
        this._tilelayout = layout;

        setupView();
    }

    private void setupView(){
//        Set adapter with data
        List<DateItem> dates = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        for(int i = 0; i < 7; i++){
            c.add(Calendar.DAY_OF_MONTH, -1);

            dates.add(new DateItem(c.getTime()));
        }

        final Adapter adapter = new Adapter(dates, getContext(), this._tilelayout);
        this._rvDates.setAdapter(adapter);

//        Touch handlers
        if(_tileclickHandler != null) {
            this._rvDates.removeOnItemTouchListener(_tileclickHandler);
        }

        _tileclickHandler = new RecyclerViewTouchHandler(getContext(), _rvDates, new RecyclerViewTouchHandler.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                adapter.setSelected(position);

                _rvDates.scrollToPosition(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
        this._rvDates.addOnItemTouchListener(_tileclickHandler);

//        Check scroll changes for recyclerview
        if(_stripscrollHandler == null) {
            this._rvDates.removeOnScrollListener(_stripscrollHandler);
        }

        _stripscrollHandler = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int itemcount = recyclerView.getAdapter().getItemCount();
                int lastvisible = layoutManager.findLastVisibleItemPosition();

                boolean end = lastvisible + 5 >= itemcount;
                if (itemcount > 0 && end) {
//                    Generate lower date
                    Calendar c = adapter.getItem(adapter.getItemCount() - 1).getDate();
                    c.add(Calendar.DAY_OF_MONTH, -1);

//                    Add date to recyclerview
                    adapter.addItem(new DateItem(c.getTime()));
                }

//                    Check date
                Calendar clastvisible = ((Adapter) recyclerView.getAdapter()).getItem(lastvisible).getDate();
                _tvMonth.setText(new SimpleDateFormat("MMMM").format(clastvisible.getTime()));
                _tvYear.setText(new SimpleDateFormat("yyyy").format(clastvisible.getTime()));
            }
        };
        this._rvDates.addOnScrollListener(_stripscrollHandler);
    }
}
