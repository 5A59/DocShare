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
public class AddedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<File> files;
    private OnRecyclerItemClickListener listener;

    public AddedAdapter(Context context, List<File> files){
        this.context = context;
        this.files = files;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add, null);
        view.setOnClickListener(this);
        return new AddedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AddedViewHolder addedViewHolder = (AddedViewHolder) holder;
        addedViewHolder.fileNameText.setText(files.get(position).getName());

        File tmpFile = files.get(position);
        if (tmpFile.isFile()){
            GeneralUtils.getInstance().setIcon(context, addedViewHolder.iconImg, tmpFile);
//            String postfix = GeneralUtils.getInstance().getFilePostfix(tmpFile.getName());

//            if (GeneralUtils.getInstance().ifPic(postfix)){
//                GeneralUtils.getInstance().setPic(context, addedViewHolder.iconImg, tmpFile);
//            }else{
//                addedViewHolder.iconImg.setImageResource(GeneralUtils.getInstance().getBackByPostfix(postfix));
//            }
//            if (postfix != null){
//                addedViewHolder.iconImg.setImageResource(GeneralUtils.getInstance().getBackByPostfix(postfix));
//            }
        }

        ((AddedViewHolder) holder).itemView.setTag(position);
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

    public class AddedViewHolder extends RecyclerView.ViewHolder{

        public View itemView;
        public LinearLayout addLinear;
        public ImageView iconImg;
        public TextView fileNameText;

        public AddedViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.addLinear = (LinearLayout) itemView.findViewById(R.id.linear_add);
            this.iconImg = (ImageView) itemView.findViewById(R.id.img_file_icon);
            this.fileNameText = (TextView) itemView.findViewById(R.id.text_file_name);
        }


    }

}
