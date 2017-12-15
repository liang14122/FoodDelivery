package sg.edu.rp.c346.fooddelivery;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 16004118 on 22/10/2017.
 */

public class SpecialCustomAdapter extends ArrayAdapter {

    Context parent_context;
    int layout_id;

    ArrayList<MenuItem> GTList;

    public SpecialCustomAdapter(Context context,
                                int resource,
                                ArrayList<MenuItem> objects) {
        super(context, resource, objects);
        parent_context = context;
        layout_id = resource;
        GTList = objects;
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

        TextView tvFoodName = (TextView)rowView.findViewById(R.id.textViewFoodName);
        TextView tvQuantity = (TextView) rowView.findViewById(R.id.textViewQuantity);
        ImageView iv = (ImageView)rowView.findViewById(R.id.imageViewPicture);
        Button btnAdd = (Button) rowView.findViewById(R.id.buttonAdd);

        MenuItem currentItem = GTList.get(position);

        tvFoodName.setText(currentItem.getFoodName());
        tvQuantity.setText(Integer.toString(currentItem.getQuantity())+ " left");
        String foodName = currentItem.getFoodName();
        if (foodName.equalsIgnoreCase("Millet porridge")) {
            iv.setImageResource(R.drawable.millet_porridge);
//            Log.i("name1", foodName);
        }else if(foodName.equalsIgnoreCase("Dumplings")){
            iv.setImageResource(R.drawable.dumplings);
//            Log.i("name2", foodName);
        }else{
            iv.setImageResource(R.drawable.no_image);
//            Log.i("name3", foodName);
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SQLiteDatabase demoDB = getContext().openOrCreateDatabase("FoodDelivery", MODE_PRIVATE, null);
                try{
                    demoDB.execSQL("CREATE TABLE IF NOT EXISTS orderTable(foodName VARCHAR,price NUMERIC,quantity INTEGER,id INTEGER PRIMARY KEY)");
                    String foodName = GTList.get(position).getFoodName();
                    Double price = GTList.get(position).getPrice();
                    Integer quantity = GTList.get(position).getQuantity();
                    demoDB.execSQL("INSERT INTO menu (foodName, price, quantity) " +
                            "VALUES ('"+foodName+"', '"+price+"', '"+quantity+"')");
                    Log.i("order", foodName + ", " + price + ", " + quantity);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        return rowView;
    }
}
