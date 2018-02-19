package info.androidhive.navigationdrawer.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.navigationdrawer.R;
import info.androidhive.navigationdrawer.activity.MainActivity;
import info.androidhive.navigationdrawer.app.AppConfig;
import info.androidhive.navigationdrawer.app.AppController;
import info.androidhive.navigationdrawer.promotion.ListPromotion;
import info.androidhive.navigationdrawer.promotion.PromotionAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PromotionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PromotionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PromotionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private static final String TAG = MainActivity.class.getSimpleName();
    private List<ListPromotion> listPromotion;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayout;
    private PromotionAdapter adapter_promotion;

    private ArrayList<String> selected_array = new ArrayList<String>();
    private ArrayList<String> value_array = new ArrayList<String>();
    private Spinner spin_selected;
    private Spinner spin_value;
    int click;



    public PromotionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PromotionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PromotionFragment newInstance(String param1, String param2) {
        PromotionFragment fragment = new PromotionFragment();
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
        View v = inflater.inflate(R.layout.fragment_promotion, container, false);
        spin_selected = (Spinner) v.findViewById(R.id.spinner);
        spin_value = (Spinner) v.findViewById(R.id.spinner1);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);

        // Inflate the layout for this fragment

        listPromotion = new ArrayList<>();

        gridLayout = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayout);

        //set Array for spinner
        selected_array.add("ราคา");
        selected_array.add("ประเภทบุปเฟ่ต์");
        //array adapter
        ArrayAdapter<String> adapterThai = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, selected_array);
        spin_selected.setAdapter(adapterThai);


        spin_selected.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                click = position;

                if(click == 0){
                    Log.d("au test" , "0");
                    getdata_spinner_value(AppConfig.URL_PRICE_TYPE);
                    spin_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {
                            int click_value = position1;
                            getBuffet(String.valueOf(click_value+1),AppConfig.URL_PROMOTION_PRICE_TYPE);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                if(click == 1){
                    getdata_spinner_value(AppConfig.URL_BUFFET_TYPE);
                    spin_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {
                            int click_value = position1;
                            getBuffet(String.valueOf(click_value+1),AppConfig.URL_PROMOTION_BUFFET_TYPE);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
            throw new RuntimeException(context.toString()     });
        return v;

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


    private void getBuffet(final String type_id , String URL) {
        // Tag used to cancel the request
        String tag_string_pro = "buffet tag";

        StringRequest strReq = new StringRequest(Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Profile Response for getBuffet: " + response.toString());

                try {

                    JSONArray array = new JSONArray(response.toString());

                    listPromotion.clear();
                    for (int i = 0; i < array.length(); i++) {
                        Log.d(TAG, "Profile Response for getBuffet: " +"+--------------------------+");
                        JSONObject object = array.getJSONObject(i);
                        //Log.wtf(TAG,"title = " + object.getString("title") + " rating = " + object.getString("rating") + " img = " + object.getString("image"));
                        Log.d("test pro id= " , object.getString("pro_id"));
                        ListPromotion listPromotion = new ListPromotion(object.getString("title"),
                                object.getString("image"), object.getString("rating") , object.getString("pro_id") );

                        PromotionFragment.this.listPromotion.add(listPromotion);
                    }
                    adapter_promotion = new PromotionAdapter(getContext(), listPromotion);
                    recyclerView.setAdapter(adapter_promotion);
                    adapter_promotion.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();

                    listPromotion.clear();
                    adapter_promotion = new PromotionAdapter(getContext(), listPromotion);
                    recyclerView.setAdapter(adapter_promotion);
                    adapter_promotion.notifyDataSetChanged();
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
                params.put("type", type_id);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_pro);
    }

    private void getdata_spinner_value(final String URL) {
        // Tag used to cancel the request
        String tag_string_pro = "buffet tag";

        StringRequest strReq = new StringRequest(Method.POST,
                URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {


                try {
                    value_array.clear();
                    JSONArray array = new JSONArray(response.toString());

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject object = array.getJSONObject(i);
                        //Log.d(TAG,"title = " + object.getString("title") + " rating = " + object.getString("rating") + " img = " + object.getString("image"));
                        value_array.add(object.getString("type_name") );

                    }
                    ArrayAdapter<String> adapterarray = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_dropdown_item_1line, value_array);
                    spin_value.setAdapter(adapterarray);
                    adapterarray.notifyDataSetChanged();
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

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_pro);
    }

}
