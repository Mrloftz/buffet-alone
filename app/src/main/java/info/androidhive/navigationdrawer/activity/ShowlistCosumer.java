package info.androidhive.navigationdrawer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import info.androidhive.navigationdrawer.R;
import info.androidhive.navigationdrawer.app.AppController;
import info.androidhive.navigationdrawer.promotion.Product;
import info.androidhive.navigationdrawer.promotion.RoomListAdapter;

public class ShowlistCosumer extends AppCompatActivity {
    ArrayList<Product> arrayList;
    ListView lv;
    String room_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showlist_cosumer);

        Bundle bundle = getIntent().getExtras();
        room_id = bundle.getString("room_id");


        arrayList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listView);
        SetListConsument(room_id);

    }

    public void SetListConsument (final String room_id){
        // Tag used to cancel the request
        String tag_string_pro = "buffet tag";
        String url = "http://robbanaz.000webhostapp.com/userinroom.php";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("AA", response.toString());

                try {

                    JSONArray jsonArray =  new JSONArray(response.toString());
                    for(int i =0;i<jsonArray.length(); i++){
                        JSONObject productObject = jsonArray.getJSONObject(i);
                        arrayList.add(new Product(
                                "https://d1nhio0ox7pgb.cloudfront.net/_img/o_collection_png/green_dark_grey/512x512/plain/user.png",//Promotion image
                                productObject.getString("fname"),//Promotion name
                                productObject.getString("age")//room title
                        ));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }


                RoomListAdapter adapter = new RoomListAdapter(
                        getApplication(), R.layout.custom_list_layout, arrayList
                );

                lv.setAdapter(adapter);

            }
        }, new com.android.volley.Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e("", "Profile Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("roomid", room_id);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_pro);
    }

}
