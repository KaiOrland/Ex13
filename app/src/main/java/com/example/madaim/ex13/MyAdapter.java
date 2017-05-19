package com.example.madaim.ex13;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;

import com.example.madaim.ex13.data.DatabaseHelper;
/**
 * Created by Madaim on 19/05/2017.
 */

public class MyAdapter extends ResourceCursorAdapter {
    public int currOrder = DatabaseHelper.SORT_BY_NUMS;
    private DatabaseHelper dbh;
    private Context context;

    public MyAdapter(Context context, int layout){
    super(context, layout, null, 0);
        this.dbh = DatabaseHelper.getInstance(context);
        this.context = context;
        Cursor cursor = this.dbh.getAllItems(currOrder);
        changeCursor(cursor);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}
