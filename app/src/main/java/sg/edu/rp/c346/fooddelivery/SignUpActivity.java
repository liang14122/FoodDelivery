package sg.edu.rp.c346.fooddelivery;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity {
    EditText etName, etEmail, etPasswrod, etConfirm;
    Button btnRegister;
    DatePicker dp;

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etName = (EditText) findViewById(R.id.editTextName);
        etPasswrod = (EditText) findViewById(R.id.editTextPassword);
        etConfirm = (EditText) findViewById(R.id.editTextConfirm);
        etEmail = (EditText) findViewById(R.id.editTextEmail);
        btnRegister = (Button) findViewById(R.id.buttonRegister);
        dp = (DatePicker)findViewById(R.id.datePicker);

        Calendar calendar = Calendar.getInstance() ;
        dp.setMaxDate(calendar.getTimeInMillis());

        final SQLiteDatabase demoDB = this.openOrCreateDatabase("FoodDelivery", MODE_PRIVATE, null);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = etPasswrod.getText().toString();
                String confirm = etConfirm.getText().toString();
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String dateOfBirth = Integer.toString(dp.getYear()) + "-" + (Integer.toString(dp.getMonth()+1)) + "-"+Integer.toString(dp.getDayOfMonth());
                boolean checkPassword = password.equals(confirm);
                boolean checkSubmission = true;
                if (name.length() == 0 || email.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please fill all field!", Toast.LENGTH_SHORT).show();
                    checkSubmission = false;
                }
                if (isValidEmail(email) == false) {
                    Toast.makeText(getApplicationContext(), "Please enter email address in correct format!", Toast.LENGTH_SHORT).show();
                    checkSubmission = false;
                }
                if (password.length() == 0 || confirm.length() == 0 || checkPassword == false) {
                    Toast.makeText(getApplicationContext(), "Double check your password!", Toast.LENGTH_SHORT).show();
                    checkSubmission = false;
                }
                if (checkSubmission == true) {
                    try {
                        demoDB.execSQL("CREATE TABLE IF NOT EXISTS users (name VARCHAR,email VARCHAR,password VARCHAR,dateOfBirth text,id INTEGER PRIMARY KEY)");
                        demoDB.execSQL("INSERT INTO users (name, email, password, dateOfBirth) " +
                                "VALUES ('"+name+"', '"+email+"', '"+password+"', '"+dateOfBirth+"')");
//                        demoDB.execSQL("DELETE FROM users");
                        Cursor c = demoDB.rawQuery("SELECT * FROM users", null);
//                        c.moveToFirst();
//                        int nameIndex = c.getColumnIndex("name");
//                        int passwordIndex = c.getColumnIndex("password");
//                        int emailIndex = c.getColumnIndex("email");
//                        int idIndex = c.getColumnIndex("id");
//                        int birthIndex = c.getColumnIndex("dateOfBirth");
//
//
//                        while (c != null) {
//                            Log.i("ID", Integer.toString(c.getInt(idIndex)));
//                            Log.i("Name", c.getString(nameIndex));
//                            Log.i("Password", c.getString(passwordIndex));
//                            Log.i("Email", c.getString(emailIndex));
//                            Log.i("Birth", c.getString(birthIndex));
//                            c.moveToNext();
//                        }

                        if(c != null && c.moveToFirst());
                        do{
                            Log.i("ID", Integer.toString(c.getInt(c.getColumnIndex("id"))));
                            Log.i("Name", c.getString(c.getColumnIndex("name")));
                            Log.i("Password", c.getString(c.getColumnIndex("password")));
                            Log.i("Email", c.getString(c.getColumnIndex("email")));
                            Log.i("Birth", c.getString(c.getColumnIndex("dateOfBirth")));
                        }while (c.moveToNext());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Intent intentPassData = new Intent(getBaseContext(),
                            WelcomeActivity.class);
                    intentPassData.putExtra("name", name);
                    intentPassData.putExtra("password", password);

                    startActivity(intentPassData);
                }
            }
        });
    }
}



