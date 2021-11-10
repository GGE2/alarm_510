package com.example.MainService.WAN.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Login.etc.PreferenceManager;
import com.example.alarm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Wrong_proverb extends Fragment {

    private RecyclerView recyclerView;
    private  Wrong_Adapter adapter;
    private ArrayList<Worg_data> arrayList;
    String user_id;
    TextView clear_note_text;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_wrong_proverb, container, false);


        recyclerView = view.findViewById(R.id.wrong_rv);
        clear_note_text = (TextView)view.findViewById(R.id.clear_note_text);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        user_id = PreferenceManager.getString(getActivity().getApplicationContext(),"User_Uid");
        adapter = new Wrong_Adapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);
        arrayList = new ArrayList<>();


        StringRequest stringRequest = new StringRequest(Request.Method.GET,  "http://ec2-3-21-96-239.us-east-2.compute.amazonaws.com:8002/getWrongProverb?user_id="+user_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if(jsonArray.length() == 0){
                                clear_note_text.setVisibility(View.VISIBLE);
                            }else {
                                clear_note_text.setVisibility(View.GONE);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String date = obj.getString("date_time");
                                    String category_name = obj.getString("category_name");
                                    String question = obj.getString("question");
                                    String problem_id = obj.getString("problem_id");
                                    String user_uid = obj.getString("user_uid");
                                    arrayList.add(new Worg_data(date, category_name, question, problem_id, user_uid));
                                }
                                adapter.setArrayList(arrayList);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG",  error.toString());
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);

    }
}