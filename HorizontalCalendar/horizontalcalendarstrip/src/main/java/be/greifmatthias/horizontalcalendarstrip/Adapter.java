package be.greifmatthias.horizontalcalendarstrip;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Adapter extends RecyclerView.Adapter<Adapter.DateViewHolder> {
    private List<DateItem> _dates = new ArrayList<>();

    public Adapter(List<DateItem> dates) {
        _dates = dates;
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tile_day, parent, false);
        DateViewHolder viewHolder = new DateViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        holder.bindData(_dates.get(position));
    }

    @Override
    public int getItemCount() {
        return _dates.size();
    }

    public DateItem getItem(int position){
        return this._dates.get(position);
    }

    public void addItem(DateItem date){
        this._dates.add(date);
        this.notifyItemInserted(this.getItemCount() - 1);
    }

    public void setSelected(int position){
//        Get old selected
        DateItem oldselected = null;
        int oldposition = -1;

//        Search old
        for(int i = 0; i < this._dates.size(); i++){
            if(this.getItem(i).isSelected()){
                oldselected = this.getItem(i);
                oldposition = i;
                break;
            }
        }

//        Update old
        if(oldselected != null){
            oldselected.deselect();
            this.notifyItemChanged(oldposition);
        }

//        Select new
        this.getItem(position).select();
        this.notifyItemChanged(position);
    }

    public DateItem getSelected(){
        for(DateItem d : this._dates){
            if(d.isSelected()){
                return d;
            }
        }

        return null;
    }

    public class DateViewHolder extends RecyclerView.ViewHolder {
        private TextView _tvDate;
        private TextView _tvDay;
        private LinearLayout _llRoot;

        private Context _context;

        public DateViewHolder(View itemView) {
            super(itemView);

//            Get controls
            this._tvDate = (TextView)itemView.findViewById(R.id.tvDate);
            this._tvDay = (TextView)itemView.findViewById(R.id.tvDay);
            this._llRoot = (LinearLayout) itemView.findViewById(R.id.llRoot);

//            Get context
            this._context = itemView.getContext();
        }

        public void bindData(DateItem date) {
//            Get date
            Calendar c = date.getDate();

//            Set Text
            this._tvDate.setText(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));

            DateFormatSymbols dfs = new DateFormatSymbols(Locale.getDefault());
            this._tvDay.setText(dfs.getShortWeekdays()[c.get(Calendar.DAY_OF_WEEK)]);

//            Set backgroundcolor if selected
            if(date.isSelected()){
                this._llRoot.setBackgroundColor(_context.getResources().getColor(R.color.Dark));
                this._tvDate.setTextColor(_context.getResources().getColor(R.color.White));
                this._tvDay.setTextColor(_context.getResources().getColor(R.color.White_semitrans));
            }else{
//            Revert possible stylings
                this._llRoot.setBackgroundColor(_context.getResources().getColor(R.color.Dark_lighttrans));
                this._tvDate.setTextColor(_context.getResources().getColor(R.color.Dark));
                this._tvDay.setTextColor(_context.getResources().getColor(R.color.Dark_semitrans));
            }
        }
    }
}
