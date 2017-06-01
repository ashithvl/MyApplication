package igotplaced.com.layouts.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import igotplaced.com.layouts.EditProfileActivity;
import igotplaced.com.layouts.Model.ProfileHome;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;

import static igotplaced.com.layouts.Utils.Utils.BaseUri;


public class ProfileFragment extends Fragment {

    private Context context;
    private RequestQueue queue;


    private ImageView profViewEditButton;
    private NetworkImageView profile_img;
    private TextView userProfileName,userProfileDepartment,userProfileCollege;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

    private int[] tabIcons = {
            R.drawable.ic_mail_outline_white_36dp,
            R.drawable.ic_timeline_white_24dp,
            R.drawable.ic_event_white_24dp,
            R.drawable.ic_forum_white_24dp
    };


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       /* if (container != null) {
            container.removeAllViews();
        }*/

        context =  getActivity().getApplicationContext();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        viewPager = (ViewPager) view.findViewById(R.id.pager);
        setupViewPager(viewPager);


        queue = NetworkController.getInstance(context).getRequestQueue();


        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        profViewEditButton = (ImageView) view.findViewById(R.id.edit_profile);
        userProfileName = (TextView) view.findViewById(R.id.user_profile_name);
        userProfileDepartment = (TextView) view.findViewById(R.id.user_profile_department);
        userProfileCollege = (TextView) view.findViewById(R.id.user_profile_college);
        profile_img = (NetworkImageView) view.findViewById(R.id.user_profile_photo);

        //Volley's inbuilt class to make Json array request
        makeJsonArrayRequestProfile();

        profViewEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prof = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(prof);
            }
        });

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
                setupTabIcons();

            }

        });

        return view;

    }

    private void makeJsonArrayRequestProfile() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BaseUri + "/profileService/profile/256", new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {


                for (int i = 0; i < response.length(); i++) {
                    Log.d("error", response.toString());
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        ProfileHome profileHome = new ProfileHome(obj.getString("imgname"), obj.getString("fname"), obj.getString("department"), obj.getString("college"));

                        userProfileName.setText(profileHome.getProfileName());
                        userProfileDepartment.setText(profileHome.getDepartmentName());
                        userProfileCollege.setText(profileHome.getCollegeName());

                        Log.d("error", "Error: " + Utils.BaseImageUri + profileHome.getImageName());
                        profile_img.setImageUrl(Utils.BaseImageUri + profileHome.getImageName(), NetworkController.getInstance(context).getImageLoader());


                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
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

    private void setupTabIcons() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setIcon(tabIcons[i]);
            }
        }
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new ProfilePostFragment());
        adapter.addFragment(new ProfileInterviewExperienceFragment());
        adapter.addFragment(new ProfileEventsFragment());
        adapter.addFragment(new ProfileQuestionFragment());
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
            //return mFragmentTitleList.get(position);
        }
    }

}
