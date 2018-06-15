package sg.edu.rp.c346.fooddelivery.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import sg.edu.rp.c346.fooddelivery.R;

public class DetailActivity extends AppCompatActivity {

    TextView tvName, tvDescription, tvPrice, tvQuantity;
    ImageView ivImage;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvName = (TextView)findViewById(R.id.textViewFoodName);
        tvDescription = (TextView)findViewById(R.id.textViewDescription);
        tvQuantity = (TextView)findViewById(R.id.textViewQuantity);
        tvPrice = (TextView)findViewById(R.id.textViewPrice);
        ivImage = (ImageView)findViewById(R.id.imageViewPicture);
        btnAdd = (Button)findViewById(R.id.buttonAdd);

        final Intent intent = getIntent();
        String foodName = intent.getStringExtra("foodName");

        tvName.setText(foodName);
        tvDescription.setText(intent.getStringExtra("description"));
        tvPrice.setText("Price: " + Double.toString(intent.getDoubleExtra("price", 0.0)));
        Log.i("foodName", intent.getStringExtra("foodName"));
        Log.i("price", Double.toString(intent.getDoubleExtra("price", 0.0)));
        Log.i("quantity", Integer.toString(intent.getIntExtra("quantity", 0)));
        Log.i("description", intent.getStringExtra("description"));
        tvQuantity.setText("Stock left: " + Integer.toString(intent.getIntExtra("quantity", 0)));
        if (foodName.equalsIgnoreCase("Millet porridge")) {
            ivImage.setImageResource(R.mipmap.millet_porridge);
        }else if(foodName.equalsIgnoreCase( "Dumplings")){
            ivImage.setImageResource(R.mipmap.dumplings);
        }else{
            ivImage.setImageResource(R.mipmap.no_image);
        }

        int quantity = intent.getIntExtra("quantity", 0);
        checkQuantity(quantity);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String foodName = intent.getStringExtra("foodName");
                int quantity = intent.getIntExtra("quantity", 0);
                if(quantity >0) {
                    quantity -= 1;
                    tvQuantity.setText("Stock left: " + Integer.toString(quantity));
                    Toast.makeText(getApplicationContext(), "Add one " + foodName + " to your cart!", Toast.LENGTH_SHORT).show();
                    checkQuantity(quantity);
                    btnAdd.setEnabled(false);
                }else{
                    Toast.makeText(getApplicationContext(), foodName + " is sold out", Toast.LENGTH_SHORT).show();
                }

                if(btnAdd.isEnabled() == false){
                    Toast.makeText(getBaseContext(), "You can change quantity in order page :)" ,Toast.LENGTH_LONG).show();
                }
            }


        });
    }
    private void checkQuantity(int quantity) {
        if(quantity == 0){
            tvQuantity.setTextColor(Color.parseColor("#FF0000"));
            btnAdd.setEnabled(false);
        }
    }
}
