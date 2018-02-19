package info.androidhive.navigationdrawer.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import info.androidhive.navigationdrawer.R;
import info.androidhive.navigationdrawer.app.AppConfig;
import info.androidhive.navigationdrawer.app.AppController;
import info.androidhive.navigationdrawer.helper.SQLiteHandler;
import info.androidhive.navigationdrawer.other.CircleTransform;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String urlProfileImg = "http://i0.kym-cdn.com/photos/images/original/000/782/020/fad.jpg";

    private SQLiteHandler db;

    private String sqlite_email;
    private String fname , lname ,user_email,sex,age,value_1,value_2,value_3;
    private ImageView img_pro;
    private TextView txt_name ,txt_value_1,txt_value_2,txt_value_3,txt_age,txt_sex,txt_email;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

        db = new SQLiteHandler(getContext());
        HashMap<String, String> users = db.getUserDetails();
        sqlite_email = users.get("email").toString();
        getDetailsUser(sqlite_email);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        img_pro = (ImageView) v.findViewById(R.id.img_pro);
        txt_name = (TextView) v.findViewById(R.id.name);
        txt_value_1 = (TextView) v.findViewById(R.id.value_1);
        txt_value_2 = (TextView) v.findViewById(R.id.value_2);
        txt_value_3 = (TextView) v.findViewById(R.id.value_3);
        txt_age = (TextView) v.findViewById(R.id.age);
        txt_sex = (TextView) v.findViewById(R.id.sex);
        txt_email = (TextView) v.findViewById(R.id.email);

        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(getContext()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img_pro);




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
    }*/

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

    private void getDetailsUser(final String email) {
        // Tag used to cancel the request
        String tag_string_pro = "profile tag";


        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_PROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Profile Response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        JSONObject user = jObj.getJSONObject("user");

                        fname = user.getString("fname");
                        lname = user.getString("lname");
                        user_email = user.getString("email");
                        sex = user.getString("sex");
                        age = user.getString("age");
                        value_1 = String.valueOf(user.getDouble("value_1"));
                        value_2 = String.valueOf(user.getDouble("value_2"));
                        value_3 = String.valueOf(user.getDouble("value_3"));

                        txt_name.setText(fname +" "+lname);
                        txt_value_1.setText(value_1);
                        txt_value_2.setText(value_2);
                        txt_value_3.setText(value_3);
                        txt_age.setText(age);
                        txt_sex.setText(sex);
                        txt_email.setText(user_email);


                        Log.d("test profile receive " , fname + " " + lname + " " + email + " " + sex + " " + age + " " + value_1 + " " + value_2 + " " + value_3 );

                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
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
