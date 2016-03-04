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
import zy.com.document.R;

/**
 * Created by zy on 15-12-19.
 */
public class TextAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<String> data;
    private OnRecyclerItemClickListener listener;

    public TextAdapter(Context context, List<String> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, null);
        view.setOnClickListener(this);
        return new DocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DocViewHolder docViewHolder = (DocViewHolder) holder;

        docViewHolder.itemView.setTag(position);
        docViewHolder.contentText.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
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

        public DocViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.contentText = (TextView) itemView.findViewById(R.id.text_item_content);
        }
    }

}
