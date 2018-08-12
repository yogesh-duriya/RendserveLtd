package rendserve.rendserveltd.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import rendserve.rendserveltd.Pojo.ProjectDataPojo;
import rendserve.rendserveltd.Pojo.ProjectPojo;
import rendserve.rendserveltd.R;
import utils.RendserveConstant;

/**
 * Created by abhi on 09-02-2018.
 */

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    private ArrayList<ProjectDataPojo> projectList;
    private Activity context;

    private int lastSelectedPosition = -1;
    public ProjectAdapter(ArrayList<ProjectDataPojo> projectList, Activity ctx) {
        this.projectList = projectList;
        context = ctx;
    }

    @Override
    public ProjectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_projects, parent, false);

        ProjectAdapter.ViewHolder viewHolder = new ProjectAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ProjectAdapter.ViewHolder holder, final int position) {
        ProjectDataPojo projectModel = projectList.get(position);
        holder.tv_project_name.setText(projectModel.getProject_name());
        holder.tv_date.setText(projectModel.getStart_date());

        holder.rl_project.setOnClickListener((View.OnClickListener) context);
        holder.rl_project.setTag(R.string.state_id,projectModel.getId());
        holder.rl_project.setTag(R.string.project_name,projectModel.getProject_name());
        holder.iv_delete.setOnClickListener((View.OnClickListener) context);
        holder.iv_delete.setTag(R.string.state_id,projectModel.getId());
        Picasso picasso = Picasso.with(context);
        picasso.load(RendserveConstant.BASE_URL+projectModel.getLogo())
                .placeholder(R.drawable.ic_settings)
                .error(R.drawable.ic_settings).into(holder.iv_logo);
       /* holder.rl_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context.getApplicationContext(), "here is toast" , Toast.LENGTH_LONG);

            }
        });*/

    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_project_name,tv_date;
        public RelativeLayout rl_project;
        public ImageView iv_logo,iv_delete;


        public ViewHolder(View view) {
            super(view);
            tv_project_name = (TextView) view.findViewById(R.id.tv_project_name);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            iv_logo = (ImageView) view.findViewById(R.id.iv_logo);
            iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
            rl_project = (RelativeLayout) view.findViewById(R.id.rl_project);

        }


    }
}
