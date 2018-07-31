package be.greifmatthias.horizontalcalendarstrip;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import be.greifmatthias.horizontalcalendarstrip.View.DefaultTileLayout;
import be.greifmatthias.horizontalcalendarstrip.View.TileLayout;

public class Adapter extends RecyclerView.Adapter<Adapter.DateViewHolder> {
    private List<DateItem> _dates = new ArrayList<>();
    private Context _context;

    private TileLayout _tileLayout;

    private RecyclerView _parent;

    public Adapter(List<DateItem> items, Context context, @Nullable TileLayout tileLayout) {
        this._dates = items;
        this._context = context;

        if(tileLayout != null){
            this._tileLayout = tileLayout;
        }else{
            this._tileLayout = new DefaultTileLayout();
        }
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tile_day, parent, false);
        DateViewHolder viewHolder = new DateViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, final int position) {
//        Load tilelayout
        holder._flContent.removeAllViewsInLayout();
        if(getItem(position).isSelected()){
            holder._flContent.addView(this._tileLayout.getSelectedView(this._context));
        }else{
            holder._flContent.addView(this._tileLayout.getDefaultView(this._context));
        }

//        Bind information to view
        this._tileLayout.bind(holder._flContent, getItem(position), getItem(position).isSelected());

//        Set size
        int childh = holder._flContent.getChildAt(0).getMinimumHeight();
        int childw = holder._flContent.getChildAt(0).getMinimumWidth();

        holder._flContent.setLayoutParams(new FrameLayout.LayoutParams(childw, childh));
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

        if(position != oldposition) {
//        Update old
            if (oldselected != null) {
                oldselected.deselect();
                this.notifyItemChanged(oldposition);
            }

//        Select new
            this.getItem(position).select();
            this.notifyItemChanged(position);
        }
    }

    public void setSelected(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);

//        Find
        int i = 0;
        boolean got = false;
        for(DateItem d : this._dates){
            if(c.get(Calendar.YEAR) == d.getDate().get(Calendar.YEAR) && c.get(Calendar.DAY_OF_YEAR) == d.getDate().get(Calendar.DAY_OF_YEAR)){
                got = true;
                break;
            }
            i++;
        }

        int position = 0;
        if(got){
            position = i;
        }else{
            if(c.getTime().getTime() < getItem(getItemCount() - 1).getDate().getTime().getTime()){
                Calendar added = Calendar.getInstance();
                added.setTime(getItem(getItemCount() - 1).getDate().getTime());

                while (added.getTime().getTime() >= c.getTime().getTime()){
                    added.add(Calendar.DAY_OF_MONTH, -1);

                    DateItem add = new DateItem(added.getTime());
                    addItem(add);
                }

                position = getItemCount() - 1;
            }
        }

        final int p = position;

        setSelected(p);
    }

    public DateItem getSelected(){
        return getItem(getSelectedPosition());
    }

    public int getSelectedPosition(){
        int i = 0;
        for(DateItem d : this._dates){
            if(d.isSelected()){
                return i;
            }
            i++;
        }

        return -1;
    }

    public class DateViewHolder extends RecyclerView.ViewHolder {
        private FrameLayout _flContent;

        public DateViewHolder(View itemView) {
            super(itemView);

            this._flContent = (FrameLayout)itemView.findViewById(R.id.flContent);
        }
    }
}
