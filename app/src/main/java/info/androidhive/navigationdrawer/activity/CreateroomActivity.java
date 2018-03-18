package info.androidhive.navigationdrawer.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import info.androidhive.navigationdrawer.helper.SQLiteHandler;

public class CreateroomActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    String url ="http://192.168.45.2/loft/insertroom.php";



    private static final String TAG = "Creategroup";
    //<ประกาศตัวแปร
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView mTxtTime,placeshow,priceshow;
    private Spinner spinnerPeopleMax;
    private EditText commentbox;
    private EditText EditTextTitle;
    private Button Button;
    private ImageView showImg;
    SQLiteHandler db ;
    String sqlite_email;

    String pro_id;
    String email_user;
    String title,des,peoplemax;
    private String time,time_end;
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createroom);
        placeshow = (TextView) findViewById(R.id.placeshow);
        priceshow = (TextView) findViewById(R.id.priceshow);
        showImg = (ImageView) findViewById(R.id.showImg);
        mDisplayDate = (TextView) findViewById(R.id.selectdate);
        mTxtTime = (TextView) findViewById(R.id.selecttime);
        commentbox = (EditText) findViewById(R.id.extraedittext);
        Button = (Button) findViewById(R.id.createbutton);
        EditTextTitle = (EditText) findViewById(R.id.edtroomname);
        spinnerPeopleMax=(Spinner) findViewById(R.id.selectpeople);

        db = new SQLiteHandler(this);
        HashMap<String, String> users = db.getUserDetails();

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                onCreateDialog(CreateroomActivity.this).show();
/*
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(this,
                        android.R.style.Theme_Material_Dialog_NoActionBar_MinWidth,
                        mDateSetListener,
                        2013, 2, 18);
                dialog.create();
                dialog.show();
*/

/*
                DatePickerDialog dialog = new DatePickerDialog(getBaseContext(),
                        android.R.style.Theme_Material_Dialog_NoActionBar_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
*/
            }
        });


        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: DD/MM/YYY " + day + " / " + month + " / " + year);

                String date = day + " / " + month + " / " + year;

            }
        };//>ส่วนของการเลือกวันที่
        Button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                create_room();
            }
        });
        //<ส่วนของการเลือกเวลา
        mTxtTime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                onCreateTimeDialog(CreateroomActivity.this).show();

                /*
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
*/
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
        spinnerPeopleMax.setAdapter(spinnerArrayAdapter);
        //>ส่วนของเลือกจำนวนคน
        // Inflate the layout for this fragment

        Intent intent = getIntent();
        //รวมข้อมูลไปส่ง
        pro_id = intent.getStringExtra("pro_id");
        sqlite_email = users.get("email").toString();
        //pro = Integer.parseInt(pro_id);
        //
        getpro(pro_id,"http://192.168.45.2/loft/getpro.php");
    }

    public Dialog onCreateDialog(CreateroomActivity savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        return new DatePickerDialog(this, dateSetListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    Toast.makeText(CreateroomActivity.this, "selected date is " + view.getYear() +
                            " / " + (view.getMonth()+1) +
                            " / " + view.getDayOfMonth(), Toast.LENGTH_SHORT).show();
                    //String date1 = view.getDayOfMonth() + "/"  + (view.getMonth()+1) + "/" + view.getYear();
                    //Log.d("test" , date1);
                    date = ""+view.getYear()+"-"+(view.getMonth()+1)+"-"+view.getDayOfMonth();
                    mDisplayDate.setText(date);
                }
            };

    public Dialog onCreateTimeDialog(CreateroomActivity savedInstanceState) {

        Calendar cal2 = Calendar.getInstance();
        int hour = cal2.get(Calendar.HOUR_OF_DAY);
        int minute = cal2.get(Calendar.MINUTE);

        return new TimePickerDialog(this, au , hour, minute, true);
    }

    private TimePickerDialog.OnTimeSetListener au =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    Log.i("Time", "hours: " + hourOfDay + " minute: " + minute);
                    //time = hourOfDay + ":" + minute + " น.";
                    time = ""+hourOfDay +":"+ minute ;
                    time_end = ""+(hourOfDay+4) + ":" + minute;



                    mTxtTime.setText(time);
                }
            };

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


    private void create_room() {
        title = EditTextTitle.getText().toString();
        des = commentbox.getText().toString();
        peoplemax = spinnerPeopleMax.getSelectedItem().toString();

        // Tag used to cancel the request
        String tag_string_pro = "buffet tag";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Profile Response for getpro: " + response.toString());

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
                params.put("user_header", sqlite_email);
                params.put("title", title);
                params.put("des", des);
                params.put("people_max", peoplemax);
                params.put("t_start", date+"T"+time);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_pro);
    }

}

