package be.greifmatthias.horizontalcalendarstrip;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
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
    private boolean _showLabel;

    private boolean _defaultShowYear = false;
    private int _defaultLabelColor_Month = Color.argb(255,0,0,0);
    private int _defaultLabelColor_Year = Color.argb(200,0,0,0);;
    private int _defaultLabelPosition = 0;
    private boolean _defaultShowLabel = true;

    private LinearLayout _llLabel;
    private TextView _tvMonth;
    private TextView _tvYear;
    private RecyclerView _rvDates;

    private TileLayout _tilelayout;

    private RecyclerViewTouchHandler _tileclickHandler;
    private RecyclerView.OnScrollListener _stripscrollHandler;

    private Date _request_selected;

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
            this._showLabel = a.getBoolean(R.styleable.HorizontalCalendar_labelShow, this._defaultShowLabel);
        } finally {
            a.recycle();
        }

        // Set view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.strip_control, this, true);

//        Get controls
        this._llLabel = (LinearLayout)findViewById(R.id.llLabel);
        this._tvMonth = (TextView)findViewById(R.id.tvMonth);
        this._tvYear = (TextView) findViewById(R.id.tvYear);
        this._rvDates = (RecyclerView)findViewById(R.id.rvDates);

//        Set recycleview
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        this._rvDates.setLayoutManager(layoutManager);

//        Generate view with data
        this.setupView();
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

    public void setSelected(Date date){
        this._request_selected = date;

//        Update data
        ((Adapter)this._rvDates.getAdapter()).setSelected(date);

//        Pass to listener
        if(_listener != null) {
            _listener.selectChanged(((Adapter) _rvDates.getAdapter()).getSelected().getDate().getTime());
        }

//        Scroll to requested element
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                _rvDates.scrollToPosition(((Adapter) _rvDates.getAdapter()).getSelectedPosition());

            }
        }, 200);
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
            dates.add(new DateItem(c.getTime()));

            c.add(Calendar.DAY_OF_MONTH, -1);
        }

        final Adapter adapter = new Adapter(dates, getContext(), this._tilelayout);
        this._rvDates.setAdapter(adapter);

//        Touch handlers
//        Reset if needed
        if(_tileclickHandler != null) {
            this._rvDates.removeOnItemTouchListener(_tileclickHandler);
        }

        _tileclickHandler = new RecyclerViewTouchHandler(getContext(), _rvDates, new RecyclerViewTouchHandler.ClickListener() {
            @Override
            public void onClick(View view, int position) {
//                Update adapter data
                adapter.setSelected(position);

//                Pass to listener
                if(_listener != null) {
                    _listener.selectChanged(((Adapter) _rvDates.getAdapter()).getSelected().getDate().getTime());
                }

//                Scroll to clicked element
                _rvDates.scrollToPosition(((Adapter) _rvDates.getAdapter()).getSelectedPosition());
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
                String tvMonth_text = _tvMonth.getText().toString();
                String tvYear_text = _tvYear.getText().toString();
                Calendar firstvisible = ((Adapter) recyclerView.getAdapter()).getItem(layoutManager.findFirstVisibleItemPosition()).getDate();
                _tvMonth.setText(new SimpleDateFormat("MMMM").format(firstvisible.getTime()));
                _tvYear.setText(new SimpleDateFormat("yyyy").format(firstvisible.getTime()));

                if(!(tvMonth_text.equals(_tvMonth.getText().toString()) &&
                        tvYear_text.equals(_tvYear.getText().toString()))){
                    _listener.labelChanged(firstvisible.getTime());
                }
            }
        };
        this._rvDates.addOnScrollListener(_stripscrollHandler);

//        Ensure set selected date if requested
        if(this._request_selected != null){
            this.setSelected(this._request_selected);
        }

//        Setup UI
        this.showLabel(this._showLabel);
    }

    public void showLabel(boolean show){
        this._showLabel = show;

//        Update UI
        if(show){
            this._llLabel.setVisibility(VISIBLE);
        }else{
            this._llLabel.setVisibility(GONE);
        }
    }

    private onChangeListener _listener;
    public interface onChangeListener{
        public void selectChanged(Date date);
        public void labelChanged(Date sourceDate);
    }

    public void setOnChanged(onChangeListener listener){
        this._listener = listener;

//        Ensure set selected date if requested
        if(_request_selected != null){
            this.setSelected(_request_selected);
        }
    }
}
