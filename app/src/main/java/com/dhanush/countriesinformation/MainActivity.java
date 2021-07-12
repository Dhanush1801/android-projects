package com.dhanush.countriesinformation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    ListView listView;
    final String[] name=new String[50];
    ArrayAdapter<String> arrayAdapter;
   ArrayList<String> cname=new ArrayList<String>();
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
                for(int i=0;i<array.length();i++){
                    String countries=array.getString(i);
                    JSONObject object=new JSONObject(countries);
                    name[i]=object.getString("name");
                   Log.i("ss",name[i]);
                   cname.add(name[i]);
                }
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent=new Intent(getApplicationContext(),MainActivity2.class);
                        intent.putExtra("Country",position);
                        intent.putExtra("cname",cname.get(position));
                        startActivity(intent);
                    }
                });

            }catch (Exception e){
                e.printStackTrace();
            }
         
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=(TextView) findViewById(R.id.textView);
        listView=(ListView) findViewById(R.id.listView);


        try { DownloadTask downloadTask=new DownloadTask();
            downloadTask.execute("https://restcountries.eu/rest/v2/region/asia");
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Could not find the weather", Toast.LENGTH_SHORT).show();
        }
        arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,cname);

    }
}