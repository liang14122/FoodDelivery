package sg.edu.rp.c346.fooddelivery.Activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import sg.edu.rp.c346.fooddelivery.Adapter.ConfirmCustomAdapter;
import sg.edu.rp.c346.fooddelivery.Object.OrderItem;
import sg.edu.rp.c346.fooddelivery.R;

public class ConfirmActivity extends AppCompatActivity {

    ListView lvOrder;
    ConfirmCustomAdapter ccaOrder;
    ArrayList<OrderItem> orderList = new ArrayList<>();
    TextView tvNum;
    TextView tvAmount;
    LinearLayout llConfirm;
    Button btnComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        lvOrder = (ListView) findViewById(R.id.listViewOrder);
        ccaOrder = new ConfirmCustomAdapter(ConfirmActivity.this, R.layout.confirm_row, orderList);
        lvOrder.setAdapter(ccaOrder);
        tvNum = (TextView) findViewById(R.id.textViewConfirmNumOfItem);
        tvAmount = (TextView) findViewById(R.id.textViewConfirmTotalPrice);
        llConfirm = (LinearLayout) findViewById(R.id.linearLayoutDetails);
        btnComplete = (Button) findViewById(R.id.buttonConfrim);

        final SQLiteDatabase demoDB = getBaseContext().openOrCreateDatabase("FoodDelivery", MODE_PRIVATE, null);

                demoDB.execSQL("CREATE TABLE IF NOT EXISTS menu (foodName VARCHAR,price NUMERIC,quantity INTEGER, description VARCHAR, category VARCHAR,mostSeller INTEGER,id INTEGER PRIMARY KEY)");
                Cursor c = demoDB.rawQuery("SELECT * FROM orderTable", null);


                    if (c != null && c.moveToFirst()) ;
                    do {
                        Log.i("foodName", c.getString(c.getColumnIndex("foodName")));
                        Log.i("price", Double.toString(c.getDouble(c.getColumnIndex("price"))));
                        Log.i("quantity", Integer.toString(c.getInt(c.getColumnIndex("quantity"))));

                        String foodName = c.getString(c.getColumnIndex("foodName"));
                        double price = c.getDouble(c.getColumnIndex("price"));
                        int qty = c.getInt(c.getColumnIndex("quantity"));

                        OrderItem item = new OrderItem(foodName, qty, price);
                        orderList.add(item);
                        ccaOrder.notifyDataSetChanged();
                    } while (c.moveToNext());




        double amount = 0.0;
        int num = 0;
        for (int i = 0; i < orderList.size(); i++) {
            OrderItem currentItem = orderList.get(i);
            int itemQty = currentItem.getQuantity();
            amount += itemQty * currentItem.getPrice();
            num += itemQty;
        }

        tvNum.setText(num + " items");
        tvAmount.setText("Total $" + amount);


        llConfirm.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View view, MotionEvent motionEvent) {

                Log.i("running", "ok");
                ArrayList<Double> priceSep = new ArrayList<Double>();
                ArrayList<Integer> qtySep = new ArrayList<Integer>();
                ArrayList<String> nameSep = new ArrayList<String>();
                double totalPrice = 0.0;
                int numItem = 0;
                try {
                    Cursor cOrder = demoDB.rawQuery("SELECT * FROM orderTable", null);
                    if (cOrder != null && cOrder.moveToFirst()) ;
                    do {
                        int itemQty = cOrder.getInt(cOrder.getColumnIndex("quantity"));
                        double itemPrice = cOrder.getDouble(cOrder.getColumnIndex("price"));
                        String itemName = cOrder.getString(cOrder.getColumnIndex("foodName"));
                        priceSep.add(itemPrice);
                        qtySep.add(itemQty);
                        nameSep.add(itemName);
                        totalPrice += itemPrice * itemQty;
                        Log.i("itemPrice", itemPrice + "");
                        Log.i("itemQtyoutput", itemQty + "");
                        numItem += itemQty;
                        Log.i("orderac", itemQty + itemPrice + itemName);
                        Log.i("totalPrice", totalPrice + "");
                    } while (cOrder.moveToNext());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("db", e.toString());
                }

                tvNum.setText(numItem + " Items");
                tvAmount.setText("Total $" + totalPrice);

                return false;

            }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener timeListener =
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int mins) {
                                Toast.makeText(ConfirmActivity.this, "Order Submitted successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ConfirmActivity.this, OverviewActivity.class);
                                startActivity(intent);
                            }
                        };

                Calendar calendar = Calendar.getInstance();

                TimePickerDialog timeDialog =
                        new TimePickerDialog(ConfirmActivity.this, timeListener,
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE), true);
                timeDialog.setTitle("Choose pick-up time");
                timeDialog.show();
            }
        });
    }
}
