package com.example.nounou.tp2;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nounou.tp2.R;

public class Main2Activity extends AppCompatActivity {


     Button deleteButton ;
     Button updateButton ;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        EditText name = (EditText) findViewById(R.id.editName);
        EditText surname =(EditText) findViewById(R.id.editSurname);
        EditText town =(EditText) findViewById(R.id.editTown);
        EditText dateOfBirth = (EditText) findViewById(R.id.editDateOfBirth);

         deleteButton = (Button) findViewById(R.id.buttonDelete);
         updateButton = (Button) findViewById(R.id.buttonUpdate);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id=extras.getString("id");
            name.setText(extras.getString("name"));
            surname.setText(extras.getString("surname"));
            town.setText(extras.getString("town"));
            dateOfBirth.setText(extras.getString("birth"));

        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                DeleteCustomer();
                Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
                Toast.makeText(Main2Activity.this, "Client supprimé",
                        Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();


            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                UpdateCustomer();
                Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
                Toast.makeText(Main2Activity.this, "Client modifié",
                        Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();

            }
        });


    }


    private void DeleteCustomer() {
        ContentValues client = new ContentValues();
        getContentResolver().delete(MyContentProvider.CONTENT_URI, SharedIformation.Customers.Customer_ID+ "=" +id,null);
    }

    private void UpdateCustomer() {
        EditText name = (EditText) findViewById(R.id.editName);
        EditText surname = (EditText) findViewById(R.id.editSurname);
        EditText town = (EditText) findViewById(R.id.editTown);
        EditText dateOfBirth = (EditText) findViewById(R.id.editDateOfBirth);


        ContentValues client = new ContentValues();
        client.put(SharedIformation.Customers.Customer_NAME, name.getText().toString());
        client.put(SharedIformation.Customers.Customer_SURNAME, surname.getText().toString());
        client.put(SharedIformation.Customers.Customer_TOWN, town.getText().toString());
        client.put(SharedIformation.Customers.Customer_DATEOFBORTH, dateOfBirth.getText().toString());

        getContentResolver().update(MyContentProvider.CONTENT_URI, client, SharedIformation.Customers.Customer_ID+ "=" +id,null);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
