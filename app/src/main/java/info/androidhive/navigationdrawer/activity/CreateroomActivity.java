package info.androidhive.navigationdrawer.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.navigationdrawer.R;
import info.androidhive.navigationdrawer.app.AppController;

public class CreateroomActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    String url ="http://192.168.1.56/loft/createroom.php";



    private static final String TAG = "Creategroup";
    //<ประกาศตัวแปร
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView mTxtTime,placeshow,priceshow;
    private Spinner spinner;
    private EditText commentbox;
    private Button Button;
    private ImageView showImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createroom);
        placeshow = (TextView) findViewById(R.id.placeshow);
        priceshow= (TextView) findViewById(R.id.priceshow);
        showImg = (ImageView) findViewById(R.id.showImg) ;
        mDisplayDate = (TextView) findViewById(R.id.selectdate);
        mTxtTime = (TextView) findViewById(R.id.selecttime);
        spinner = (Spinner) findViewById(R.id.selectpeople);
        commentbox = (EditText) findViewById(R.id.extraedittext);
        Button = (Button) findViewById(R.id.createbutton);


        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getApplicationContext(),
                        android.R.style.Theme_Material_Dialog_NoActionBar_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: DD/MM/YYY " + day + " / " + month + " / " + year);

                String date = day + " / " + month + " / " + year;
                mDisplayDate.setText(date);
            }
        };//>ส่วนของการเลือกวันที่
        Button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameter = new HashMap<String, String>();
                        parameter.put("peoplemax",spinner.getContext().toString());
                        parameter.put("d_create",mDisplayDate.getText().toString());
                        parameter.put("t_create",mTxtTime.getText().toString());
                        parameter.put("des",commentbox.getText().toString());
                        return parameter;

                    }
                };
                requestQueue.add(request);
            }
        });
        //<ส่วนของการเลือกเวลา
        mTxtTime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar cal2 = Calendar.getInstance();
                int hour = cal2.get(Calendar.HOUR_OF_DAY);
                int minute = cal2.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getApplicationContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        Log.i("Time", "hours: " + hourOfDay + " minute: " + minute);
                        String time = hourOfDay + ":" + minute + " น.";
                        mTxtTime.setText(time);
                    }
                }, hour, minute, true);
                timePickerDialog.show();

            }
        });//>ส่วนของการเลือกเวลา

        //<ส่วนของเลือกจำนวนคน
        String[] number = new String[]{
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
        };
        final List<String> numberList = new ArrayList<>(Arrays.asList(number));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item, numberList);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);
        //>ส่วนของเลือกจำนวนคน
        // Inflate the layout for this fragment



        Intent intent = getIntent();
        String id = intent.getStringExtra("pro_id");
        getpro(id,"http://192.168.45.2/loft/getpro.php");
    }

    private void getpro(final String pro_id , String URL) {
        // Tag used to cancel the request
        String tag_string_pro = "buffet tag";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Profile Response for getpro: " + response.toString());

                try {

                    JSONArray array = new JSONArray(response.toString());

                    Log.d(TAG, "Profile Response for getBuffet: " +"+--------------------------+");
                        JSONObject object = array.getJSONObject(0);
                        Log.wtf(TAG,"title = " + object.getString("image") + " rating = " + object.getString("location") + " img = " + object.getString("type_name"));

                    placeshow.setText(object.getString("location"));
                    priceshow.setText(object.getString("type_name"));
                    Glide.with(getApplication()).load(object.getString("image")).into(showImg);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("JSON error" , "kuy");

                }

            }
        }, new com.android.volley.Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Profile Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("pro_id", pro_id);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_pro);
    }



        }

