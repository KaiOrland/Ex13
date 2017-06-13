package com.example.madaim.ex13;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.NumberPicker;

import com.example.madaim.ex13.data.DatabaseHelper;
import com.example.madaim.ex13.data.Item;

import java.util.HashSet;

import ColorPicker.LineColorPicker;

public class MainActivity extends AppCompatActivity {

    MyAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gv = (GridView)findViewById(R.id.gridView);
        this.adapter = new MyAdapter(this, R.layout.item);
        gv.setAdapter(adapter);
        registerForContextMenu(gv);

    }
    public void openNewItemDialog(int itemID){
        HashSet<Integer> existingNumbers = this.adapter.getExistingNumbers();
        String[] stringArray = new String[MyAdapter.MAX_NUMBERS + 1 -existingNumbers.size()];
        int i=0,n=0;
        while (n <=MyAdapter.MAX_NUMBERS){

            if (!existingNumbers.contains(n))
                stringArray[i++] = Integer.toString(n);
            n++;
        }
        Bundle bndl = new Bundle();
        bndl.putStringArray("numbers", stringArray);
        bndl.putInt("itemID", itemID);
        NewItemDlg dlg = new NewItemDlg(bndl);
        dlg.setCancelable(false);
        dlg.show(getFragmentManager(), "");
    };

    private class NewItemDlg extends DialogFragment {

        private LineColorPicker colorPicker;
        private int itemID =-1;
        private NumberPicker num;
        String[] stringArray=null;

        private String[] pallete = new String[] { "#b8c847", "#67bb43", "#41b691",
                "#4182b6", "#4149b6", "#7641b6", "#b741a7", "#c54657", "#d1694a" };

        private int[] palleteInt = new int[] { 0xffb8c847, 0xff67bb43, 0xff41b691,
                0xff4182b6, 0xff4149b6, 0xff7641b6, 0xffb741a7, 0xffc54657, 0xffd1694a };

        public NewItemDlg() {}//to let android auto-restart the fragment when rotating

        public NewItemDlg(Bundle args) {
            this.setArguments(args);}

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            getMenuInflater().inflate(R.menu.context, menu);
            super.onCreateContextMenu(menu, v, menuInfo);
        }

        @Override
        public boolean onContextItemSelected(MenuItem item) {
            return super.onContextItemSelected(item);
           // switch (item.getItemId()){
           //     case R.id.change:
           //         onCreateDialog();
            //        break;
           //     case R.id.delete:


           // }
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final View dlgView = getActivity().getLayoutInflater().inflate(R.layout.new_item, null);

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity())
                            .setView(dlgView)
                            .setTitle("New Item")
                            .setCancelable(false)
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dismiss();
                                }
                            })
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int index = num.getValue();

                                    Item item = new Item(Integer.parseInt(stringArray[index]),colorPicker.getColor());
                                    if (itemID!=-1){
                                        item.setId(itemID);
                                        adapter.updateItem(item);
                                    }
                                    else
                                        adapter.addNewItem(item);
                                    dismiss();
                                }
                            });

            colorPicker = (LineColorPicker) dlgView.findViewById(R.id.colorPicker);
            num =(NumberPicker) dlgView.findViewById(R.id.numberPicker);

            int[] colors = generateColors(Color.BLUE, Color.RED, 10);
//			colorPicker.setColors(colors);
            colorPicker.setColors(palleteInt);
            colorPicker.setSelectedColorPosition(0);
            this.itemID = getArguments().getInt("itemID");
            this.stringArray = getArguments().getStringArray("numbers");//new String[10];
            num.setDividerDrawable(null);
            num.setMaxValue(stringArray.length-1);
            num.setMinValue(0);

            num.setDisplayedValues(stringArray);
            return builder.create();
        }

        private int[] generateColors(int from, int to, int count) {
            int[] palette = new int[count];

            float[] hsvFrom = new float[3];
            float[] hsvTo = new float[3];

            Color.colorToHSV(from, hsvFrom);
            Color.colorToHSV(to, hsvTo);

            float step = (hsvTo[0] - hsvFrom[0]) / count;

            for (int i = 0; i < count; i++) {
                palette[i] = Color.HSVToColor(hsvFrom);

                hsvFrom[0] += step;
            }

            return palette;
        }
    }

    public void rearange (View v){
        switch (v.getId()){
            case R.id.numSort:
                adapter.sortItems(DatabaseHelper.SORT_BY_NUMS);
                break;
            case R.id.colorSort:
                adapter.sortItems(DatabaseHelper.SORT_BY_COLORS);
                break;
            case R.id.shuffle:
                adapter.sortItems(DatabaseHelper.SHUFFLE);
                break;
        }
    }
}
