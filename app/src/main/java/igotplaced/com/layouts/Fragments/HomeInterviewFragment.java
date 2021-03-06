package igotplaced.com.layouts.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import igotplaced.com.layouts.CompanyDetailsActivity;
import igotplaced.com.layouts.Model.Interview;
import igotplaced.com.layouts.Model.Post;
import igotplaced.com.layouts.OtherProfileActivity;
import igotplaced.com.layouts.ProfileInterviewDetailsActivity;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.ClickListener;
import igotplaced.com.layouts.Utils.ItemClickListener;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;
import static igotplaced.com.layouts.Utils.Utils.Id;
import static igotplaced.com.layouts.Utils.Utils.MyPREFERENCES;
import static igotplaced.com.layouts.Utils.Utils.Name;
import static igotplaced.com.layouts.Utils.Utils.screenSize;

public class HomeInterviewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private Context context;
    private RequestQueue queue;

    private SwipeRefreshLayout swipeRefreshLayout;

    private String userId;
    private List<Interview> interviewList = new ArrayList<Interview>();
    private RecyclerAdapterInterviewHome recyclerAdapterInterviewHome;

    int loadLimit;
    private LinearLayoutManager mLayoutManager;
    private boolean loading;

    public HomeInterviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_interview, container, false);
        context = getActivity().getApplicationContext();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("Interview Experience");
        //mapping web view
        mapping(view);

        //   mLayoutManager = new LinearLayoutManager(context);

        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString(Id, null);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);


        interviewRecyclerView(view);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        if (screenSize(getActivity()) < 6.5) {
            loadLimit = 5;

        } else {

            loadLimit = 15;
        }

    }

    private void mapping(View view) {

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

    }


    @Override
    public void onRefresh() {
        interviewList.clear();
        loadData();
    }

    private void interviewRecyclerView(View view) {
        //mapping RecyclerView
        RecyclerView interview_view = (RecyclerView) view.findViewById(R.id.recycler_view_interview);
        //feeding values to RecyclerView using custom RecyclerView adapter
        recyclerAdapterInterviewHome = new RecyclerAdapterInterviewHome(context, interviewList);

        //setting fixed size

        if (screenSize(getActivity()) < 6.5)
            mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        else {
            mLayoutManager = new GridLayoutManager(context, 2);
        }
        interview_view.setHasFixedSize(true);
        //setting horizontal layout
        interview_view.setLayoutManager(mLayoutManager);
        interview_view.setAdapter(recyclerAdapterInterviewHome);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(context).getRequestQueue();

        //  loadData();

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadData();

            }
        });

        interview_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    int  totalItemCount;

                    totalItemCount = mLayoutManager.getItemCount();


                    if (!loading) {
                        loadMoreData(totalItemCount + 1);
                        loading = true;
                    }

                }
            }
        });



    }


    private void makeJsonObjectRequestInterviewHome(int start, int size) {

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }


        Log.d("error", "loaded" + BaseUri + "/home/topInterviewExperience/" + userId + "?start=" + start + "&size=" + size);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BaseUri + "/home/topInterviewExperience/" + userId + "?start=" + start + "&size=" + size, null, new Response.Listener<JSONObject>() {
            JSONArray jsonObjectJSON = null;

            @Override
            public void onResponse(JSONObject jsonObject) {
/*
                Log.d("error", jsonObject.toString());*/
                try {
                    jsonObjectJSON = jsonObject.getJSONArray("");

                    //clearing blogList
                    // postList.clear();

                    for (int i = 0; i < jsonObjectJSON.length(); i++) {
                     /*   Log.d("error", jsonObjectJSON.toString());*/
                        try {

                            JSONObject obj = jsonObjectJSON.getJSONObject(i);
                            Interview interview = new Interview(obj.getString("id"), obj.getString("user_id"), obj.getString("feedback"), obj.getString("industryname"), obj.getString("interviewUserImgName"), obj.getString("username"), obj.getString("created_by"), obj.getString("imgname"), obj.getString("fname"), obj.getString("companyname"), obj.getString("company_id"));
                            // adding movie to blogHomeList array
                            interviewList.add(interview);

                        } catch (Exception e) {
                            Log.d("error", e.getMessage());
                            System.out.println(e.getMessage());
                        } finally {
                            //Notify adapter about data changes
                            recyclerAdapterInterviewHome.notifyDataSetChanged();
                            loading = false;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.getMessage());
                Utils.showDialogue(getActivity(), "Sorry! Server Error");

            }
        });

        queue.add(jsonObjectRequest);

    }

    // By default, we add 10 objects for first time.
    private void loadData() {
        // I have not used current page for showing demo, if u use a webservice
        // then it is useful for every call request

        makeJsonObjectRequestInterviewHome(0, loadLimit);

    }

    // adding 10 object creating dymically to arraylist and updating recyclerview when ever we reached last item
    private void loadMoreData(int totalItemCount) {

        // I have not used current page for showing demo, if u use a webservice
        // then it is useful for every call request
        makeJsonObjectRequestInterviewHome(totalItemCount, totalItemCount + 5);

        recyclerAdapterInterviewHome.notifyDataSetChanged();

    }


    class RecyclerAdapterInterviewHome extends RecyclerView.Adapter<RecyclerAdapterInterviewHome.MyViewHolder> {
        private String userId = null;

        private List<Interview> interviewList;
        private Context context;
        private LayoutInflater inflater;

        public RecyclerAdapterInterviewHome(Context context, List<Interview> interviewList) {


            SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

            userId = sharedpreferences.getString(Id, null);

            this.context = context;
            this.interviewList = interviewList;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View rootView = inflater.inflate(R.layout.card_view_interview, parent, false);
            return new MyViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            Interview interview = interviewList.get(position);

            String  postedinterviewId;
            postedinterviewId = interview.getInterviewUserId();

            //Pass the values of feeds object to Views
            holder.interview.setText(interview.getInterview());
            holder.interviewIndustry.setText("#" + interview.getInterviewIndustry());
            holder.interviewProfileName.setText(interview.getInterviewProfileName());
            holder.interviewTime.setText(interview.getInterviewTime());

            if (interview.getInterviewCompany().equals("")) {
                holder.interviewCompany.setText(interview.getInterviewCompany());
            } else {
                holder.interviewCompany.setText("#" + interview.getInterviewCompany());
            }


            //  holder.userImage.setImageUrl(Utils.BaseImageUri + interview.getUserImage(), NetworkController.getInstance(context).getImageLoader());
            holder.interviewImage.setImageUrl(Utils.BaseImageUri + interview.getInterviewImage(), NetworkController.getInstance(context).getImageLoader());
            if (!userId.equals(postedinterviewId)) {
                holder.interviewProfileName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent otherProfileDetails = new Intent(context, OtherProfileActivity.class);

                        otherProfileDetails.putExtra("post_createdid", interviewList.get(position).getInterviewUserId());
                        otherProfileDetails.putExtra("created_uname", interviewList.get(position).getInterviewProfileName());
                        startActivity(otherProfileDetails);
                    }
                });
            }
            holder.interviewCompany.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent companyDetails = new Intent(context, CompanyDetailsActivity.class);
                    companyDetails.putExtra("postCompany", interviewList.get(position).getInterviewCompany());
                    companyDetails.putExtra("companyId", interviewList.get(position).getCompanyId());
                    startActivity(companyDetails);
                }
            });


            holder.viewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent profileInterview = new Intent(getContext(), ProfileInterviewDetailsActivity.class);

                    profileInterview.putExtra("iid", interviewList.get(position).getInterviewId());
                    profileInterview.putExtra("created_uname", interviewList.get(position).getInterviewProfileName());
                    profileInterview.putExtra("created_by", interviewList.get(position).getInterviewTime());
                    profileInterview.putExtra("interview", interviewList.get(position).getInterview());
                    profileInterview.putExtra("interviewImage", interviewList.get(position).getInterviewImage());
                    profileInterview.putExtra("interviewIndustry", interviewList.get(position).getInterviewIndustry());
                    profileInterview.putExtra("interview_createdid", interviewList.get(position).getInterviewUserId());
                    profileInterview.putExtra("postCompany", interviewList.get(position).getInterviewCompany());
                    profileInterview.putExtra("companyId", interviewList.get(position).getCompanyId());
                    startActivity(profileInterview);
                }
            });


        }


        @Override
        public int getItemCount() {
            return interviewList.size();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView interview, interviewIndustry, interviewProfileName, interviewTime, interviewCompany, viewMore;
            private NetworkImageView interviewImage;


            public MyViewHolder(View itemView) {
                super(itemView);
                interview = (TextView) itemView.findViewById(R.id.interview);
                interviewIndustry = (TextView) itemView.findViewById(R.id.interview_industry);
                interviewProfileName = (TextView) itemView.findViewById(R.id.interview_profile_name);
                interviewTime = (TextView) itemView.findViewById(R.id.interview_time);
                viewMore = (TextView) itemView.findViewById(R.id.view_more);
                interviewCompany = (TextView) itemView.findViewById(R.id.interview_company);


                // Volley's NetworkImageView which will load Image from URL
                interviewImage = (NetworkImageView) itemView.findViewById(R.id.interview_img);



            }


        }
    }


}