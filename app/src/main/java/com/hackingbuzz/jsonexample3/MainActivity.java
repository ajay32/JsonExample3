package com.hackingbuzz.jsonexample3;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    // this program will end up the NetworkThread Exception.. android doesn't allow you to do Network calls on UI Thread..

    TextView textView;
    Button btn;
    HttpURLConnection connection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        btn = (Button) findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {    // when we click on a button...do all of this.. set the output to the textView

                new JsonClass().execute("https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesDemoItem.txt");    //we re creating object n passing URL to AsyncTask first Parameter...then it move to doInBackgroud varagas (String) n then value in varags in 0th location put in url...for use.

            }
        });


    } // onCreate Close
    class JsonClass extends AsyncTask<String, String, String > {     // first param(URL) ..we give Async Task to Process... 2. param for Process...3. the Result we get..(String form)

        @Override
        protected String doInBackground(String... params) {
            BufferedReader bufferedReader = null;                                                        // gotta initilize bufferReader....why coz..if connection is initilize..n even its null(empty)..n u have to put in ..u get data in it..

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection)url.openConnection();     //open                         //opening browser n putting url..n pressed click..
                connection.connect();                                       //connect                    // now with this command we are connecting..

                InputStream inputStream = connection.getInputStream();                                // now we need input stream to get read data comming from server..n getting InputStream n connecting to our connection to server ( connection.getInputStream)

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));                     // so data comming in input stream..is in byte form (InputStreamReader -- read character from Byte ....BufferederReader --- then read text from character input )
                String textual = "";
                StringBuffer sb = new StringBuffer();   // for sending u also convet it to String..

                while((textual = bufferedReader.readLine()) !=null) {
                    sb.append(textual);
                }
                String JsonData = sb.toString();

                JSONObject parentObject = new JSONObject(JsonData);
                JSONArray parentArray = parentObject.getJSONArray("movies");
                JSONObject childObject  = parentArray.getJSONObject(0);
                String movieName = childObject.getString("movie");
                int yearName = childObject.getInt("year");

                return movieName + " - " + yearName;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if( connection != null) {                                                             // obviously if connect is null (empty ) how could u disconnect it..
                    connection.disconnect();  } // close
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();      }                                                         // it gives IO Exception...if data that is comming is not in Character from..
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;                                                            // we returned value before..so just put null here if it ask for return..we put it before catch..
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textView.setText(s);

        }

    }




}
// something is not initlize...u dont have to close it....
