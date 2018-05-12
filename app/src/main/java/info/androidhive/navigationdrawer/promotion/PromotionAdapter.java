package info.androidhive.navigationdrawer.promotion;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import info.androidhive.navigationdrawer.R;
import info.androidhive.navigationdrawer.activity.CreateroomActivity;

/**
 * Created by AndroidBash on 09/05/2016.
 */
public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.ViewHolder> {

    private Context context;
    private List<ListPromotion> listPromotions;

    public PromotionAdapter(Context context, List<ListPromotion> listPromotions) {
        this.context = context;
        this.listPromotions = listPromotions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.promotionName.setText(listPromotions.get(position).getPromotionName());
        holder.promotionGenre.setText(listPromotions.get(position).getPromotionGenre());
        Glide.with(context).load(listPromotions.get(position).getImageLink()).into(holder.imageView);
    }



    @Override
    public int getItemCount() {
        return listPromotions.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView promotionName;
        public TextView promotionGenre;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            promotionName = (TextView) itemView.findViewById(R.id.promotionname);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            promotionGenre = (TextView) itemView.findViewById(R.id.genre);
            imageView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            showPopupMenu(v,position);
        }
    }

    private void showPopupMenu(View view, int poaition) {
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_context, popup.getMenu());
        popup.setOnMenuItemClickListener(new MenuClickListener(poaition));
        popup.show();
    }


    class MenuClickListener implements PopupMenu.OnMenuItemClickListener {
        Integer pos;
        public MenuClickListener(int pos) {
            this.pos=pos;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_favourite:

                    Toast.makeText(context, listPromotions.get(pos).getpro() + " Create Room !", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, CreateroomActivity.class);
                    i.putExtra("pro_id", listPromotions.get(pos).getpro());

                    context.startActivity(i);
                    return true;
//               case R.id.action_watch:
//                    Toast.makeText(context, listPromotions.get(pos).getPromotionName()+" is added to watchlist", Toast.LENGTH_SHORT).show();
//                    return true;
//                case R.id.action_book:
//                    Toast.makeText(context, "Booked Ticket for "+ listPromotions.get(pos).getPromotionName(), Toast.LENGTH_SHORT).show();
//                    return true;
                default:
            }
            return false;
        }
    }
}
