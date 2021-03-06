package igotplaced.com.layouts.CustomAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import igotplaced.com.layouts.Model.BlogHome;
import igotplaced.com.layouts.R;
import igotplaced.com.layouts.Utils.ClickListener;
import igotplaced.com.layouts.Utils.NetworkController;
import igotplaced.com.layouts.Utils.Utils;


public class RecyclerAdapterBlogHome extends RecyclerView.Adapter<RecyclerAdapterBlogHome.MyViewHolder> {

    private List<BlogHome> blogHomeList;
    private Context context;
    private LayoutInflater inflater;
    private ClickListener clickListener;

    public RecyclerAdapterBlogHome(Context context, List<BlogHome> blogList) {

        this.context = context;
        this.blogHomeList = blogList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.card_view_blog, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BlogHome blogView = blogHomeList.get(position);
        //Pass the values of feeds object to Views
        holder.blogPost.setText(blogView.getBlogPost());
        holder.blogPostedBy.setText("Posted by: "+blogView.getBlogPostedBy());
        holder.blogTime.setText(blogView.getBlogTime());

        Log.d("error", Utils.BaseImageUri + blogView.getImageName());

        holder.blog_img.setImageUrl(Utils.BaseImageUri + blogView.getImageName(), NetworkController.getInstance(context).getImageLoader());

    }

    @Override
    public int getItemCount() {
        return blogHomeList.size();
    }

    public void setClickListener(ClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView blogPost, blogPostedBy, blogTime;
        private NetworkImageView blog_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            blogPost = (TextView) itemView.findViewById(R.id.blog_post);
            blogPostedBy = (TextView) itemView.findViewById(R.id.blog_posted_by);
            blogTime = (TextView) itemView.findViewById(R.id.post_time);
            // Volley's NetworkImageView which will load Image from URL
            blog_img = (NetworkImageView) itemView.findViewById(R.id.blog_img);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }
    }

}