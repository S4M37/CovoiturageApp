package tn.iac.mobiledevelopment.covoiturageapp.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import tn.iac.mobiledevelopment.covoiturageapp.activities.Parametre;
import tn.iac.mobiledevelopment.covoiturageapp.R;
import tn.iac.mobiledevelopment.covoiturageapp.models.RecylerViewInformation;

/**
 * Created by kalou on 15/01/2016.
 */
public class RecylerViewAdapter extends RecyclerView.Adapter<RecylerViewAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    List<RecylerViewInformation> data = Collections.emptyList();

    public RecylerViewAdapter(Context context, List<RecylerViewInformation> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.menu_recycler_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RecylerViewInformation current = data.get(position);
        holder.title.setText(current.getTitle());
        holder.icon.setImageResource(current.getIconId());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.ListText);
            icon = (ImageView) itemView.findViewById(R.id.Listicon);
        }

        @Override
        public void onClick(View v) {

            if (getPosition() == 4)
                context.startActivity(new Intent(context, Parametre.class));
            //  delete(getPosition());

        }
    }
}
