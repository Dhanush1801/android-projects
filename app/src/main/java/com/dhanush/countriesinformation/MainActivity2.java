package com.dhanush.countriesinformation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity2 extends AppCompatActivity {
    int cid;
    String name;
    String capital;
    String flagLink;
    String region;
    String subregion;
    String population;
    TextView t1;
    TextView t2;
    ImageView flagView;
    String borders;
    String languages;
    String languageparts="";
    String borderparts="";


    public void setimage(String link){
        String url = link;
        Utils.fetchSvg(this, url, flagView);

    }


    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            StringBuilder sb = new StringBuilder();
            String result;
            URL url;
            HttpURLConnection urlConnection = null;
            try {

                url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    sb.append(current);
                    data = reader.read();
                }

                result = sb.toString();


                return result;



            } catch (Exception e) {

                e.printStackTrace();

                return null;

            }


        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONArray array=new JSONArray(s);
                    String countries=array.getString(cid);
                    JSONObject object=new JSONObject(countries);
                    name=object.getString("name");
                    capital=object.getString("capital");
                    region=object.getString("region");
                    subregion=object.getString("subregion");
                    population=object.getString("population");
                    flagLink=object.getString("flag");
                    borders=object.getString( "borders");
                    languages=object.getString("languages");
                    Log.i("dd",languages);
                    JSONArray arr=new JSONArray(borders);
                    for (int i=0;i<arr.length();i++){
                        if(!borderparts.equals("")){
                            borderparts+=",";
                        }
                        borderparts=borderparts+arr.getString(i);
                    }
                    JSONArray array1=new JSONArray(languages);
                    for(int i=0;i<array1.length();i++) {
                        if(!languageparts.equals("")){
                            languageparts+=",";
                        }
                        JSONObject jsonpart = array1.getJSONObject(i);
                        languageparts += jsonpart.getString("name");

                    }


                    t1.setText("Name:"+name+"\n\nCapital:"+capital+"\n\nFlag:");
                    setimage(flagLink);
                    t2.setText("Region:"+region+"\n\nSubRegion:"+subregion+"\n\nPopulation:"+population+"\n\nBorders:"+borderparts+"\n\nLanguages:"+languageparts);



            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        t1=(TextView) findViewById(R.id.t1);
        t2=(TextView) findViewById(R.id.t2);
        flagView=(ImageView) findViewById(R.id.flagView);

        Intent intent=getIntent();
        String cname=intent.getStringExtra("cname");
        cid=intent.getIntExtra("Country",0);
        setTitle(cname);
        try { DownloadTask downloadTask=new DownloadTask();
            downloadTask.execute("https://restcountries.eu/rest/v2/region/asia");
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"please check your internet", Toast.LENGTH_SHORT).show();
        }
    }
}