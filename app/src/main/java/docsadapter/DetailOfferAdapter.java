package docsadapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import docnetwork.dataobj.Answer;
import fileselecter.OnRecyclerItemClickListener;
import utils.GeneralUtils;
import utils.Logger;
import zy.com.document.R;

/**
 * Created by zy on 16-2-1.
 */
public class DetailOfferAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private String content;
    private List<Answer.AnsMes> data;
    private OnRecyclerItemClickListener listener;

    public DetailOfferAdapter(Context context, String content, List<Answer.AnsMes> data) {
        this.context = context;
        this.content = content;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_detail_offer, parent, false);
        view.setOnClickListener(this);
        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AnswerViewHolder ansViewHolder = (AnswerViewHolder) holder;

        ansViewHolder.itemView.setTag(position - 1);
        if (position == 0){
            ansViewHolder.itemLayout.setVisibility(View.GONE);
            ansViewHolder.contentText.setVisibility(View.VISIBLE);
            ansViewHolder.contentText.setText(content);
            return ;
        }
        ansViewHolder.contentText.setVisibility(View.GONE);
        ansViewHolder.itemLayout.setVisibility(View.VISIBLE);

        Answer.AnsMes tmpAns = data.get(position - 1);
        ansViewHolder.itemContentText.setText(tmpAns.getContent());
        ansViewHolder.nameText.setText(tmpAns.getWritterName());
        ansViewHolder.timeText.setText(tmpAns.getTime());
        GeneralUtils.getInstance().setHeadImg(context, ansViewHolder.headImg,
                GeneralUtils.getInstance().getFullUrl(tmpAns.getWritterHeadImg()));
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
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

    public class AnswerViewHolder extends RecyclerView.ViewHolder{

        public View itemView;
        public TextView contentText;
        public TextView itemContentText;
        public TextView timeText;
        public TextView nameText;
        public CircleImageView headImg;
        public LinearLayout itemLayout;

        public AnswerViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.itemLayout = (LinearLayout) itemView.findViewById(R.id.layout_item);
            this.contentText = (TextView) itemView.findViewById(R.id.text_content);
            this.itemContentText = (TextView) itemView.findViewById(R.id.text_item_content);
            this.timeText = (TextView) itemView.findViewById(R.id.text_item_time);
            this.nameText = (TextView) itemView.findViewById(R.id.text_item_name);
            this.headImg = (CircleImageView) itemView.findViewById(R.id.img_head);
        }
    }
}
