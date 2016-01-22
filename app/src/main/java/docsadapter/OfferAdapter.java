package docsadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import docnetwork.dataobj.OfferReword;
import fileselecter.OnRecyclerItemClickListener;
import zy.com.document.R;

/**
 * Created by zy on 15-12-19.
 */
public class OfferAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private OfferReword offer;
    private OnRecyclerItemClickListener listener;

    public OfferAdapter(Context context, OfferReword offer){
        this.context = context;
        this.offer = offer;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doc, null);
        view.setOnClickListener(this);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OfferViewHolder offerViewHolder = (OfferViewHolder) holder;

        offerViewHolder.itemView.setTag(position);
        offerViewHolder.offerNameText.setText(offer.getOffer().get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return offer.getOffer().size();
    }

    public void setItemClickListener(OnRecyclerItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.onClick(v, (Integer) v.getTag());
        }
    }

    public class OfferViewHolder extends RecyclerView.ViewHolder{

        public View itemView;
        public TextView offerNameText;

        public OfferViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.offerNameText = (TextView) itemView.findViewById(R.id.text_item_doc_name);
        }
    }

}
