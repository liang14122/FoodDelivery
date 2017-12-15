package sg.edu.rp.c346.fooddelivery;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class OverviewActivity extends AppCompatActivity {

    ListView lvOverview;
    Button btnHome;
    OverviewCustomAdapter ocaOverview;
    ArrayList<OrderItem> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        lvOverview = (ListView) findViewById(R.id.listViewOverview);
        btnHome = (Button) findViewById(R.id.buttonHome);
        orderList = new ArrayList<>();
        ocaOverview = new OverviewCustomAdapter(OverviewActivity.this, R.layout.overview_row, orderList);
        lvOverview.setAdapter(ocaOverview);

        final SQLiteDatabase demoDB = getBaseContext().openOrCreateDatabase("FoodDelivery", MODE_PRIVATE, null);
        boolean check = true;
        Cursor cc = demoDB.rawQuery("SELECT COUNT(*) FROM orderTable", null);
        if (cc != null && cc.moveToFirst()) {
            check = (cc.getInt(0) == 0);
        }
        if (check == false) {
            try {
                Cursor c = demoDB.rawQuery("SELECT * FROM orderTable", null);

                if (c != null && c.moveToFirst()) ;
                do {
                    String name = c.getString(c.getColumnIndex("foodName"));
                    int qty = c.getInt(c.getColumnIndex("quantity"));
                    double price = c.getDouble(c.getColumnIndex("price"));
                    OrderItem item = new OrderItem(name, qty, price);
                    orderList.add(item);
                    ocaOverview.notifyDataSetChanged();
                } while (c.moveToNext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Intent intent = new Intent(OverviewActivity.this, HomeActivity.class);
            startActivity(intent);
        }

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                demoDB.execSQL("DELETE FROM orderTable");
                Intent intent = new Intent(OverviewActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }
}
