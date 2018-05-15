package info.androidhive.buffet_alone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import info.androidhive.buffet_alone.R;
import info.androidhive.buffet_alone.app.AppController;
import info.androidhive.buffet_alone.helper.SQLiteHandler;

public class RoomDetail extends AppCompatActivity {
    Intent intent = getIntent();
    String title ;
    TextView txt_detail , txt_place , txt_start , txt_end , txt_extra , txt_room , txt_title , textViewPrice;
    ImageView showImg;
    Button btn ;
    String rood_id;
    SQLiteHandler db ;
    String sqlite_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomdetail);

        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        Log.d("robbanaz" , title);

        db = new SQLiteHandler(this);
        HashMap<String, String> users = db.getUserDetails();

        txt_room = (TextView) findViewById(R.id.detailroomidshow);
        txt_detail = (TextView) findViewById(R.id.detailshow);
        txt_place = (TextView) findViewById(R.id.placeshow);
        txt_start = (TextView) findViewById(R.id.dateshow);
        txt_end = (TextView) findViewById(R.id.timeshow);
        txt_extra = (TextView) findViewById(R.id.extrashow);
        txt_title = (TextView) findViewById(R.id.detailshow1);
        textViewPrice= (TextView) findViewById(R.id.detailshow2);
        showImg = (ImageView) findViewById(R.id.showimg);
        btn = (Button) findViewById(R.id.listuserbutton);

        getDetailRoom(title , "http://robbanaz.000webhostapp.com/get_detail_room.php");

        sqlite_email = users.get("email").toString();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                joinRoom(rood_id,sqlite_email);
                startActivity(myIntent);
            }
        });

    }

    private void getDetailRoom(final String title , String url) {
        // Tag used to cancel the request
        String tag_string_pro = "buffet tag";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Robbanaz", response.toString());

                try {
                    Log.d("AAA" , "DONE");

                    JSONArray array = new JSONArray(response.toString());
                    JSONObject object = array.getJSONObject(0);

                    rood_id = object.getString("room_id");
                    txt_room.setText(rood_id);
                    String image = object.getString("image");
                    Log.d("kkkk" , image);
                    Picasso.with(getApplicationContext()).load(image)
                            .fit().into(showImg);
                    String title = object.getString("title");
                    txt_title.setText(title);
                    String price = object.getString("type_name");
                    textViewPrice.setText(price);
                    String pro_des = object.getString("pro_des");
                    txt_detail.setText(pro_des);
                    String location = object.getString("location");
                    txt_place.setText(location);
                    String t_start = object.getString("date");
                    txt_start.setText(t_start);
                    String t_end = object.getString("time");
                    txt_end.setText(t_end);
                    String room_des = object.getString("room_des");
                    txt_extra.setText(room_des);

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new com.android.volley.Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("to", "Profile Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("title", title);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_pro);
    }

    public void joinRoom(final String room_id , final String email){
        String url ="http://robbanaz.000webhostapp.com/UserJoinToRoom.php";

        // Tag used to cancel the request
        String tag_string_pro = "buffet tag";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("a", "Profile Response for getpro: " + response.toString());

            }
        }, new com.android.volley.Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("dasda", "Profile Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("roomid", room_id);
                params.put("email", email);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_pro);
    }
}
