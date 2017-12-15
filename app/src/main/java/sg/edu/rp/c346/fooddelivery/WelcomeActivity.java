package sg.edu.rp.c346.fooddelivery;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {
    EditText etName, etPassword;
    Button btnSignIn, btnSignUp;
    TextView tvError;

    @Override
    protected void onResume() {
        tvError.setText("");
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        etName = (EditText) findViewById(R.id.editTextName);
        etPassword = (EditText) findViewById(R.id.editTextPassword);
        btnSignIn = (Button) findViewById(R.id.buttonSignIn);
        tvError = (TextView)findViewById(R.id.textViewError);
        btnSignUp = (Button)findViewById(R.id.buttonSignUp);
//
        Intent intentReceived = getIntent();
        final String name = intentReceived.getStringExtra("name");
        final String password = intentReceived.getStringExtra("password");

        etName.setText(name);
        etPassword.setText(password);

        final SQLiteDatabase demoDB = this.openOrCreateDatabase("FoodDelivery", MODE_PRIVATE, null);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    try {
                        demoDB.execSQL("CREATE TABLE IF NOT EXISTS users (name VARCHAR,email VARCHAR,password VARCHAR,dateOfBirth text,id INTEGER PRIMARY KEY)");

//                        demoDB.execSQL("DELETE FROM users");
                        Cursor c = demoDB.rawQuery("SELECT * FROM users WHERE name='" + etName.getText().toString() + "' AND password='" + etPassword.getText().toString() + "'", null);


//                        c.moveToFirst();
//                        while (c != null && c.getCount() != 0) {
//                            int nameIndex = c.getColumnIndex("name");
//                            int passwordIndex = c.getColumnIndex("password");
//                            int emailIndex = c.getColumnIndex("email");
//                            int idIndex = c.getColumnIndex("id");
//                            int birthIndex = c.getColumnIndex("dateOfBirth");
//                            Log.i("ID", Integer.toString(c.getInt(idIndex)));
//                            Log.i("Name", c.getString(nameIndex));
//                            Log.i("Password", c.getString(passwordIndex));
//                            Log.i("Email", c.getString(emailIndex));
//                            Log.i("Birth", c.getString(birthIndex));
//                            check = true;
//                            c.moveToNext();
//                        }

                        int numberOfRows = c.getCount();

                        if(numberOfRows == 0){
                            tvError.setText("Please check your name or password!");
                        }else{
                            if (c != null && c.moveToFirst());
                            do{
                                Log.i("ID", Integer.toString(c.getInt(c.getColumnIndex("id"))));
                                Log.i("Name", c.getString(c.getColumnIndex("name")));
                                Log.i("Password", c.getString(c.getColumnIndex("password")));
                                Log.i("Email", c.getString(c.getColumnIndex("email")));
                            }while (c.moveToNext());
                            Intent intentHome = new Intent(getBaseContext(),
                                    HomeActivity.class);
                            startActivity(intentHome);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSignUp = new Intent(getBaseContext(), SignUpActivity.class);
                startActivity(intentSignUp);
            }
        });
    }
}
