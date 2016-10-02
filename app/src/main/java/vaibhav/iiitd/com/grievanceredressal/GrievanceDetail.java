package vaibhav.iiitd.com.grievanceredressal;

import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import android.util.Log;
import java.io.FileInputStream;
import android.content.DialogInterface;
import android.widget.Button;

public class GrievanceDetail extends AppCompatActivity {

    String id = null, name = null, filename = null, inserted_on=null, serviced_on=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievance_detail);
        BufferedReader br;
        String[] permission_arr = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, permission_arr, 1);
        Bundle data_from_main = getIntent().getExtras();
        if(data_from_main != null){
            id = data_from_main.getString("id");
            name = data_from_main.getString("name");
            filename = data_from_main.getString("filename");
            inserted_on = data_from_main.getString("inserted_on");
            serviced_on = data_from_main.getString("serviced_on");
        }

        EditText tb_id_display = (EditText)findViewById(R.id.tb_name_display);
        EditText tb_name_display = (EditText)findViewById(R.id.tb_id_display);
        EditText tb_grievance_display= (EditText)findViewById(R.id.tb_grievance_display);
        Button btn_resolved = (Button)findViewById(R.id.btn_resolved);
        if(serviced_on != null){
            btn_resolved.setEnabled(false);
        }
        tb_id_display.setText(id, TextView.BufferType.EDITABLE);
        tb_name_display.setText(name, TextView.BufferType.EDITABLE);
        String ret = "";
        String receiveString = "";
        //now try to read the file
        try {
            //reading the data from internal storage if it exists
            InputStream file = this.openFileInput((filename + ".txt"));
            if (file != null) {
                br = new BufferedReader(new InputStreamReader(file));
                StringBuilder sb = new StringBuilder();

                while ((receiveString = br.readLine()) != null) {
                    sb.append(receiveString);
                }

                file.close();
                ret = sb.toString();
            }
        }catch (FileNotFoundException e) {
            //read file from external storage
            try {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Greviance");
                    File file = new File(folder.getAbsolutePath(), (filename + ".txt"));
                    FileInputStream fis = new FileInputStream(file);
                    DataInputStream in = new DataInputStream(fis);
                    br = new BufferedReader(new InputStreamReader(in));
                    while ((receiveString = br.readLine()) != null) {
                        ret = ret + receiveString;
                    }
                    in.close();
                }
            }
            catch (FileNotFoundException f){
                Log.e("GrievanceDetail", "File cannotbe found");
            }
            catch (IOException g){
                Log.e("GrievanceDetail", "Inner IOException");
            }
        }
         catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
         }
        tb_grievance_display.setText(ret, TextView.BufferType.EDITABLE);
    }

    public void onResolvedButtonClick(View w){

        final Grievance db = new Grievance(this);
        db.markResolved(id, name, filename, inserted_on);
        new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Do you also want to delete this entry and the file?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db.delete(id, name, filename, inserted_on);
                        try{
                            File file = new File(getApplicationContext().getFilesDir(), (filename + ".txt"));
                            Boolean b = file.delete();
                        }
                        catch(Exception e){
                            try{
                                File folder= new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"Greviance");
                                folder.mkdirs();
                                File file= new File(folder.getAbsolutePath(), (filename + ".txt"));
                                file.delete();
                            }
                            catch(Exception f){
                                Log.e("not found", "not found");
                            }
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
