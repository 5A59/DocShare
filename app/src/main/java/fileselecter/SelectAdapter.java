package fileselecter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import utils.GeneralUtils;
import zy.com.document.R;

/**
 * Created by zy on 15-12-19.
 */
public class SelectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<File> files;
    private OnRecyclerItemClickListener listener;

    public SelectAdapter(Context context, List<File> files){
        this.context = context;
        this.files = files;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select, null);
        view.setOnClickListener(this);
        return new SelectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SelectViewHolder selectViewHolder = (SelectViewHolder) holder;
        String fileName = files.get(position).getName();
        selectViewHolder.fileNameText.setText(fileName.equals("..") ? "上一级" : fileName);

        File tmpFile = files.get(position);
        GeneralUtils.getInstance().setIcon(context, selectViewHolder.fileIconImg, tmpFile);

        ((SelectViewHolder) holder).itemView.setTag(position);
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

    public class SelectViewHolder extends RecyclerView.ViewHolder{

        public View itemView;
        public LinearLayout fileLinear;
        public ImageView fileIconImg;
        public TextView fileNameText;
        public TextView fileIconNameText;

        public SelectViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.fileLinear = (LinearLayout) itemView.findViewById(R.id.linear_file);
            this.fileIconImg = (ImageView) itemView.findViewById(R.id.img_file_icon);
            this.fileNameText = (TextView) itemView.findViewById(R.id.text_file_name);
            this.fileIconNameText = (TextView) itemView.findViewById(R.id.text_file_icon_name);
        }


    }

}
