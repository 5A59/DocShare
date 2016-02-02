package docsadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import docnetwork.dataobj.OfferReword;
import fileselecter.OnRecyclerItemClickListener;
import utils.GeneralUtils;
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
        OfferReword.OfferMes offerMes = offer.getOffer().get(position);

        offerViewHolder.itemView.setTag(position);
        offerViewHolder.titleText.setText(offerMes.getTitle());
        offerViewHolder.contentText.setText(offerMes.getContent());
        offerViewHolder.nameText.setText(offerMes.getWritterName());
        offerViewHolder.timeText.setText(offerMes.getTime());
        GeneralUtils.getInstance().setHeadImg(context, offerViewHolder.headImg,
                GeneralUtils.getInstance().getFullUrl(offerMes.getWritterHeadImg()));
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
        public TextView titleText;
        public TextView contentText;
        public TextView nameText;
        public TextView timeText;
        public CircleImageView headImg;

        public OfferViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.titleText = (TextView) itemView.findViewById(R.id.text_item_doc_title);
            this.contentText= (TextView) itemView.findViewById(R.id.text_item_doc_content);
            this.nameText = (TextView) itemView.findViewById(R.id.text_item_writter_name);
            this.timeText = (TextView) itemView.findViewById(R.id.text_item_time);
            this.headImg = (CircleImageView) itemView.findViewById(R.id.img_item_writter_head);
        }
    }

}
