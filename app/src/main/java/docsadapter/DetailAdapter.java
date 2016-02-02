package docsadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import fileselecter.OnRecyclerItemClickListener;
import utils.GeneralUtils;
import zy.com.document.R;

/**
 * Created by zy on 15-12-19.
 */
public class DetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private String content;
    private List<String> data;
    private OnRecyclerItemClickListener listener;

    public DetailAdapter(Context context, String content, List<String> data){
        this.context = context;
        this.content = content;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail, null);
        view.setOnClickListener(this);
        return new DocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DocViewHolder docViewHolder = (DocViewHolder) holder;

        docViewHolder.itemView.setTag(position - 1);
        if (position == 0){
            docViewHolder.itemLayout.setVisibility(View.GONE);
            docViewHolder.contentText.setVisibility(View.VISIBLE);
            docViewHolder.contentText.setText(content);
            return ;
        }
        docViewHolder.contentText.setVisibility(View.GONE);
        docViewHolder.itemLayout.setVisibility(View.VISIBLE);

        docViewHolder.itemContentText.setText(data.get(position - 1));
//        GeneralUtils.getInstance().setIcon(context, docViewHolder.iconImg, data[position - 1]);
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

    public class DocViewHolder extends RecyclerView.ViewHolder{

        public View itemView;
        public TextView contentText;
        public TextView itemContentText;
        public ImageView iconImg;
        public ImageView actionImg;
        public RelativeLayout itemLayout;

        public DocViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.itemContentText = (TextView) itemView.findViewById(R.id.text_item_content);
            this.itemLayout = (RelativeLayout) itemView.findViewById(R.id.layout_item);
            this.contentText = (TextView) itemView.findViewById(R.id.text_content);
            this.actionImg = (ImageView) itemView.findViewById(R.id.img_action);
            this.iconImg = (ImageView) itemView.findViewById(R.id.img_icon);
        }
    }

}
