package docsadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import fileselecter.OnRecyclerItemClickListener;
import zy.com.document.R;

/**
 * Created by zy on 15-12-19.
 */
public class DetailDocAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private String[] files;
    private OnRecyclerItemClickListener listener;

    public DetailDocAdapter(Context context, String[] files){
        this.context = context;
        this.files = files;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_doc, null);
        view.setOnClickListener(this);
        return new DocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DocViewHolder docViewHolder = (DocViewHolder) holder;

        docViewHolder.itemView.setTag(position);
        docViewHolder.docNameText.setText(files[position]);
    }

    @Override
    public int getItemCount() {
        return files.length;
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
        public TextView docNameText;
        public ProgressBar progressBar;

        public DocViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.docNameText = (TextView) itemView.findViewById(R.id.text_item_doc_name);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.progress_doc_download);
        }
    }

}
