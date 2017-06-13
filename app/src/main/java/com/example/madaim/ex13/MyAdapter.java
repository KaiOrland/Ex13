package com.example.madaim.ex13;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.TextView;

import com.example.madaim.ex13.data.DBContract;
import com.example.madaim.ex13.data.DatabaseHelper;
import com.example.madaim.ex13.data.Item;

import java.util.HashSet;

/**
 * Created by Madaim on 19/05/2017.
 */

public class MyAdapter extends ResourceCursorAdapter {
    public int currOrder = DatabaseHelper.SORT_BY_NUMS;
    private DatabaseHelper dbh;
    private Context context;
    public final static int MAX_NUMBERS = 99;
    private final static int MAX_ITEMS =16;


    public MyAdapter(Context context, int layout){
    super(context, layout, null, 0);
        this.dbh = DatabaseHelper.getInstance(context);
        this.context = context;
        Cursor cursor = this.dbh.getAllItems(currOrder);
        changeCursor(cursor);

    }


    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView tv = (TextView)view.findViewById(R.id.tvNumber);
        int number = cursor.getInt(cursor.getColumnIndex(DBContract.ItemEntry.KEY_NUMBER));
        view.setTag(cursor.getInt(cursor.getColumnIndex(DBContract.ItemEntry.KEY_ID)));
        int color = cursor.getInt(cursor.getColumnIndex(DBContract.ItemEntry.KEY_COLOR));
        ((GradientDrawable)view.getBackground()).setColor(color);

        if(number!=-1){
            tv.setText(Integer.toString(number));
            view.setOnClickListener(null);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                   /*dbh.deleteItem((Integer) v.getTag());
                    changeCursor(dbh.getAllItems(currOrder));
                    notifyDataSetChanged();
*/
                    ((MainActivity)context).openNewItemDialog((Integer)v.getTag());


                    return true;
                }
            });

            }

        else{
            tv.setText("...");
            if(cursor.getCount()<MAX_ITEMS)
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity)context).openNewItemDialog(-1);
                    }
                });
            else
                view.setOnClickListener(null);
            view.setOnLongClickListener(null);

        }
    }

    public HashSet<Integer> getExistingNumbers(){
        return this.dbh.getExistingNumbers();
    }

    public void addNewItem(Item item){
        this.dbh.addItem(item);
        changeCursor(this.dbh.getAllItems(currOrder));
        notifyDataSetChanged();
    }

    public void updateItem(Item item){
        this.dbh.updateItem(item);
        changeCursor(this.dbh.getAllItems(currOrder));
        notifyDataSetChanged();
    }

    public void sortItems(int sortBy){
        this.currOrder = sortBy;
        changeCursor(this.dbh.getAllItems(sortBy));
        notifyDataSetChanged();
    }
}
