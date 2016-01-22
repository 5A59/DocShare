package docsadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import fileselecter.OnRecyclerItemClickListener;
import network.DownloadMes;
import utils.Logger;
import zy.com.document.R;

/**
 * Created by zy on 16-1-20.
 */
public class DownloadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private List<DownloadMes> downloadMes;
    private OnRecyclerItemClickListener listener;

    public DownloadAdapter(List<DownloadMes> downloadMes){
        this.downloadMes = downloadMes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download, null);
        view.setOnClickListener(this);
        return new DownloadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DownloadViewHolder viewHolder = (DownloadViewHolder) holder;
        holder.itemView.setTag(position);
        viewHolder.filaNameText.setText(downloadMes.get(position).getFileName());
        viewHolder.progressBar.setMax((int) downloadMes.get(position).getAllLength());
        viewHolder.progressBar.setProgress((int) downloadMes.get(position).getCurLength());
    }

    @Override
    public int getItemCount() {
        return downloadMes.size();
    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.onClick(v, (Integer) v.getTag());
        }
    }

    public void setItemClickListener(OnRecyclerItemClickListener listener){
        this.listener = listener;
    }

    public class DownloadViewHolder extends RecyclerView.ViewHolder{

        public View itemView;
        public TextView filaNameText;
        public ProgressBar progressBar;

        public DownloadViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.filaNameText = (TextView) itemView.findViewById(R.id.text_download);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.progress_download);
        }
    }
}
