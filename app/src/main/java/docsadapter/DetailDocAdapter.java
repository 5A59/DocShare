package docsadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import utils.GeneralUtils;

/**
 * Created by zy on 16-2-1.
 */
public class DetailDocAdapter extends DetailAdapter{

    private Context context;
    private String content;
    private List<String> data;

    public DetailDocAdapter(Context context, String code, String content, List<String> data) {
        super(context, "资料码 : " + code + " (点击复制)\n\n" + content, data);

        this.context = context;
        this.content = content;
        this.data = data;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DocViewHolder docViewHolder = (DocViewHolder) holder;
        if (position > 0){
            GeneralUtils.getInstance().setIcon(context, docViewHolder.iconImg, data.get(position - 1));
        }
        super.onBindViewHolder(holder, position);
    }
}
