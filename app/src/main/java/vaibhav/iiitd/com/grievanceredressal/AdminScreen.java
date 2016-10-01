package vaibhav.iiitd.com.grievanceredressal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.view.View;
import android.content.Intent;

public class AdminScreen extends AppCompatActivity {

    String[][] all_complains = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_screen);
        Grievance db = new Grievance(this);
        all_complains = db.getAllGrievances();
        int i;
        String[] display_list = new String[all_complains.length];
        for(i = 0 ; i < all_complains.length ; i++){
            display_list[i] = all_complains[i][1];
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.populate_list, display_list);
        ListView listView = (ListView) findViewById(R.id.lv_complaints);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(AdminScreen.this, GrievanceDetail.class);
                intent.putExtra("id", all_complains[position][0]);
                intent.putExtra("name", all_complains[position][1]);
                intent.putExtra("filename", all_complains[position][2]);
                intent.putExtra("inserted_on", all_complains[position][3]);
                startActivity(intent);
            }
        });
    }
}
