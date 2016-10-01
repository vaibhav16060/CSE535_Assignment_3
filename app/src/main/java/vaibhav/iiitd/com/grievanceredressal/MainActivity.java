package vaibhav.iiitd.com.grievanceredressal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final String Pref = "UserDetail";
    SharedPreferences details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    void onButtonClick(View v){

        int btn_id = v.getId();
        switch(btn_id){
            case R.id.btn_register : Intent register = new Intent(MainActivity.this, Register.class);
                                     startActivity(register);
                                     break;

            case R.id.btn_login : details = getSharedPreferences(Pref, MODE_PRIVATE);
                                  EditText tb_username = (EditText)findViewById(R.id.tb_username);
                                  EditText tb_password = (EditText)findViewById(R.id.tb_password);
                                  String real_password = details.getString(tb_username.getText().toString(), "");
                                  if(tb_username.getText().toString().equals("") || tb_password.getText().toString().equals("")){
                                      Toast.makeText(MainActivity.this, "No field can be left blank", Toast.LENGTH_LONG).show();
                                  }
                                  else if(tb_username.getText().toString().toLowerCase().equals("user") && tb_password.getText().toString().toLowerCase().equals("user")){
                                      Intent user_login = new Intent(MainActivity.this, UserScreen.class);
                                      startActivity(user_login);
                                  }
                                  else if(tb_password.getText().toString().equals(real_password)){
                                      Intent admin_login = new Intent(MainActivity.this, AdminScreen.class);
                                      startActivity(admin_login);
                                  }
                                  else{
                                      Toast.makeText(MainActivity.this, "This login in not recognized by us !", Toast.LENGTH_LONG).show();
                                  }
                                  break;
        }
    }
}
