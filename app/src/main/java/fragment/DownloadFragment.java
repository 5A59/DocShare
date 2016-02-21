package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fileselecter.OnRecyclerItemClickListener;
import fileselecter.SelectAdapter;
import utils.GeneralUtils;
import zy.com.document.R;

/**
 * Created by zy on 16-2-20.
 */
public class DownloadFragment extends Fragment {

    private RecyclerView recyclerView;
    private SelectAdapter selectAdapter;
    private View rootView;
    private List<File> files;

    private File mainFile;

    public DownloadFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_download, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
        initView();
    }

    private void initData(){
        files = new ArrayList<>();
        mainFile = new File(GeneralUtils.getInstance().getFileSavePath());
        if (!mainFile.exists()){
            mainFile.mkdir();
        }
        File[] tmpFiles = mainFile.listFiles(new DirFileFilter());

        Collections.addAll(files, tmpFiles);
    }

    private void initView(){
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_path);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        selectAdapter = new SelectAdapter(this.getActivity(), files);
        selectAdapter.setItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onClick(View v, int pos) {
                selectFiles(pos);
            }
        });
//        selectAdapter.setItemClickListener((View v, int pos) -> selectFiles(pos)); //lambda
        recyclerView.setAdapter(selectAdapter);
    }

    private void selectFiles(int pos){
        File file = files.get(pos);
        if (file.isDirectory()){
            files.clear();
            File[] tmpFiles;
            if (!file.getName().equals("..")){
                File parentFile = new File(file.getAbsolutePath() + "/..");
                files.add(parentFile);
                tmpFiles = file.listFiles();
            }else {
                tmpFiles = file.listFiles(new DirFileFilter());
            }
            Collections.addAll(files, tmpFiles);
            selectAdapter.notifyDataSetChanged();
            return ;
        }

        openFile(file);
    }

    private void openFile(File file){
        GeneralUtils.getInstance().openFile(this.getActivity(), file);
    }

    public static class DirFileFilter implements FileFilter {

        @Override
        public boolean accept(File pathname) {
            if (pathname.isDirectory()){
                return true;
            }
            return false;
        }
    }

}



