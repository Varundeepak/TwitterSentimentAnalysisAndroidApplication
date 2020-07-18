package com.example.twittertest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import java.util.ArrayList;


public class MainActivity extends Activity {

    RequestQueue requestQueue;
    String postext=null,negtext=null;
    int positive=0,negative=0,neutral=0;
    TextView textview2,textview3;
    PieChart pie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText name = findViewById(R.id.input);
        textview2=findViewById(R.id.textview2);
        textview3=findViewById(R.id.textview3);
        textview2.setMovementMethod(new ScrollingMovementMethod());
        textview3.setMovementMethod(new ScrollingMovementMethod());
        pie=findViewById(R.id.piechart);
        Button One = (Button) findViewById(R.id.submitbutton);
        One.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String in=name.getText().toString();
                //String jsonformat="{\"name\" : "+in+"}";
                if(in.length()>0){
                String jsonformat=in;
                Submit(jsonformat);}
                else{
                    Toast.makeText(getApplicationContext(),"Please Enter Something!!!",Toast.LENGTH_LONG).show();
                }

                }

            });
        }

    private void Submit(String data)
    {
        final String savedata= data;
        String URL="http://varundeepak.pythonanywhere.com";

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objres=new JSONObject(response);
                    //Toast.makeText(getApplicationContext(),objres.toString(),Toast.LENGTH_LONG).show();
                    positive=objres.getInt("positive");
                    negative=objres.getInt("negative");
                    neutral=objres.getInt("neutral");
                    postext=objres.getString("finaltextpos");
                    negtext=objres.getString("finaltextneg");

                    pie.setUsePercentValues(true);
                    pie.getDescription().setEnabled(false);
                    pie.setExtraOffsets(5,10,5,5);
                    pie.setDragDecelerationFrictionCoef(0.95f);
                    pie.setDrawHoleEnabled(true);
                    pie.setHoleColor(Color.WHITE);
                    pie.setTransparentCircleRadius(61f);
                    ArrayList<PieEntry> yvalues=new ArrayList<>();
                    yvalues.add(new PieEntry(positive,"Positive"));
                    yvalues.add(new PieEntry(negative,"Negative"));
                    yvalues.add(new PieEntry(neutral,"Neutral"));
                    pie.animateY(1000, Easing.EaseInOutCubic);
                    PieDataSet dataset=new PieDataSet(yvalues,"Sentiment");
                    dataset.setSliceSpace(3f);
                    dataset.setSelectionShift(5f);
                    dataset.setColors(Color.GREEN,Color.RED,Color.GRAY);
                    PieData data=new PieData((dataset));
                    data.setValueTextSize(10f);
                    data.setValueTextColor(Color.BLACK);
                    data.setDrawValues(true);
                    pie.setData(data);
                    textview2.setText(postext);
                    textview3.setText(negtext);

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_LONG).show();

                }
                //Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                //Log.v("VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/text; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return savedata == null ? null : savedata.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {

                    return null;
                }
            }

        };
        requestQueue.add(stringRequest);
    }



        @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //MainActivity.this.finish();
                        //System.exit(1);
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
