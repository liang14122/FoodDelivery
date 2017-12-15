package sg.edu.rp.c346.fooddelivery;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 16004118 on 18/11/2017.
 */

public class ConfirmCustomAdapter extends ArrayAdapter {

    Context parent_context;
    int layout_id;
    ArrayList<OrderItem> orderList;

    public ConfirmCustomAdapter(Context context,
                              int resource,
                              ArrayList<OrderItem> objects) {
        super(context, resource, objects);
        parent_context = context;
        layout_id = resource;
        orderList = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (rowView == null){
            LayoutInflater inflater = (LayoutInflater) parent_context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(layout_id, parent, false);
        }else{
            rowView = convertView;
        }

        final TextView tvFoodName = (TextView) rowView.findViewById(R.id.textViewConfirmFoodName);
        final TextView tvQty = (TextView) rowView.findViewById(R.id.textViewConfirmQty);
        ImageView ivAdd = (ImageView) rowView.findViewById(R.id.imageViewAdd);
        ImageView ivMinus = (ImageView) rowView.findViewById(R.id.imageViewMinus);
        ImageView ivDelete = (ImageView) rowView.findViewById(R.id.imageViewDelete);

        OrderItem currentItem = orderList.get(position);
        final String foodName = currentItem.getName();
        tvFoodName.setText(foodName);
        tvQty.setText(currentItem.getQuantity()+"");
        final SQLiteDatabase demoDB = parent_context.openOrCreateDatabase("FoodDelivery", MODE_PRIVATE, null);



        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int updateQty = add(foodName, position);
                if(updateQty != -1){
                    tvQty.setText(Integer.toString(updateQty));
                }
            }
        });

        ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int updatedQty = -1;
                try{
                    boolean check;
                    Cursor cc = demoDB.rawQuery("SELECT COUNT(*) FROM orderTable WHERE foodName = '"+foodName+"'", null);
                    if(cc != null && cc.moveToFirst());
                    do{
                        check = (cc.getInt(0) == 0);
                    }while (cc.moveToNext());
                    if(check == true){
                        Log.i("error", "no record");
                    }else {
                        Log.i("have record?", "yes");
                        try{
                            Cursor cQtyGet = demoDB.rawQuery("SELECT quantity FROM orderTable WHERE foodName = '"+foodName+"'", null);

                            int currentQty;

                            if(cQtyGet != null && cQtyGet.moveToFirst());
                            do{
                                currentQty = cQtyGet.getInt(cQtyGet.getColumnIndex("quantity"));
                                Log.i("currentQty", currentQty+"");
                            }while(cQtyGet.moveToNext());

                            if(currentQty > 1){
                                updatedQty = currentQty - 1;
                                demoDB.execSQL("UPDATE orderTable SET quantity = " + (currentQty - 1 ) +" WHERE foodName = '" + foodName + "'");
                                orderList.get(position).setQuantity(updatedQty);
                                Log.i("updated", updatedQty+"");
                                Log.i("itemName", foodName);
                            }else if(currentQty == 1){
                                AlertDialog.Builder deleteAlert = new AlertDialog.Builder(parent_context);
                                deleteAlert.setTitle("Quantity of item cannot go under 1.");
                                deleteAlert.setMessage("Do you want to delete the item?");

                                deleteAlert.setCancelable(true);
                                deleteAlert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        demoDB.execSQL("DELETE FROM orderTable WHERE foodName = '" + foodName + "'");
                                        orderList.remove(position);
                                    }
                                });

                                deleteAlert.setNegativeButton("Cancel", null);
                                AlertDialog dialog = deleteAlert.create();
                                dialog.show();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            Log.i("db", e.toString());
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                if(updatedQty != -1){
                    tvQty.setText(Integer.toString(updatedQty));
                }
            }
        });

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               delete(foodName, position);
            }
        });
        return rowView;
    }

    private int add(String foodName, int position){
        final SQLiteDatabase demoDB = parent_context.openOrCreateDatabase("FoodDelivery", MODE_PRIVATE, null);
        int updatedQty = -1;
        try{
            boolean check;
            Cursor cc = demoDB.rawQuery("SELECT COUNT(*) FROM orderTable WHERE foodName = '"+foodName+"'", null);
            if(cc != null && cc.moveToFirst());
            do{
                check = (cc.getInt(0) == 0);
            }while (cc.moveToNext());
            if(check == true){
                Log.i("error", "no record");
            }else {
                Log.i("have record?", "yes");
                try{
                    Cursor cQtyGet = demoDB.rawQuery("SELECT quantity FROM orderTable WHERE foodName = '"+foodName+"'", null);

                    int currentQty;

                    if(cQtyGet != null && cQtyGet.moveToFirst());
                    do{
                        currentQty = cQtyGet.getInt(cQtyGet.getColumnIndex("quantity"));
                        Log.i("currentQty", currentQty+"");
                    }while(cQtyGet.moveToNext());
                    updatedQty = currentQty + 1;
                    demoDB.execSQL("UPDATE orderTable SET quantity = " + (currentQty + 1 ) +" WHERE foodName = '" + foodName + "'");
                    orderList.get(position).setQuantity(updatedQty);
                    Log.i("updated", updatedQty+"");
                    Log.i("itemName", foodName);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.i("db", e.toString());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return updatedQty;
    }

    private int minus(final String foodName, final int position){
        final SQLiteDatabase demoDB = parent_context.openOrCreateDatabase("FoodDelivery", MODE_PRIVATE, null);
        int updatedQty = -1;
        try{
            boolean check;
            Cursor cc = demoDB.rawQuery("SELECT COUNT(*) FROM orderTable WHERE foodName = '"+foodName+"'", null);
            if(cc != null && cc.moveToFirst());
            do{
                check = (cc.getInt(0) == 0);
            }while (cc.moveToNext());
            if(check == true){
                Log.i("error", "no record");
            }else {
                Log.i("have record?", "yes");
                try{
                    Cursor cQtyGet = demoDB.rawQuery("SELECT quantity FROM orderTable WHERE foodName = '"+foodName+"'", null);

                    int currentQty;

                    if(cQtyGet != null && cQtyGet.moveToFirst());
                    do{
                        currentQty = cQtyGet.getInt(cQtyGet.getColumnIndex("quantity"));
                        Log.i("currentQty", currentQty+"");
                    }while(cQtyGet.moveToNext());

                    if(currentQty > 1){
                        updatedQty = currentQty - 1;
                        demoDB.execSQL("UPDATE orderTable SET quantity = " + (currentQty - 1 ) +" WHERE foodName = '" + foodName + "'");
                        orderList.get(position).setQuantity(updatedQty);
                        Log.i("updated", updatedQty+"");
                        Log.i("itemName", foodName);
                    }else if(currentQty == 1){
                        AlertDialog.Builder deleteAlert = new AlertDialog.Builder(parent_context);
                        deleteAlert.setTitle("Quantity of item cannot go under 1.");
                        deleteAlert.setMessage("Do you want to delete the item?");

                        deleteAlert.setCancelable(true);
                        deleteAlert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                demoDB.execSQL("DELETE FROM orderTable WHERE foodName = '" + foodName + "'");
                                orderList.remove(position);
                            }
                        });

                        deleteAlert.setNegativeButton("Cancel", null);
                        AlertDialog dialog = deleteAlert.create();
                        dialog.show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Log.i("db", e.toString());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return updatedQty;
    }

    private void delete (final String foodName, final int position){
        final SQLiteDatabase demoDB = parent_context.openOrCreateDatabase("FoodDelivery", MODE_PRIVATE, null);
        try{
            boolean check;
            Cursor cc = demoDB.rawQuery("SELECT COUNT(*) FROM orderTable WHERE foodName = '"+foodName+"'", null);
            if(cc != null && cc.moveToFirst());
            do{
                check = (cc.getInt(0) == 0);
            }while (cc.moveToNext());
            if(check == true){
                Log.i("error", "no record");
            }else {
                Log.i("have record?", "yes");
                try{

                        AlertDialog.Builder deleteAlert = new AlertDialog.Builder(parent_context);
                        deleteAlert.setTitle("Delete item");
                        deleteAlert.setMessage("Do you want to delete the item?");

                        deleteAlert.setCancelable(true);
                        deleteAlert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                demoDB.execSQL("DELETE FROM orderTable WHERE foodName = '" + foodName + "'");
                                orderList.remove(position);
                            }
                        });

                        deleteAlert.setNegativeButton("Cancel", null);
                        AlertDialog dialog = deleteAlert.create();
                        dialog.show();
                    }catch (Exception e){
                    e.printStackTrace();
                    Log.i("db", e.toString());
                }

                }
            }catch (Exception e){
            e.printStackTrace();
        }
        }
    }

