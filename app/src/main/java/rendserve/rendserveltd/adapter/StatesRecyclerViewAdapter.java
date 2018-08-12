package rendserve.rendserveltd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import rendserve.rendserveltd.Pojo.StatePojo;
import rendserve.rendserveltd.R;

/**
 * Created by abhi on 04-02-2018.
 */

public class StatesRecyclerViewAdapter extends RecyclerView.Adapter<StatesRecyclerViewAdapter.ViewHolder> {

    private List<StatePojo> offersList;
    private Context context;

    private int lastSelectedPosition = -1;
    public StatesRecyclerViewAdapter(List<StatePojo> offersListIn, Context ctx) {
        offersList = offersListIn;
        context = ctx;
    }

    @Override
    public StatesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_state_row, parent, false);

        StatesRecyclerViewAdapter.ViewHolder viewHolder = new StatesRecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final StatesRecyclerViewAdapter.ViewHolder holder,
                                 final int position) {
        StatePojo offersModel = offersList.get(position);
        holder.tv_state.setText(offersModel.getStateName());
        //since only one radio button is allowed to be selected,
        // this condition un-checks previous selections

        holder.rb_state_select.setChecked(offersModel.isCheck());

       /* holder.rl_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSelectedPosition = position;
                notifyDataSetChanged();
                tostShow(holder.tv_state.getText().toString());
            }
        });*/
        holder.rl_state.setOnClickListener((View.OnClickListener) context);
        holder.rl_state.setTag(R.string.state_name,position);


    }

    @Override
    public int getItemCount() {
        return offersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_state;
        public RelativeLayout rl_state;
        public RadioButton rb_state_select;

        public ViewHolder(View view) {
            super(view);
            tv_state = (TextView) view.findViewById(R.id.tv_state);
            rl_state = (RelativeLayout) view.findViewById(R.id.rl_state);
            rb_state_select = (RadioButton) view.findViewById(R.id.rb_state_select);


            /*rb_state_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                    Toast.makeText(StatesRecyclerViewAdapter.this.context,
                            "selected offer is " + tv_state.getText(), Toast.LENGTH_LONG).show();
                }
            });*/
        }
    }
}