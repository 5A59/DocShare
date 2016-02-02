package docsadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import docnetwork.dataobj.Doc;
import fileselecter.OnRecyclerItemClickListener;
import utils.GeneralUtils;
import zy.com.document.R;

/**
 * Created by zy on 15-12-19.
 */
public class DocAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private Doc doc;
    private OnRecyclerItemClickListener listener;

    public DocAdapter(Context context, Doc doc){
        this.context = context;
        this.doc = doc;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doc, null);
        view.setOnClickListener(this);
        return new DocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DocViewHolder docViewHolder = (DocViewHolder) holder;

        Doc.DocMes docMes = doc.getDoc().get(position);
        docViewHolder.itemView.setTag(position);
        docViewHolder.titleText.setText(docMes.getTitle());
        docViewHolder.contentText.setText(docMes.getContent());
        docViewHolder.nameText.setText(docMes.getWritterName());
        docViewHolder.timeText.setText(docMes.getTime());
        GeneralUtils.getInstance().setHeadImg(context, docViewHolder.headImg,
                GeneralUtils.getInstance().getFullUrl(docMes.getWritterHeadImg()));
    }

    @Override
    public int getItemCount() {
        return doc.getDoc().size();
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
        public TextView titleText;
        public TextView contentText;
        public TextView nameText;
        public TextView timeText;
        public CircleImageView headImg;

        public DocViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.titleText = (TextView) itemView.findViewById(R.id.text_item_doc_title);
            this.contentText = (TextView) itemView.findViewById(R.id.text_item_doc_content);
            this.nameText = (TextView) itemView.findViewById(R.id.text_item_writter_name);
            this.timeText = (TextView) itemView.findViewById(R.id.text_item_time);
            this.headImg = (CircleImageView) itemView.findViewById(R.id.img_item_writter_head);
        }
    }

}
