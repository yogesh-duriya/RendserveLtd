package rendserve.rendserveltd.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import rendserve.rendserveltd.Pojo.ProjectDataPojo;
import rendserve.rendserveltd.R;
import utils.RendserveConstant;

/**
 * Created by abhi on 25-03-2018.
 */

public class WorkLogsAdapter extends RecyclerView.Adapter<WorkLogsAdapter.ViewHolder> {

    private JSONArray list;
    private Activity context;
    private String from;

    public WorkLogsAdapter(JSONArray list, Activity ctx, String from) {
        this.list = list;
        this.from = from;
        context = ctx;
    }

    @Override
    public WorkLogsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_work_log_labor, parent, false);

        WorkLogsAdapter.ViewHolder viewHolder = new WorkLogsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final WorkLogsAdapter.ViewHolder holder, final int position) {
        try {
            JSONObject obj = (JSONObject) list.get(position);
            if(from.equals("labor")){
                holder.tv_title1.setText("Labor: ");
                holder.tv_title2.setText("Work time: ");
                holder.tv_title3.setText("Travel time: ");
                holder.tv_labor_type.setText(obj.get("labour_type").toString());
                holder.tv_work_time.setText(obj.get("workhours").toString());
                holder.tv_travel_time.setText(obj.get("travelhour").toString());
            }else if(from.equals("constructor")){
                holder.tv_title1.setText("Subcontract: ");
                holder.tv_title2.setText("Work time: ");
                holder.tv_title3.setText("Travel time: ");
                holder.tv_labor_type.setText(obj.get("labour_type").toString());
                holder.tv_work_time.setText(obj.get("workhours").toString());
                holder.tv_travel_time.setText(obj.get("travelhour").toString());
            }else if(from.equals("rental")){
                holder.tv_title1.setText("Equipmetn: ");
                holder.tv_title2.setText("Hours: ");
                holder.tv_title3.setText("Total Cost: ");
                holder.tv_labor_type.setText(obj.get("equipment").toString());
                holder.tv_work_time.setText(obj.get("hour").toString());
                holder.tv_travel_time.setText(obj.get("totalcost").toString());
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return list.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_labor_type,tv_work_time, tv_travel_time, tv_title1, tv_title2, tv_title3;

        public ViewHolder(View view) {
            super(view);
            tv_labor_type = (TextView) view.findViewById(R.id.tv_labor_type);
            tv_work_time = (TextView) view.findViewById(R.id.tv_work_time);
            tv_travel_time = (TextView) view.findViewById(R.id.tv_travel_time);
            tv_title1 = (TextView) view.findViewById(R.id.tv_title1);
            tv_title2 = (TextView) view.findViewById(R.id.tv_title2);
            tv_title3 = (TextView) view.findViewById(R.id.tv_title3);

        }


    }
}
