package brickbyte.hemabh.mayur.com.loginservlet;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {


    EditText userName,pass;
    Button login;
    String username;
    JSONparser jParser = new JSONparser();

    JSONObject json;
    private static String url_login ="http://10.0.2.2:8080/servlet-DB-connect/ServletTalk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName= (EditText) findViewById(R.id.username);
        pass= (EditText) findViewById(R.id.pass);
        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Login().execute();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }


    private class Login extends AsyncTask<String, String, String>{

        String s=null;
        @Override
        protected String doInBackground(String... params) {


            username = userName.getText().toString();
            String passKey;
            passKey = pass.getText().toString();

            List<NameValuePair> args = new ArrayList<>();
            args.add(new BasicNameValuePair("à",username));
            args.add(new BasicNameValuePair("p",passKey));

            json = jParser.makeHttpRequest(url_login, "POST", args);


            try {
                s= json.getString("info");

            } catch (JSONException e) {
                e.printStackTrace();

                Log.e("JSON exception","Error"+ e.toString());
            }


            return null;
        }

        @Override
        protected void onPostExecute(String p) {
            super.onPostExecute(s);

            if (s.equals("success")){
                Intent login = new Intent(getApplicationContext(), Welcome.class);
                login.putExtra("user",username);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
                finish();

            }
            else if (s.equals("fail")){
                Toast.makeText(getApplicationContext(),"Login Fail : username/password error",Toast.LENGTH_LONG).show();
            }

        }
    }



}
