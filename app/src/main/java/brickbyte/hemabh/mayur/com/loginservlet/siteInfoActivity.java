package brickbyte.hemabh.mayur.com.loginservlet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayursanjaydevgaonkar on 02/07/15.
 */
public class siteInfoActivity extends ActionBarActivity {

    ListView siteInfoList;
    JSONObject jsonSiteInfo;
    JSONparser jParserSiteInfo=new JSONparser();
    String url_siteInfo= "http://10.0.2.2:8080/servlet-DB-connect/siteListerServlet";
    String rtuID;

    TextView tv;
    String timestamp;

    siteInfoListAdapter siteInfoLister;

    String[] finalsiteInfoList;

    String[] params = new String[]{"oil_pressure", "coolant_temperature", "dg1_fuel_level_per", "dg1_battery_voltage", "coolant_pressure1",
            "inlet_manfold_temp1", "engine_run_time", "dg_kvah", "number_of_starts", "fuel_used"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.site_info);

        Intent getID=getIntent();
        Bundle ID=getID.getExtras();

        rtuID = ID.getString("site_id");



        tv = (TextView) findViewById(R.id.tvstr);


        siteInfoList= (ListView) findViewById(R.id.siteInfoList);

        siteInfoList.setEmptyView(tv);
        new getInfo().execute();

        siteInfoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position != 10){

                    String title=((TextView) findViewById(R.id.paramName)).getText().toString();
                    Intent paramGraphIntent=new Intent(getApplicationContext(),paramGraph.class);
                    paramGraphIntent.putExtra("title",position);
                    paramGraphIntent.putExtra("param",params[position]);
                    paramGraphIntent.putExtra("rtuID",rtuID);
                    paramGraphIntent.putExtra("timestamp",timestamp);
                    startActivity(paramGraphIntent);
                    Toast.makeText(getApplicationContext(),params[position]+" : "+timestamp,Toast.LENGTH_LONG).show();

                }
                else Toast.makeText(getApplicationContext(),"choose a valid parameter",Toast.LENGTH_LONG).show();
            }
        });




    }

   private class getInfo extends AsyncTask<String, String, String> {

        String s1=null;
        private ProgressDialog dialog =
                new ProgressDialog(siteInfoActivity.this);

        @Override
        protected void onPreExecute() {

            dialog.setMessage("Getting data... Please wait...");
            dialog.show();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            List<NameValuePair> args = new ArrayList<>();
            args.add(new BasicNameValuePair("n",rtuID));
            jsonSiteInfo = jParserSiteInfo.makeHttpRequest(url_siteInfo, "GET", args);

            s1=jsonSiteInfo.toString();



            return null;
        }

        @Override
        protected void onPostExecute(String p) {
            super.onPostExecute(s1);


            finalsiteInfoList = JSONtoJavaArray(s1);
            siteInfoLister=new siteInfoListAdapter(getApplicationContext(),R.layout.site_info_row,finalsiteInfoList);
            dialog.dismiss();
            siteInfoList.setAdapter(siteInfoLister);

        }

        private String[] JSONtoJavaArray(String s1) {

            JSONObject recievedJSONObj=null;

            String[] siteInfoList={};
            try {
                recievedJSONObj=new JSONObject(s1);
                JSONArray tempArray;
                tempArray = recievedJSONObj.getJSONArray("SiteInfo");
                String arrayString=tempArray.toString();

               if (!arrayString.matches("")) {
                    int i;
                    for (i = 0; i < tempArray.length(); i++) {

                        JSONObject tempObj = tempArray.getJSONObject(i);
                        site tempSiteInfo = new site();

                        String oil_pressure = tempObj.getString("oil_pressure");
                        String coolant_temperature = tempObj.getString("coolant_temperature");
                        String fuel_level = tempObj.getString("fuel_level");
                        String dg1_battery_voltage = tempObj.getString("dg1_battery_voltage");
                        String coolant_pressure1 = tempObj.getString("coolant_pressure1");
                        String inlet_manfold_temp1 = tempObj.getString("inlet_manfold_temp1");
                        String engine_run_time = tempObj.getString("engine_run_time");
                        String dg_kvah = tempObj .getString("dg_kvah");
                        String number_of_starts = tempObj.getString("number_of_starts");
                        String fuel_used = tempObj.getString("fuel_used");
                        String data_time = tempObj.getString("data_time");
                        timestamp = data_time;
                        siteInfoList = new String[]{oil_pressure, coolant_temperature, fuel_level,  dg1_battery_voltage, coolant_pressure1, inlet_manfold_temp1, engine_run_time, dg_kvah, number_of_starts, fuel_used, data_time};

                        tempSiteInfo.setSiteParams(siteInfoList);


                    }
                }else {
                    //siteInfoList.clear();
                    Toast.makeText(getApplicationContext(), "Error occured", Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return siteInfoList;
        }
    }




    private class siteInfoListAdapter extends ArrayAdapter<String> {

         String[] paramList;

        public siteInfoListAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);

            this.paramList= objects;
        }







        public  class ViewHolder{
            TextView parameterName,parameterValue;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {




            ViewHolder holder=null;
            if (convertView==null){

                LayoutInflater makeRow= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView=makeRow.inflate(R.layout.site_info_row,null);

                holder=new ViewHolder();
                holder.parameterName= (TextView) convertView.findViewById(R.id.paramName);
                holder.parameterValue= (TextView) convertView.findViewById(R.id.paramValue);



                convertView.setTag(holder);

            }else holder= (ViewHolder) convertView.getTag();

            String tempSite=paramList[position];
            switch (position){

                case 0:
                    holder.parameterName.setText("Oil Pressure");
                    if (tempSite!=null)
                    holder.parameterValue.setText(tempSite);
                    else holder.parameterValue.setText("null/NA");

                    break;
                case 1:
                    holder.parameterName.setText("Coolant Temperature");
                    if (tempSite!=null)
                        holder.parameterValue.setText(tempSite);
                    else holder.parameterValue.setText("null/NA");
                    break;
                case 2:
                    holder.parameterName.setText("Fuel Level");
                    if (tempSite!=null)
                        holder.parameterValue.setText(tempSite);
                    else holder.parameterValue.setText("null/NA");
                    break;
                case 3:
                    holder.parameterName.setText("Battery Voltage");
                    if (tempSite!=null)
                        holder.parameterValue.setText(tempSite);
                    else holder.parameterValue.setText("null / NA");
                    break;
                case 4:
                    holder.parameterName.setText("Coolant Pressure1");
                    if (tempSite!=null)
                        holder.parameterValue.setText(tempSite);
                    else holder.parameterValue.setText("null/NA");
                    break;
                case 5:
                    holder.parameterName.setText("Inlet Manifold Temperature");
                    if (tempSite!=null)
                        holder.parameterValue.setText(tempSite);
                    else holder.parameterValue.setText("null/NA");
                    break;
                case 6:
                    holder.parameterName.setText("Engine Runtime");
                    if (tempSite!=null)
                        holder.parameterValue.setText(tempSite);
                    else holder.parameterValue.setText("null/NA");
                    break;
                case 7:
                    holder.parameterName.setText("DG kVAh");
                    if (tempSite!=null)
                        holder.parameterValue.setText(tempSite);
                    else holder.parameterValue.setText("null/NA");
                    break;
                case 8:
                    holder.parameterName.setText("No. of Starts");
                    if (tempSite!=null)
                        holder.parameterValue.setText(tempSite);
                    else holder.parameterValue.setText("null/NA");
                    break;
                case 9:
                    holder.parameterName.setText("Fuel used");
                    if (tempSite!=null)
                        holder.parameterValue.setText(tempSite);
                    else holder.parameterValue.setText("null/NA");
                    break;
                case 10:
                    holder.parameterName.setText("Data time");
                    if (tempSite!=null){
                        String time=tempSite;
                       // timestamp = tempSite;
                        long t= Long.valueOf(time);
                        Timestamp ti= new Timestamp(t*1000);
                        String stamp=ti.toString();

                        holder.parameterValue.setText(stamp);}
                    else holder.parameterValue.setText("null/NA");
                    break;
            }

            return convertView;

        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }
    }

}
