package info.androidhive.navigationdrawer.fragment;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import info.androidhive.navigationdrawer.R;
import info.androidhive.navigationdrawer.promotion.CustomListAdapter;
import info.androidhive.navigationdrawer.promotion.Product;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FindPartyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FindPartyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindPartyFragment extends Fragment {
    ArrayList<Product> arrayList;
    ListView lv;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FindPartyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FindPartyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FindPartyFragment newInstance(String param1, String param2) {
        FindPartyFragment fragment = new FindPartyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_find_party, container, false);
        arrayList = new ArrayList<>();
        lv = (ListView) v.findViewById(R.id.listView);

        {


            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new ReadJSON().execute("http://192.168.45.2/loft/list_room.php");
                }
            });
            //getAllListRoom("http://192.168.45.2/loft/list_room.php");

            return v;
        }

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
    //        if (context instanceof OnFragmentInteractionListener) {
    //            mListener = (OnFragmentInteractionListener) context;
    //        } else {
    //            throw new RuntimeException(context.toString()
    //                    + " must implement OnFragmentInteractionListener");
    //        }
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

    class ReadJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return readURL(params[0]);
        }

        @Override
        protected void onPostExecute(String content) {
            try {

                JSONArray jsonArray =  new JSONArray(content.toString());
                for(int i =0;i<jsonArray.length(); i++){
                    JSONObject productObject = jsonArray.getJSONObject(i);
                    arrayList.add(new Product(
                            productObject.getString("image"),//Promotion image
                            productObject.getString("pro_title"),//Promotion name
                            productObject.getString("room_title")//room title
                    ));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            CustomListAdapter adapter = new CustomListAdapter(
                    getActivity(), R.layout.custom_list_layout, arrayList
            );

            lv.setAdapter(adapter);



        }
    }

    private static String readURL(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {
            // create a url object
            URL url = new URL(theUrl);
            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();
            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }


}


