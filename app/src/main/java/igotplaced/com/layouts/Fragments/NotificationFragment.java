package igotplaced.com.layouts.Fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import igotplaced.com.layouts.CustomAdapter.RecyclerAdapterNotification;
import igotplaced.com.layouts.Model.NotificationView;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.NetworkController;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;

public class NotificationFragment extends Fragment {

    private Context context;
    private RequestQueue queue;


    private List<NotificationView> notificationViewList = new ArrayList<NotificationView>();
    private RecyclerAdapterNotification recyclerAdapterNotification;



    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        context = getActivity().getApplicationContext();

        notificationRecyclerView(view);

        return view;
    }

    private void notificationRecyclerView(View view) {
        //mapping RecyclerView
        RecyclerView notification_view = (RecyclerView) view.findViewById(R.id.card_recycler_view_notification);
        //feeding values to RecyclerView using custom RecyclerView adapter
        recyclerAdapterNotification = new RecyclerAdapterNotification(context, notificationViewList);

        //setting fixed size
        notification_view.setHasFixedSize(true);
        //setting horizontal layout
        notification_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        //setting RecyclerView adapter
        notification_view.setAdapter(recyclerAdapterNotification);
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(context).getRequestQueue();
        //Volley's inbuilt class to make Json array request
        makeJsonArrayRequestNotification();

    }

    private void makeJsonArrayRequestNotification() {



        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/notificationService/notification/256", new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        NotificationView notificationView = new NotificationView(obj.getString("createdBy"), obj.getString("notificationPost"),obj.getString("imageName"));
                        // adding movie to testimonialsList array
                        notificationViewList.add(notificationView);

                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                        System.out.println(e.getMessage());
                    } finally {
                        //Notify adapter about data changes
                        recyclerAdapterNotification.notifyDataSetChanged();
                    }
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.getMessage());

            }
        });

        queue.add(jsonArrayRequest);

    }

}
