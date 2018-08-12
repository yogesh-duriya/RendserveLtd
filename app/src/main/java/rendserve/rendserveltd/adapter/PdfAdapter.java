package rendserve.rendserveltd.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import rendserve.rendserveltd.Pojo.ReportDataPojo;
import rendserve.rendserveltd.R;

/**
 * Created by abhi on 21-03-2018.
 */

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.ViewHolder> {

    private ArrayList<ReportDataPojo> logsList;
    private Activity context;
    private String project_name;

    private int lastSelectedPosition = -1;
    public PdfAdapter(ArrayList<ReportDataPojo> logsList, Activity ctx,String project_name) {
        this.logsList = logsList;
        context = ctx;
        this.project_name = project_name;
    }

    @Override
    public PdfAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_pdf_list, parent, false);

        PdfAdapter.ViewHolder viewHolder = new PdfAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PdfAdapter.ViewHolder holder, final int position) {
        ReportDataPojo logModel = logsList.get(position);
        holder.tv_project_name.setText(project_name);
        holder.tv_date.setText(logModel.getStartdate()/*+" "+ logModel.getTime_created()*/);

        holder.rl_pdf.setOnClickListener((View.OnClickListener) context);
        holder.rl_pdf.setTag(R.string.pdf_id,logModel.getId());
        holder.rl_pdf.setTag(R.string.pdf_link,logModel.getFile());

/*        holder.iv_delete_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logsList.remove(position);
                //call delete api
                notifyDataSetChanged();
            }
        });*/
       /* holder.iv_delete_log.setTag(R.string.save_log_id,logModel.getId());
        holder.iv_delete_log.setTag(R.string.save_log_project_id,logModel.getProject_id());
*/
        /*if (logModel.isEdit() == false){
            holder.iv_next.setVisibility(View.VISIBLE);
            holder.iv_delete_log.setVisibility(View.GONE);
        }else {
            holder.iv_next.setVisibility(View.GONE);
            holder.iv_delete_log.setVisibility(View.VISIBLE);
        }*/
       /* holder.rl_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context.getApplicationContext(), "here is toast" , Toast.LENGTH_LONG);

            }
        });*/

    }

    @Override
    public int getItemCount() {
        return logsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_project_name,tv_date;
        public RelativeLayout rl_pdf;
        public ImageView iv_next,iv_delete_log;

        public ViewHolder(View view) {
            super(view);
            tv_project_name = (TextView) view.findViewById(R.id.tv_project_name);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            rl_pdf = (RelativeLayout) view.findViewById(R.id.rl_pdf);
           // iv_delete_log = (ImageView) view.findViewById(R.id.iv_delete_log);
            //iv_next = (ImageView) view.findViewById(R.id.iv_next);

        }


    }
}
