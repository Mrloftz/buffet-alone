package info.androidhive.buffet_alone.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import info.androidhive.buffet_alone.activity.MainActivity;
import info.androidhive.buffet_alone.activity.ShowlistCosumer;
import info.androidhive.buffet_alone.app.AppController;
import info.androidhive.buffet_alone.helper.SQLiteHandler;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JoinedRoomFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JoinedRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JoinedRoomFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = JoinedRoomFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SQLiteHandler db ;
    String sqlite_email;

    TextView txt_detail , txt_place , txt_start , txt_end , txt_extra , txt_room , txt_title , textViewPrice;
    ImageView showImg;
    Button listuserbutton , leavebutton;

    String rood_id;
    private OnFragmentInteractionListener mListener;

    public JoinedRoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JoinedRoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JoinedRoomFragment newInstance(String param1, String param2) {
        JoinedRoomFragment fragment = new JoinedRoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        db = new SQLiteHandler(getContext());
        HashMap<String, String> users = db.getUserDetails();


        View v = inflater.inflate(R.layout.fragment_joined_room, container, false);
        txt_room = (TextView) v.findViewById(R.id.groupidshow);
        txt_detail = (TextView) v.findViewById(R.id.detailshow);
        txt_place = (TextView) v.findViewById(R.id.placeshow);
        txt_start = (TextView) v.findViewById(R.id.dateshow);
        txt_end = (TextView) v.findViewById(R.id.timeshow);
        txt_extra = (TextView) v.findViewById(R.id.extrashow);
        txt_title = (TextView) v.findViewById(R.id.detailshow1);
        textViewPrice= (TextView) v.findViewById(R.id.detailshow2);
        showImg = (ImageView) v.findViewById(R.id.showimg);
        listuserbutton = (Button) v.findViewById(R.id.listuserbutton);
        leavebutton = (Button) v.findViewById(R.id.leavebutton);

        sqlite_email = users.get("email").toString();
        getMyJoinedRoom("http://robbanaz.000webhostapp.com/getMyJoinedRoom.php",sqlite_email);

        listuserbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getContext(), ShowlistCosumer.class);
                myIntent.putExtra("room_id" , rood_id);
                startActivity(myIntent);
            }
        });

        leavebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getContext(), MainActivity.class);
                leaveRoom(sqlite_email);
                startActivity(myIntent);
            }
        });


        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
*/
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void getMyJoinedRoom(String url , final String email) {
        // Tag used to cancel the request
        String tag_string_pro = "buffet tag";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("AA", response.toString());

                try {
                    Log.d("AAA" , "DONE");

                    JSONArray array = new JSONArray(response.toString());
                    JSONObject object = array.getJSONObject(0);

                    rood_id = object.getString("room_id");
                    txt_room.setText(rood_id);
                    String image = object.getString("image");
                    Log.wtf("AAA","image = " + object.getString("image"));

                    //Glide.with(getContext()).load(object.getString("image").toString()).into(showImg);
                    Picasso.with(getActivity()).load(image)
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
                Log.e(TAG, "Profile Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_pro);
    }

    public void leaveRoom(final String email){
        // Tag used to cancel the request
        String tag_string_pro = "buffet tag";
        String url = "http://robbanaz.000webhostapp.com/leaveroom.php";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("AA", response.toString());

                Log.d("DELETE" , "DONE");

            }
        }, new com.android.volley.Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Profile Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_pro);
    }
}
