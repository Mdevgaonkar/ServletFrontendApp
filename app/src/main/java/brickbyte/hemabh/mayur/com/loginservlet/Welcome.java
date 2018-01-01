package brickbyte.hemabh.mayur.com.loginservlet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayursanjaydevgaonkar on 29/06/15.
 */
public class Welcome extends ActionBarActivity {
    String user;
    JSONObject json;
    JSONparser jParser=new JSONparser();
    ArrayList<site> finalSiteList;

    TextView tv;
    private static String url_siteList ="http://10.0.2.2:8080/servlet-DB-connect/siteListerServlet";

    ListView siteListView;
    siteListAdapter siteLister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        Intent getI= getIntent();
        Bundle b =getI.getExtras();

        user = b.getString("user");

        siteListView= (ListView) findViewById(R.id.siteList);

        tv= (TextView) findViewById(R.id.getjson);
        siteListView.setEmptyView(tv);
        new Login().execute();

        siteListView.setTextFilterEnabled(true);
        siteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                String name=((TextView) view.findViewById(R.id.siteID)).getText().toString();
                Intent getInfo=new Intent(getApplicationContext(),siteInfoActivity.class);
                getInfo.putExtra("site_id",name);
                startActivity(getInfo);
                Toast.makeText(getApplicationContext(),name+" :"+user,Toast.LENGTH_LONG).show();

            }
        });






    }

    private class Login extends AsyncTask<String, String, String> {

        String s=null;
        private ProgressDialog dialog =
                new ProgressDialog(Welcome.this);

        @Override
        protected void onPreExecute() {

            dialog.setMessage("Getting your data... Please wait...");
            dialog.show();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            List<NameValuePair> args = new ArrayList<>();
            args.add(new BasicNameValuePair("u",user));
            args.add(new BasicNameValuePair("f","Y"));
            args.add(new BasicNameValuePair("gid","4"));
            json = jParser.makeHttpRequest(url_siteList, "GET", args);

            s=json.toString();



            return null;
        }

        @Override
        protected void onPostExecute(String p) {
            super.onPostExecute(s);
           // tv.setText(s);
            finalSiteList = JSONtoJavaArray(s);
            siteLister=new siteListAdapter(getApplicationContext(),R.layout.sites_row,finalSiteList);
            dialog.dismiss();
            siteListView.setAdapter(siteLister);

        }

        private ArrayList<site> JSONtoJavaArray(String s) {

            JSONObject recievedJSONObj=null;

            ArrayList<site> siteList=new ArrayList();
            try {
                recievedJSONObj=new JSONObject(s);
                JSONArray tempArray;
                tempArray = recievedJSONObj.getJSONArray("Sites");
                String arrayString=tempArray.toString();
                if (!arrayString.matches("")) {
                    int i;
                    for (i = 0; i < tempArray.length(); i++) {

                        JSONObject tempObj = tempArray.getJSONObject(i);
                        site tempSite = new site();
                        tempSite.setSiteName(tempObj.getString("siteName"));

                        tempSite.setSiteLocation(tempObj.getString("siteLocation"));

                        tempSite.setSiteID(tempObj.getString("siteID"));

                        siteList.add(tempSite);

                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Error occured",Toast.LENGTH_LONG);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return siteList;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {




            Intent login = new Intent(getApplicationContext(), MainActivity.class);
            login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class siteListAdapter extends ArrayAdapter<site>{

        private  ArrayList<site> siteList;

        public siteListAdapter(Context context, int resource, ArrayList<site> objects) {
            super(context, resource, objects);

            this.siteList=new ArrayList<site>();
            this.siteList.addAll(objects);

        }

        public  class ViewHolder{
            TextView siteName,siteLocation,siteID;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            String SITE_LOCATION="Location : ";
            String SITE_ID="ID : ";

            ViewHolder holder=null;
            if (convertView==null){

                LayoutInflater makeRow= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView=makeRow.inflate(R.layout.sites_row,null);

                holder=new ViewHolder();
                holder.siteID= (TextView) convertView.findViewById(R.id.siteID);
                holder.siteLocation= (TextView) convertView.findViewById(R.id.siteLocation);
                holder.siteName= (TextView) convertView.findViewById(R.id.siteName);



                convertView.setTag(holder);

            }else holder= (ViewHolder) convertView.getTag();

            site tempSite=siteList.get(position);
            holder.siteName.setText(tempSite.getSiteName());
            holder.siteLocation.setText(SITE_LOCATION+tempSite.getSiteLocation());
            holder.siteID.setText(tempSite.getSiteID());


            return convertView;

        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }
    }

}
