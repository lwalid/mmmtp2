package com.example.nounou.tp2;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import javax.xml.validation.Validator;

import com.example.nounou.tp2.R;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    /** Called when the activity is first created. */

    private boolean added = false;

    EditText name, surname,town;
    String departement;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText phoneNumber = (EditText) findViewById(R.id.editPhone);
        phoneNumber.setVisibility(View.GONE);
        final Button submit = (Button) findViewById(R.id.button);
        final EditText dateOfBirth = (EditText) findViewById(R.id.editDateOfBirth);


        //nous avons ajouté un  ClickListener sur le editText dateOfBirth
        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            // @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int y = c.get(Calendar.YEAR);
                int m = c.get(Calendar.MONTH);
                int d = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dp = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String date = "";
                                date = String.valueOf(dayOfMonth);
                                date += "-" + String.valueOf(monthOfYear);
                                date += "-" + year;

                                 dateOfBirth.setText(date);

                            }

                        }, y, m, d);

                dp.show();



            }
        });


        submit.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {


                if(insertRecords()) {
                    Intent intent = new Intent(MainActivity.this, Main3Activity.class);
                    Toast.makeText(MainActivity.this, "Client ajouté",
                            Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }
      /* test second commit */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_raz:
                EditText name = (EditText) findViewById(R.id.editName);
                EditText surname = (EditText) findViewById(R.id.editSurname);
                EditText town = (EditText) findViewById(R.id.editTown);
                EditText dateOfBirth = (EditText) findViewById(R.id.editDateOfBirth);

                name.setText("");
                surname.setText("");
                dateOfBirth.setText("");
                town.setText("");
                break;
            case R.id.action_phone:

                EditText phoneNumber = (EditText) findViewById(R.id.editPhone);
                phoneNumber.setVisibility(View.VISIBLE);
                item.setEnabled(false);
                break;

            case R.id.action_browser:
                Spinner departement = (Spinner) findViewById(R.id.spinner);
                Intent recherche = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://fr.wikipedia.org/wiki/"
                                + ((String) departement.getSelectedItem())));
                startActivity(recherche);


                break;

        }
        return true;
    }

    private boolean insertRecords() {
        EditText name = (EditText) findViewById(R.id.editName);
        EditText surname = (EditText) findViewById(R.id.editSurname);
        EditText town = (EditText) findViewById(R.id.editTown);
        EditText dateOfBirth = (EditText) findViewById(R.id.editDateOfBirth);
        boolean r=false;
        boolean x,y,z,v;
        x=validateForNull(name,"Champ obligatoire");
        y=validateForNull(surname,"Champ obligatoire");
        z= validateForNull(town,"Champ obligatoire");
        v= validateForNull(dateOfBirth,"Champ obligatoire");
        if(x &&  y && z && v) {
            ContentValues client = new ContentValues();
            client.put(SharedIformation.Customers.Customer_NAME, name.getText().toString());
            client.put(SharedIformation.Customers.Customer_SURNAME, surname.getText().toString());
            client.put(SharedIformation.Customers.Customer_TOWN, town.getText().toString());
            client.put(SharedIformation.Customers.Customer_DATEOFBORTH, dateOfBirth.getText().toString());
            getContentResolver().insert(MyContentProvider.CONTENT_URI, client);
            r=true;
        }

      return  r;

    }

    private boolean validateForNull(EditText p_editText, String p_nullMsg)
    {
        boolean m_isValid = false;
        try
        {
            if (p_editText != null && p_nullMsg != null)
            {
                if (TextUtils.isEmpty(p_editText.getText().toString().trim()))
                {
                    p_editText.setError(p_nullMsg);
                    m_isValid = false;
                }
                else
                {
                    m_isValid = true;
                }
            }
        }
        catch(Throwable p_e)
        {
            p_e.printStackTrace();
        }
        return m_isValid;
    }
}
