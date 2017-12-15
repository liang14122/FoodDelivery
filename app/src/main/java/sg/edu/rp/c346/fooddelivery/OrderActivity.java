package sg.edu.rp.c346.fooddelivery;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class OrderActivity extends AppCompatActivity {

    ArrayList<MenuItem> alMenu = new ArrayList<MenuItem>();
//    ArrayList<MenuItem> alMenuSample = new ArrayList<MenuItem>();
//    Spinner spCategory;
    CustomAdapter caMenu;
    ListView lvMenu;
    LinearLayout llOrder;
    Button btnConfirm;

    ArrayList<MenuItem> alOrder = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

//        spCategory = (Spinner) findViewById(R.id.spinnerSpinner);
        lvMenu = (ListView) findViewById(R.id.listViewMenu);
        caMenu = new CustomAdapter(this, R.layout.menu_row, alMenu);
        llOrder = (LinearLayout) findViewById(R.id.linearLayoutOrder);
        lvMenu.setAdapter(caMenu);
        btnConfirm = (Button)findViewById(R.id.button);

        final TextView tvNumOfItem = (TextView) findViewById(R.id.textViewNumOfItem);
        final TextView tvTotalPrice = (TextView) findViewById(R.id.textViewTotalPrice);

        final SQLiteDatabase demoDB = this.openOrCreateDatabase("FoodDelivery", MODE_PRIVATE, null);
//        demoDB.execSQL("DELETE FROM orderTable");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(OrderActivity.this);
        if(!prefs.getBoolean("firstTime", false)){
            try {
                demoDB.execSQL("CREATE TABLE IF NOT EXISTS menu (foodName VARCHAR,price NUMERIC,quantity INTEGER,description VARCHAR, category VARCHAR,id INTEGER PRIMARY KEY)");
                demoDB.execSQL("INSERT INTO menu (foodName, price, quantity, description, category) " +
                        "VALUES ('Millet porridge', 1.5, 0, 'It is millet porridge', 'porridge')");
                demoDB.execSQL("INSERT INTO menu (foodName, price, quantity, description, category) " +
                        "VALUES ('Dumplings', 5.5, 10, 'It is dumplings', 'others')");
                demoDB.execSQL("INSERT INTO menu (foodName, price, quantity, description, category) " +
                        "VALUES ('Hot Dishes', 8.5, 10, 'It is Hot Dishes', 'Hot Dishes')");
                demoDB.execSQL("INSERT INTO menu (foodName, price, quantity, description, category) " +
                        "VALUES ('Cold Dishes', 6.5, 10, 'It is Cold Dishes', 'Cold Dishes')");}
            catch (Exception e){
                e.printStackTrace();
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }

//            demoDB.execSQL("DELETE FROM menu");
        boolean check = true;
        Cursor cc = demoDB.rawQuery("SELECT COUNT(*) FROM menu", null);
        if(cc != null && cc.moveToFirst()){
            check = (cc.getInt(0) == 0);
        }
        if(check == true){
            try {
                demoDB.execSQL("CREATE TABLE IF NOT EXISTS menu (foodName VARCHAR,price NUMERIC,quantity INTEGER, description VARCHAR, category VARCHAR,mostSeller INTEGER,id INTEGER PRIMARY KEY)");
                demoDB.execSQL("INSERT INTO menu (foodName, price, quantity, description, category, mostSeller) " +
                        "VALUES ('Millet porridge', 1.5, 10, 'It is millet porridge', 'porridge', 1)");
                demoDB.execSQL("INSERT INTO menu (foodName, price, quantity, description, category, mostSeller) " +
                        "VALUES ('Dumplings', 5.5, 10, 'It is dumplings', 'others', 1)");
                demoDB.execSQL("INSERT INTO menu (foodName, price, quantity, description, category, mostSeller) " +
                        "VALUES ('Hot Dishes', 8.5, 10, 'It is Hot Dishes', 'Hot Dishes', 0)");
                demoDB.execSQL("INSERT INTO menu (foodName, price, quantity, description, category, mostSeller) " +
                        "VALUES ('Cold Dishes', 6.5, 10, 'It is Cold Dishes', 'Cold Dishes', 0)");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
            Cursor c = demoDB.rawQuery("SELECT * FROM menu", null);

            if (c != null && c.moveToFirst()) ;
            do {
                Log.i("foodName", c.getString(c.getColumnIndex("foodName")));
                Log.i("price", Double.toString(c.getDouble(c.getColumnIndex("price"))));
//                Log.i("quantity", Integer.toString(c.getInt(c.getColumnIndex("quantity"))));
//                Log.i("description", c.getString(c.getColumnIndex("description")));
//                Log.i("category", c.getString(c.getColumnIndex("category")));

                MenuItem item = new MenuItem(c.getString(c.getColumnIndex("foodName")), c.getString(c.getColumnIndex("description")),
                        c.getDouble(c.getColumnIndex("price")), c.getString(c.getColumnIndex("category")),
                        c.getInt(c.getColumnIndex("quantity")), c.getInt(c.getColumnIndex("mostSeller")));
                alMenu.add(item);
//                alMenuSample.add(item);
            } while (c.moveToNext());

//        final ArrayList<MenuItem> finalMenuList = alMenuSample;

//        Log.i("size", Integer.toString(finalMenuList.size()));
        Log.i("size", Integer.toString(alMenu.size()));

        //open detailActivity
        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                MenuItem selectedItem = alMenu.get(pos);
                Intent intent = new Intent(getBaseContext(), DetailActivity.class);
                intent.putExtra("foodName", selectedItem.getFoodName());
                intent.putExtra("description", selectedItem.getDescription());
                intent.putExtra("quantity", selectedItem.getQuantity());
                intent.putExtra("price", selectedItem.getPrice());
                startActivity(intent);
            }
        });



//        demoDB.execSQL("DELETE FROM orderTable")

        llOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Double> priceSep = new ArrayList<Double>();
                ArrayList<Integer> qtySep = new ArrayList<Integer>();
                ArrayList<String> nameSep = new ArrayList<String>();
                double totalPrice = 0.0;
                int numItem = 0;
                try {
                     Cursor cOrder = demoDB.rawQuery("SELECT * FROM orderTable" , null);
                    if (cOrder != null && cOrder.moveToFirst()) ;
                    do {
                        int itemQty = cOrder.getInt(cOrder.getColumnIndex("quantity"));
                        double itemPrice = cOrder.getDouble(cOrder.getColumnIndex("price"));
                        String itemName = cOrder.getString(cOrder.getColumnIndex("foodName"));
                        priceSep.add(itemPrice);
                        qtySep.add(itemQty);
                        nameSep.add(itemName);
                        totalPrice += itemPrice * itemQty;
                        Log.i("itemPrice", itemPrice+"");
                        Log.i("itemQtyoutput", itemQty+"");
                        numItem += itemQty;
                        Log.i("orderac", itemQty+itemPrice+itemName);
                        Log.i("totalPrice", totalPrice+"");
                    } while (cOrder.moveToNext());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("db", e.toString());
                }

                tvNumOfItem.setText(numItem + " Items");
                tvTotalPrice.setText("Total $" + totalPrice);

            }
        });


        llOrder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final ArrayList<OrderItem> orderList = new ArrayList<OrderItem>();
                Cursor cGetOrder = demoDB.rawQuery("SELECT * FROM ordertable", null);
                if (cGetOrder != null && cGetOrder.moveToFirst());
                do{
                    String name = cGetOrder.getString(cGetOrder.getColumnIndex("foodName"));
                    int quantity =cGetOrder.getInt(cGetOrder.getColumnIndex("quantity"));
                    double price = cGetOrder.getInt(cGetOrder.getColumnIndex("price"));
                    orderList.add(new OrderItem(name,quantity, price));
                }while (cGetOrder.moveToNext());

                AlertDialog.Builder myBuilder = new AlertDialog.Builder(OrderActivity.this);
                myBuilder.setTitle("Order Detail");

                AlertCustomAdapter olAdapter = new AlertCustomAdapter(getApplicationContext(), R.layout.alert_row, orderList);
                View alertRow = getLayoutInflater().inflate(R.layout.alert_listview, null);
                ListView lvAlert = (ListView) alertRow.findViewById(R.id.listViewAlert);

                lvAlert.setAdapter(olAdapter);
                olAdapter.notifyDataSetChanged();

                myBuilder.setView(alertRow);
                myBuilder.setCancelable(true);
                myBuilder.setPositiveButton("Confirm my order", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(OrderActivity.this, ConfirmActivity.class);
//                        intent.putExtra("size", orderList.size());
//                        for(int index = 0; index < orderList.size(); index ++){
//                            OrderItem currentItem = orderList.get(index);
//                            intent.putExtra("foodName"+index, currentItem.getName());
//                            intent.putExtra("price"+index, currentItem.getPrice());
//                            intent.putExtra("qty"+index, currentItem.getQuantity());
//                        }
                        startActivity(intent);
                    }
                });

                myBuilder.setNegativeButton("Keep ordering", null);
                AlertDialog dialog = myBuilder.create();
                dialog.show();
                return false;
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderActivity.this, ConfirmActivity.class);
                startActivity(intent);
            }
        });
//        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
//                Log.i("size", Integer.toString(finalMenuList.size()));
//                if (pos == 0) {
//                    alMenu = finalMenuList;
//                    caMenu.notifyDataSetChanged();
//                } else {
//                    Log.i("size1", Integer.toString(finalMenuList.size()));
//                    alMenu.clear();
//                    Log.i("size2", Integer.toString(alMenuSample.size()));
//                    String category = spCategory.getSelectedItem().toString();
//                    //alMenu.removeAll(alMenu);
//                    Log.i("size", Integer.toString(alMenu.size()));
//                    Log.i("size", Integer.toString(finalMenuList.size()));
//                    for (int i = 0; i < finalMenuList.size(); i++) {
//                        MenuItem currentItem = finalMenuList.get(i);
//
//                        if (currentItem.getCategory().equalsIgnoreCase(category)) {
//                            alMenu.add(currentItem);
//                        }
//                    }
//                    caMenu.notifyDataSetChanged();
//
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
    }
}
