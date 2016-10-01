package vaibhav.iiitd.com.grievanceredressal;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;

public class Register extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    final String Pref = "UserDetail";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    void onRegisterButtonClick(View v){

        EditText tb_user = (EditText)findViewById(R.id.tb_user);
        EditText tb_pass = (EditText)findViewById(R.id.tb_pass);

        sharedpreferences = getSharedPreferences(Pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(tb_user.getText().toString(), tb_pass.getText().toString());
        editor.commit();
        Toast.makeText(Register.this, "The Username and Password is registered", Toast.LENGTH_LONG).show();

    }
}
