package info.androidhive.navigationdrawer.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import info.androidhive.navigationdrawer.R;
import info.androidhive.navigationdrawer.app.AppController;

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
    TextView txt_detail , txt_place , txt_start , txt_end , txt_extra , txt_room , txt_title;

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

        View v = inflater.inflate(R.layout.fragment_joined_room, container, false);
        txt_room = (TextView) v.findViewById(R.id.groupidshow);
        txt_detail = (TextView) v.findViewById(R.id.detailshow);
        txt_place = (TextView) v.findViewById(R.id.placeshow);
        txt_start = (TextView) v.findViewById(R.id.dateshow);
        txt_end = (TextView) v.findViewById(R.id.timeshow);
        txt_extra = (TextView) v.findViewById(R.id.extrashow);

        //txt_title = (TextView) findViewById(R.id.title);

        getDetail_room("http://localhost/loft/get_detail_room.php","1");
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

    private void getDetail_room(String url , final String roomid) {
        // Tag used to cancel the request
        String tag_string_pro = "buffet tag";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Profile Response for getBuffet: " + response.toString());

                try {

                    JSONArray array = new JSONArray(response.toString());
                    JSONObject object = array.getJSONObject(0);

                    String rood_id = object.getString("room_id");
                    txt_room.setText(rood_id);
                    String title = object.getString("title");

                    String des = object.getString("des");
                    txt_detail.setText(des);
                    String location = object.getString("location");
                    txt_place.setText(location);
                    String t_start = object.getString("date");
                    txt_start.setText(t_start);
                    String t_end = object.getString("time");
                    txt_end.setText(t_end);
                    String extras = object.getString("sp_cdt");
                    txt_extra.setText(extras);



                    Log.d(TAG , "JSON " + rood_id);


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
                params.put("roomid", roomid);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_pro);
    }
}
