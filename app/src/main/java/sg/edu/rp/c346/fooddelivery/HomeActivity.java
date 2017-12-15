package sg.edu.rp.c346.fooddelivery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    ListView lvGT;
    ArrayList<MenuItem> specialList;
    SpecialCustomAdapter caGourmet;
    Button btnOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        btnOrder = (Button) findViewById(R.id.buttonStartOrder);
        lvGT = (ListView) findViewById(R.id.listViewSpecial);
        specialList = new ArrayList<>();

        caGourmet = new SpecialCustomAdapter(this, R.layout.mostsellers_row, specialList);
        lvGT.setAdapter(caGourmet);

        final SQLiteDatabase demoDB = this.openOrCreateDatabase("FoodDelivery", MODE_PRIVATE, null);
        demoDB.execSQL("DROP TABLE IF EXISTS menu");


        demoDB.execSQL("CREATE TABLE IF NOT EXISTS menu (foodName VARCHAR,price NUMERIC,quantity INTEGER, description VARCHAR, category VARCHAR, mostSeller INTEGER, id INTEGER PRIMARY KEY)");

//        demoDB.execSQL("DELETE FROM menu");
//
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        if(!prefs.getBoolean("firstTime", false)){
            try {
                demoDB.execSQL("CREATE TABLE IF NOT EXISTS menu (foodName VARCHAR,price NUMERIC,quantity INTEGER, description VARCHAR, category VARCHAR, mostSeller INTEGER, id INTEGER PRIMARY KEY)");
                demoDB.execSQL("INSERT INTO menu (foodName, price, quantity, description, category, mostSeller) " +
                        "VALUES ('Millet porridge', 1.5, 10, 'It is millet porridge', 'porridge', 1 )");
                demoDB.execSQL("INSERT INTO menu (foodName, price, quantity, description, category, mostSeller) " +
                        "VALUES ('Dumplings', 5.5, 10, 'It is dumplings', 'others', 1 )");
                demoDB.execSQL("INSERT INTO menu (foodName, price, quantity, description, category, mostSeller) " +
                        "VALUES ('Hot Dishes', 8.5, 10, 'It is Hot Dishes', 'Hot Dishes', 0 )");
                demoDB.execSQL("INSERT INTO menu (foodName, price, quantity, description, category, mostSeller) " +
                        "VALUES ('Cold Dishes', 6.5, 10, 'It is Cold Dishes', 'Cold Dishes', 0 )");
            }catch (Exception e){
                e.printStackTrace();
            }
//
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }

        boolean check = true;
        Cursor cc = demoDB.rawQuery("SELECT COUNT(*) FROM menu", null);
        if (cc != null && cc.moveToFirst()) {
            check = (cc.getInt(0) == 0);
        }
        if (check == true) {
            Log.i("true", "true");
            try {
                demoDB.execSQL("CREATE TABLE IF NOT EXISTS menu (foodName VARCHAR,price NUMERIC,quantity INTEGER, description VARCHAR, category VARCHAR, mostSeller INTEGER, id INTEGER PRIMARY KEY)");
                demoDB.execSQL("INSERT INTO menu (foodName, price, quantity, description, category, mostSeller) " +
                        "VALUES ('Millet porridge', 1.5, 10, 'It is millet porridge', 'porridge', 1 )");
                demoDB.execSQL("INSERT INTO menu (foodName, price, quantity, description, category, mostSeller) " +
                        "VALUES ('Dumplings', 5.5, 10, 'It is dumplings', 'others', 1 )");
                demoDB.execSQL("INSERT INTO menu (foodName, price, quantity, description, category, mostSeller) " +
                        "VALUES ('Hot Dishes', 8.5, 10, 'It is Hot Dishes', 'Hot Dishes', 0 )");
                demoDB.execSQL("INSERT INTO menu (foodName, price, quantity, description, category, mostSeller) " +
                        "VALUES ('Cold Dishes', 6.5, 10, 'It is Cold Dishes', 'Cold Dishes', 0 )");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        specialList.clear();
        Cursor c = demoDB.rawQuery("SELECT * FROM menu", null);

        if (c != null && c.moveToFirst());
        do {
//            Log.i("foodName", c.getString(c.getColumnIndex("foodName")));
//            Log.i("price", Double.toString(c.getDouble(c.getColumnIndex("price"))));
//            Log.i("quantity", Integer.toString(c.getInt(c.getColumnIndex("quantity"))));
//            Log.i("description", c.getString(c.getColumnIndex("description")));
//            Log.i("category", c.getString(c.getColumnIndex("category")));
//            Log.i("mostSeller", Integer.toString(c.getInt(c.getColumnIndex("mostSeller"))));
            MenuItem item = new MenuItem(c.getString(c.getColumnIndex("foodName")), c.getString(c.getColumnIndex("description")),
                    c.getDouble(c.getColumnIndex("price")), c.getString(c.getColumnIndex("category")),
                    c.getInt(c.getColumnIndex("quantity")), c.getInt(c.getColumnIndex("mostSeller")));
            if (c.getInt(c.getColumnIndex("mostSeller")) == 1) {
                specialList.add(item);
            }
        } while (c.moveToNext());
        caGourmet.notifyDataSetChanged();

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_sidemenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//
//        if(id == R.id.startOrder){
//
//        }else if (id == R.id.orderHistory){
//
//        }else if(id == R.id.manageAccount){
//
//        }else if (id == R.id.setting){
//
//        }
//        return super.onContextItemSelected(item);
//    }
}


//



