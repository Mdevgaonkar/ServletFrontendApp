package brickbyte.hemabh.mayur.com.loginservlet;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mayursanjaydevgaonkar on 07/07/15.
 */
public class paramGraph extends ActionBarActivity {

    String timestamp,param,rtuID;
    int title;
    int dur=10;
    String url_siteParamInfo= "http://10.0.2.2:8080/servlet-DB-connect/siteListerServlet";

    String[] Lbles = new String[]{

            "Oil Pressure", "Coolant Temperature", "Fuel Level", "Battery Voltage", "Coolant Pressure1", "Inlet Manifold Temperature",
            "Engine Runtime", "DG kVAh", "No. of Starts", "Fuel used"
    };

    JSONparser paramparser =  new JSONparser();

    JSONObject paramSeriesJSONObject;


    double DateRangeLow=0,DateRangeHigh=0;

    GraphView paramGraphview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parameter_graph);

        Intent getparam=getIntent();
        Bundle b=getparam.getExtras();

        title=b.getInt("title");
        param = b.getString("param");
        rtuID = b.getString("rtuID");
        timestamp = b.getString("timestamp");
        getSupportActionBar().setTitle(Lbles[title]);

     //   a.hide();

        paramGraphview= (GraphView) findViewById(R.id.paramGraph);
        //tvp = (TextView) findViewById(R.id.testtext);
        //tvp.setText(param + " : " + timestamp);


        new getInfo().execute();






    }

    private class getInfo extends AsyncTask<String, String, String> {

        String s2=null;
        private ProgressDialog dialog =
                new ProgressDialog(paramGraph.this);

        @Override
        protected void onPreExecute() {

            dialog.setMessage("Getting data... Please wait...");
            dialog.show();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            List<NameValuePair> args = new ArrayList<>();
            args.add(new BasicNameValuePair("param",param));
            args.add(new BasicNameValuePair("rtuID",rtuID));
            String timefrom,timeto;
            timeto = timestamp;
            long timetemp = Long.valueOf(timeto);
            timefrom =""+(timetemp-(3600 * dur));
            args.add(new BasicNameValuePair("timefrom",timefrom));
            args.add(new BasicNameValuePair("timeto", timeto));
            paramSeriesJSONObject = paramparser.makeHttpRequest(url_siteParamInfo, "GET", args);

            s2=paramSeriesJSONObject.toString();



            return null;
        }

        @Override
        protected void onPostExecute(String p) {
            super.onPostExecute(s2);




            LineGraphSeries<DataPoint> paramsDataPointSeries=JSONtoDataSeries(s2);
            //LineGraphSeries<DataPoint> paramsDataPointSeries=new LineGraphSeries<>();
            paramGraphview.addSeries(paramsDataPointSeries);
            Viewport graphPort = paramGraphview.getViewport();
            graphPort.setScrollable(true);
            graphPort.setScalable(true);

            paramGraphview.getGridLabelRenderer().setLabelFormatter(new LabelFormatter() {
                @Override
                public String formatLabel(double v, boolean b) {
                    if (b) {
                        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:MM:ss.S");
                        Date d = new Date((long) (v*1000));
                        return (sdf.format(d));
                    }
                    return "" + (int) v;
                }

                @Override
                public void setViewport(Viewport viewport) {

                }
            } );
           // paramGraphview.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(paramGraph.this));
            paramGraphview.getGridLabelRenderer().setNumHorizontalLabels(2); // only 4 because of the space

// set manual x bounds to have nice steps
           // paramGraphview.getViewport().setMinX(DateRangeLow);
           // paramGraphview.getViewport().setMaxX(DateRangeLow);
           // paramGraphview.getViewport().setXAxisBoundsManual(true);

            dialog.dismiss();



        }

        private LineGraphSeries<DataPoint> JSONtoDataSeries(String s2) {

            JSONObject recievedJSONObj=null;

            LineGraphSeries<DataPoint> finalDataSeries = null;


            try {
                recievedJSONObj=new JSONObject(s2);
                JSONArray tempArray;
                tempArray = recievedJSONObj.getJSONArray("Data Series");
                DataPoint[] paramDataArray = new DataPoint[tempArray.length()];
                String arrayString=tempArray.toString();



                if (!arrayString.matches("")) {
                    int i;
                    for (i = 0; i < tempArray.length(); i++) {

                        JSONObject tempObj = tempArray.getJSONObject(i);
                        Date date = null;
                        double value = 0;

                        // format received timestamp to show on graph
                        String stampTemp = tempObj.getString("paramTime");
                        long t= Long.valueOf(stampTemp);


                        // Format received value to show on graph
                        String valueTemp = tempObj.getString("paramValue");
                        if (valueTemp.matches("null"))
                            value = 0;
                        else value = Double.valueOf(valueTemp);

                        paramDataArray[i]= new DataPoint(t,value);
                        //finalDataSeries.appendData(new DataPoint(t, value), true, 10);
                        //tvp.append(""+stampTemp+" : "+value);


                    }
                    DateRangeLow = paramDataArray[0].getX();
                    DateRangeHigh = paramDataArray[paramDataArray.length-1].getX();



                    finalDataSeries = new LineGraphSeries<>(paramDataArray);


                }else {

                    Toast.makeText(getApplicationContext(), "Error occured", Toast.LENGTH_LONG).show();
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
            return finalDataSeries;
        }
    }


}
