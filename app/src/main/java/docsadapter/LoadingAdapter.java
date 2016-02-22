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
import network.LoadingMes;
import utils.GeneralUtils;
import zy.com.document.R;

/**
 * Created by zy on 16-1-20.
 */
public class LoadingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private Context context;
    private List<LoadingMes> loadingMes;
    private OnRecyclerItemClickListener listener;

    public LoadingAdapter(Context context, List<LoadingMes> loadingMes){
        this.loadingMes = loadingMes;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download, parent, false);
        view.setOnClickListener(this);
        return new LoadingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LoadingViewHolder viewHolder = (LoadingViewHolder) holder;
        holder.itemView.setTag(position);
        LoadingMes mes = loadingMes.get(position);
        viewHolder.filaNameText.setText(mes.getSymbol());
        viewHolder.progressBar.setMax((int) mes.getAllLength());
        viewHolder.progressBar.setProgress((int) mes.getCurLength());
        if (mes.getCurLength() >= mes.getAllLength()){
            GeneralUtils.getInstance().setPic(context, viewHolder.progressImg,
                    R.mipmap.finish, R.mipmap.finish, R.mipmap.finish);
            viewHolder.progressBar.setMax(1);
            viewHolder.progressBar.setProgress(1);
        }else {
            GeneralUtils.getInstance().setPic(context, viewHolder.progressImg,
                    R.mipmap.loading, R.mipmap.loading, R.mipmap.loading);
        }
        GeneralUtils.getInstance().setIcon(context, viewHolder.iconImg, mes.getSymbol());
    }

    @Override
    public int getItemCount() {
        return loadingMes.size();
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

    public class LoadingViewHolder extends RecyclerView.ViewHolder{

        public View itemView;
        public TextView filaNameText;
        public ProgressBar progressBar;
        public ImageView progressImg;
        public ImageView iconImg;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.filaNameText = (TextView) itemView.findViewById(R.id.text_download);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.progress_download);
            this.progressImg = (ImageView) itemView.findViewById(R.id.img_progress_icon);
            this.iconImg = (ImageView) itemView.findViewById(R.id.img_file_icon);
        }
    }
}
