package com.example.MainService.WAN.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Login.etc.PreferenceManager;
import com.example.MainService.StudyGame.fragments.Problem;
import com.example.alarm.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Wrong_Adapter extends RecyclerView.Adapter<Wrong_Adapter.CustonViewHolder> {

    private ArrayList<Worg_data> arrayList;
    private Context context;
    ConstraintLayout wrong_problem;
    ConstraintLayout Wrong_preview;
    Button re_solution;
    String url;
    boolean click = false;


    public Wrong_Adapter() {
        this.arrayList = this.arrayList;
        this.context = context;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @NotNull
    @Override
    public Wrong_Adapter.CustonViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wrong_note_data,parent,false);
        CustonViewHolder holder = new CustonViewHolder(view);


        re_solution = (Button)view.findViewById(R.id.re_solution);
        wrong_problem = (ConstraintLayout)view.findViewById(R.id.wrong_problem);
        Wrong_preview = (ConstraintLayout)view.findViewById(R.id.Wrong_preview);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Wrong_Adapter.CustonViewHolder holder, int position) {
        holder.onBind(arrayList.get(position));


        re_solution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION){
                    Log.e("problem_id", holder.problem_id );
                    Intent intent = new Intent(view.getContext(),Problem.class);
                    intent.putExtra("problem_id",holder.problem_id);
                    intent.putExtra("title",holder.tv_category.getText().toString());
                    view.getContext().startActivity(intent);

                }
            }
        });

        wrong_problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                Log.e("TAG", String.valueOf(pos));
                if (pos != RecyclerView.NO_POSITION){
                    Log.e("TAG", String.valueOf(pos));
                    click = !click;
                    if(click){
                        holder.Wrong_preview.setVisibility(View.VISIBLE);
                    }else{
                       holder.Wrong_preview.setVisibility(View.GONE);

                    }
                }
            }
        });

        wrong_problem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION){
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                    builder.create();
                    builder.setTitle("오답 문제 삭제");
                    builder.setMessage("오답 문제를 삭제할까요?");
                    url = "http://ec2-3-21-96-239.us-east-2.compute.amazonaws.com:8002/godelete";
                    Log.e("TAG", url );
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        RequestQueue queue = Volley.newRequestQueue(view.getContext());
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            StringRequest wrong_delete = new StringRequest(
                                    Request.Method.POST, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("TAG", String.valueOf(error));
                                }
                            }){
                                @Override
                                protected Map<String, String> getParams()throws AuthFailureError{
                                    Map<String, String> params = new HashMap<>();
                                    params.put("problem_id", holder.problem_id);
                                    Log.e("TAG", holder.problem_id );
                                    params.put("user_id", holder.user_uid);
                                    Log.e("TAG", holder.user_uid );
                                    return params;
                                }
                            };
                            wrong_delete.setRetryPolicy(new DefaultRetryPolicy(0,-1,0));
                            queue.add(wrong_delete);
                            removeItem(pos);
                            notifyDataSetChanged();
                        }

                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                }
                return false;
            }
        });

    }

    public void clearArrayList()
    {
        arrayList.clear();
    }

    public void removeItem(int position) {
        if(arrayList.size() < 1) {

        }
        else {
            arrayList.remove(position);
        }

    }


    public void removeItem() {
        if(arrayList.size() < 1) {

        }
        else {
            arrayList.remove(arrayList.size()-1);
        }

    }


    public void setArrayList(ArrayList<Worg_data> list){
        this.arrayList =list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }



    public static class CustonViewHolder extends RecyclerView.ViewHolder {

        TextView tv_date, tv_category, tv_problem_text;
        ConstraintLayout wrong_problem, Wrong_preview;
        String problem_id, user_uid;
        Button re_solution;


        CustonViewHolder(View itemView) {
            super(itemView);
            tv_date = (TextView)itemView.findViewById(R.id.wrong_date);
            tv_category = (TextView)itemView.findViewById(R.id.wrong_type_text);
            tv_problem_text = (TextView)itemView.findViewById(R.id.wrong_problem_text);
            wrong_problem = (ConstraintLayout)itemView.findViewById(R.id.wrong_problem);
            Wrong_preview = (ConstraintLayout)itemView.findViewById(R.id.Wrong_preview);
            re_solution = (Button)itemView.findViewById(R.id.re_solution);


        }

        public void onBind(Worg_data item) {
            tv_date.setText(item.getTv_date());
            tv_category.setText(item.getTv_category());
            tv_problem_text.setText(item.getTv_problem_text());
            problem_id = item.getProblem_id();
            user_uid = item.getUser_uid();

        }
    }


}
