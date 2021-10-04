package softcare.game.model;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import softcare.game.R;
import softcare.gui.PointClickListener;
import softcare.util.S;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LevelViewHolder> {
    private Context mContext;
    private  Location  locations;

    public LocationAdapter(Context context ) {
        this.mContext = context; 
    }

    @NonNull
    @Override
    public LevelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_level, parent, false);
        return new LevelViewHolder(view);
    }

    PointClickListener pointClickListener;

    public void setPointClickListener(PointClickListener pointClickListener) {
        this.pointClickListener = pointClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull final LevelViewHolder holder, int position) {

        if(locations!=null) {
            holder.title.setText(locations.getNames().get(position));
            holder.lati.setText(S.doubleToString(locations.getLocations().get(position).x));
            holder.logi.setText(S.doubleToString(locations.getLocations().get(position).x));
        holder.container.setOnClickListener(v->{
            if(pointClickListener!=null) pointClickListener.click(position);
        });
        }

    }


    public void changeData(Location location) {
        this.locations=locations;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
      return   (locations==null)?0: locations.getNames().size();

    }

    public static class LevelViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView lati;
        TextView logi;
        ConstraintLayout container;

        public LevelViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            logi = itemView.findViewById(R.id.logi);
            lati = itemView.findViewById(R.id.lati);
            container = itemView.findViewById(R.id.container);

        }
    }

}

