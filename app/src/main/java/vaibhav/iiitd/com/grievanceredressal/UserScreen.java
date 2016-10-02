package vaibhav.iiitd.com.grievanceredressal;

import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.Manifest;

public class UserScreen extends AppCompatActivity {

    Grievance db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);
        String[] permission_arr = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, permission_arr, 1);
    }

    void onSubmitButtonPressed(View v){

        File file;
        FileOutputStream outputStream;
        //Put value in sqlite database and also make a file in storage and save the file in the database
        db = new Grievance(this);
        EditText tb_enrollment = (EditText)findViewById(R.id.tb_enrollment);
        EditText tb_full_name = (EditText)findViewById(R.id.tb_full_name);
        EditText tb_grievance = (EditText)findViewById(R.id.tb_grievance);
        String s = db.getMaxFileValue();

        int max_file = (s != null) ? Integer.parseInt(s) : 0;
        String insert_file_name = (max_file + 1) + "";

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //External storage is available so write file to external storage
            File folder= new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"Greviance");
            folder.mkdirs();
            file= new File(folder.getAbsolutePath(), (insert_file_name + ".txt"));
            try {
                file.createNewFile();
                //outputStream = openFileOutput((insert_file_name + ".txt"), getApplicationContext().MODE_PRIVATE);

                outputStream = new FileOutputStream(file);
                outputStream.write(tb_grievance.getText().toString().getBytes());
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            //write files to internal storage
            file = new File(getApplicationContext().getFilesDir(), (insert_file_name + ".txt"));
            try {
                outputStream = openFileOutput((insert_file_name + ".txt"), getApplicationContext().MODE_PRIVATE);
                outputStream.write(tb_grievance.getText().toString().getBytes());
                outputStream.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }

        }
        db.insertGrievance(tb_enrollment.getText().toString(), tb_full_name.getText().toString(), insert_file_name);
        tb_enrollment.setText("");
        tb_full_name.setText("");
        tb_grievance.setText("");
        Toast.makeText(UserScreen.this, "The data has been succcesfully saved and will be resolved by the Admin!", Toast.LENGTH_LONG).show();
    }
}
