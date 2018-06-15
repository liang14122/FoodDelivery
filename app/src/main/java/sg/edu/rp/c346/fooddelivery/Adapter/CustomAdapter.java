package sg.edu.rp.c346.fooddelivery.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import sg.edu.rp.c346.fooddelivery.Object.MenuItem;
import sg.edu.rp.c346.fooddelivery.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 16004118 on 8/11/2017.
 */

public class CustomAdapter extends ArrayAdapter {

    Context parent_context;
    int layout_id;
    ArrayList<MenuItem> menuList;

    public CustomAdapter(Context context,
                         int resource,
                         ArrayList<MenuItem> objects) {
        super(context, resource, objects);
        parent_context = context;
        layout_id = resource;
        menuList = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        if(rowView == null){
            LayoutInflater inflater = (LayoutInflater)
                    parent_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(layout_id, parent, false);
        }else{
            rowView = convertView;
        }

        TextView tvPrice = (TextView)rowView.findViewById(R.id.textViewPrice);
        TextView tvName = (TextView) rowView.findViewById(R.id.textViewFoodName);
        TextView tvNumOfItem = (TextView) rowView.findViewById(R.id.textViewNumOfItem);
        TextView tvTotalPrice = (TextView) rowView.findViewById(R.id.textViewTotalPrice);


//        ImageView ivPicture = (ImageView)rowView.findViewById(R.id.imageViewPic);
        ImageView ivPic = (ImageView)rowView.findViewById(R.id.imageViewPicture);
        ImageView ivAdd = (ImageView) rowView.findViewById(R.id.imageViewAddSign);

        String foodName = menuList.get(position).getFoodName();
        tvPrice.setText("$" + Double.toString(menuList.get(position).getPrice()));
        tvName.setText(foodName);
//        Log.i("name", foodName);
        if (foodName.equalsIgnoreCase("Millet porridge")) {
//            ivPicture.setImageResource(R.drawable.millet_porridge);
            ivPic.setImageResource(R.mipmap.millet_porridge);
//            Log.i("name", foodName);
        }else if(foodName.equalsIgnoreCase( "Dumplings")){
//            ivPicture.setImageResource(R.drawable.dumplings);
            ivPic.setImageResource(R.mipmap.dumplings);
//            Log.i("name", foodName);

        }else{
//            ivPicture.setImageResource(R.drawable.no_image);
            ivPic.setImageResource(R.mipmap.no_image);
        }

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SQLiteDatabase demoDB = getContext().openOrCreateDatabase("FoodDelivery", MODE_PRIVATE, null);
//                demoDB.execSQL("DELETE FROM orderTable");
                try{
                    demoDB.execSQL("CREATE TABLE IF NOT EXISTS orderTable(foodName VARCHAR,price NUMERIC,quantity INTEGER,id INTEGER PRIMARY KEY)");
                    String foodName = menuList.get(position).getFoodName();
                    Double price = menuList.get(position).getPrice();
//                    demoDB.execSQL("INSERT INTO orderTable (foodName, price, quantity) " +
//                            "VALUES ('"+foodName+"', '"+price+"', 1)");
//                    Log.i("order", foodName + ", " + price + ", " + 1);
//                    Log.i("itemQtyinput", 1+"");



                    boolean check = true;
                    Cursor cc = demoDB.rawQuery("SELECT COUNT(*) FROM orderTable WHERE foodName = '"+foodName+"'", null);
                    if(cc != null && cc.moveToFirst());
                    do{
                        check = (cc.getInt(0) == 0);
                    }while (cc.moveToNext());
                    if(check == true){
                        Log.i("have record", "no");
                        try {
                            demoDB.execSQL("INSERT INTO orderTable (foodName, price, quantity) " +
                                    "VALUES ('"+foodName+"', '"+price+"', 1)");
                            Log.i("order", foodName + ", " + price + ", " + 1);
                            Log.i("itemQtyinput", 1+"");
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }else {
                        Log.i("have record", "yes");
                        try{
                            Cursor cQtyGet = demoDB.rawQuery("SELECT quantity FROM orderTable WHERE foodName = '"+foodName+"'", null);

                            int currentQty = 0;

                            if(cQtyGet != null && cQtyGet.moveToFirst());
                            do{
                                currentQty = cQtyGet.getInt(cQtyGet.getColumnIndex("quantity"));
                                Log.i("currentQty", currentQty+"");
                            }while(cQtyGet.moveToNext());
                            int updatedQty = currentQty + 1;
                            demoDB.execSQL("UPDATE orderTable SET quantity = " + (currentQty + 1 ) +" WHERE foodName = '" + foodName + "'");
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

            }
        });

        return rowView;
    }
}
