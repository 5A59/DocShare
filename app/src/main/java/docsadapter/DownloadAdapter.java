package docsadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import fileselecter.OnRecyclerItemClickListener;
import network.DownloadMes;
import utils.GeneralUtils;
import utils.Logger;
import zy.com.document.R;

/**
 * Created by zy on 16-1-20.
 */
public class DownloadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private Context context;
    private List<DownloadMes> downloadMes;
    private OnRecyclerItemClickListener listener;

    public DownloadAdapter(Context context, List<DownloadMes> downloadMes){
        this.downloadMes = downloadMes;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download, parent, false);
        view.setOnClickListener(this);
        return new DownloadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DownloadViewHolder viewHolder = (DownloadViewHolder) holder;
        holder.itemView.setTag(position);
        DownloadMes mes = downloadMes.get(position);
        viewHolder.filaNameText.setText(mes.getFileName());
        viewHolder.progressBar.setMax((int) mes.getAllLength());
        viewHolder.progressBar.setProgress((int) mes.getCurLength());
        if (mes.getCurLength() >= mes.getAllLength()){
            GeneralUtils.getInstance().setPic(context, viewHolder.progressImg,
                    R.mipmap.finish, R.mipmap.finish, R.mipmap.finish);
        }else {
            GeneralUtils.getInstance().setPic(context, viewHolder.progressImg,
                    R.mipmap.loading, R.mipmap.loading, R.mipmap.loading);
        }
        GeneralUtils.getInstance().setIcon(context, viewHolder.iconImg, mes.getFileName());
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
        public ImageView progressImg;
        public ImageView iconImg;

        public DownloadViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.filaNameText = (TextView) itemView.findViewById(R.id.text_download);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.progress_download);
            this.progressImg = (ImageView) itemView.findViewById(R.id.img_progress_icon);
            this.iconImg = (ImageView) itemView.findViewById(R.id.img_file_icon);
        }
    }
}
