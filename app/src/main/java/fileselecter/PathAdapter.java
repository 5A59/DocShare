package fileselecter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import zy.com.document.R;

/**
 * Created by zy on 15-12-19.
 */
public class PathAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<File> files;
    private OnRecyclerItemClickListener listener;

    public PathAdapter(Context context, List<File> files){
        this.context = context;
        this.files = files;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_path, null);
        view.setOnClickListener(this);
        return new PathViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PathViewHolder PathViewHolder = (PathViewHolder) holder;
        PathViewHolder.fileNameText.setText(files.get(position).getName() + "> ");

        ((PathViewHolder) holder).itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return files.size();
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

    public class PathViewHolder extends RecyclerView.ViewHolder{

        public View itemView;
        public LinearLayout pathLinear;
        public TextView fileNameText;

        public PathViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.pathLinear = (LinearLayout) itemView.findViewById(R.id.linear_path);
            this.fileNameText = (TextView) itemView.findViewById(R.id.text_path);
        }

    }

}
