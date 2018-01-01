package brickbyte.hemabh.mayur.com.loginservlet;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by mayursanjaydevgaonkar on 29/06/15.
 */
public class JSONparser {

    static InputStream is = null;
    static JSONObject jObj=null;
    static String json="";

    static  InputStream iStream=null;
    static JSONArray jarray=null;

    String lru;

    public JSONparser(){
    }

    public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params) {


        try {
            if (method == "POST"){
                DefaultHttpClient httpClient= new DefaultHttpClient();
                HttpPost httpPost =new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse=httpClient.execute(httpPost);
                HttpEntity httpEntity=httpResponse.getEntity();
                is=httpEntity.getContent();

            }else if (method == "GET"){

                DefaultHttpClient httpClient=new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params,"utf-8");
                if (!paramString.matches(""))
                {
                url +="?"+paramString;
                }
                HttpGet httpGet = new HttpGet(url);
                lru =url;

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity=httpResponse.getEntity();
                is=httpEntity.getContent();

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            if (is == null){
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"),8);
            StringBuilder sb=new StringBuilder();
            String line= null;
            while((line = reader.readLine()) != null){
                sb.append(line+"\n");
            }
            is.close();
            json=sb.toString();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

            Log.e("JSON Parser", "Error parsing data" + e.toString());

        }

        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data" + e.toString());
        }

        return jObj;
    }
    public JSONArray getJSONFromUrl(String url){


        StringBuilder builder = new StringBuilder();
        HttpClient client= new  DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200){
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null){
                    builder.append(line);
                }
            }else {
                Log.e("==>","Failed to download file");
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            jarray = new JSONArray(builder.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data" + e.toString());
        }


        return jarray;
    }
}
