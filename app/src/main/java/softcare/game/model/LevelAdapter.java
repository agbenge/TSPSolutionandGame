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

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.LevelViewHolder> {
    private final Context mContext;
    private List<Integer>  game;
    private int unlock;
    private final String name;

    public LevelAdapter(Context context, String name) {
        this.mContext = context;
        this.name = name;
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

        if(game!=null) {
          final   int posi= game.get(position);
            holder.title.setText(name);
            holder.level.setText(String.valueOf(posi ));
            if (unlock >= posi) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.level.setForeground(null);
                }
                holder.title.setBackground(null);
                holder.container.setOnClickListener(v -> {
                    if (pointClickListener != null)
                        pointClickListener.click(posi);
                });
            } else {
                Log.d( CodeX.tag," Lock  posi" +posi+"  position "+position+" ");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)      holder.title.setBackground(null);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.level.setForeground(mContext.getDrawable(R.drawable.ic_baseline_lock_24));
                    holder.title.setBackground(null);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.title.setBackground(mContext.getDrawable(R.drawable.ic_baseline_lock_24));
                    }else{
                      ///  holder.title.setBackground( R.drawable.ic_baseline_lock_24));

                    }
                }
                holder.container.setOnClickListener(v -> Toast.makeText(mContext, mContext.getString(R.string.level_lock), Toast.LENGTH_LONG).show());
            }
        }

    }


    public void changeSize(int unlock, List<Integer>  i) {
        this.game= i;
        this.unlock = unlock;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
      return   (game==null)?0: game.size();

    }

    public static class LevelViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView level;
        ConstraintLayout container;

        public LevelViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            level = itemView.findViewById(R.id.level);
            container = itemView.findViewById(R.id.container);

        }
    }

}

