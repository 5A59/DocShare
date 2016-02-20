package fileselecter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import zy.com.document.R;

/**
 * Created by zy on 15-12-19.
 */
public class FileSelecterActivity extends Activity implements View.OnClickListener{
    public static final int FILE_SELECT_RES_CODE = 0;
    public static final String FILE_SELECT_RES_KEY = "files";

    private RecyclerView selectRecycle;
    private RecyclerView addRecycle;
    private RecyclerView pathRecycle;

    private Button sureButton;
    private Button cancelButton;

    private List<File> files;
    private List<File> addFileList;
    private List<File> pathFile;
    private Set<File> addFileSet;
    private Stack<File> pathStack;

    private File externalFile;

    private SelectAdapter adapter;
    private AddedAdapter addedAdapter;
    private PathAdapter pathAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_select);

        init();
    }

    private void init(){
        sureButton = (Button) this.findViewById(R.id.button_sure);
        cancelButton = (Button) this.findViewById(R.id.button_cancel);
        sureButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        selectRecycle = (RecyclerView) this.findViewById(R.id.recycle_select);
        addRecycle = (RecyclerView) this.findViewById(R.id.recycle_added);
        pathRecycle = (RecyclerView) this.findViewById(R.id.recycle_path);

        externalFile = Environment.getExternalStorageDirectory();
        files = new ArrayList<>();
        files.add(externalFile);

        addFileSet = new HashSet<>();
        addFileList = new ArrayList<>();
        pathFile = new ArrayList<>();
        pathFile.add(externalFile);

        pathStack = new Stack<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        selectRecycle.setLayoutManager(layoutManager);
        adapter = new SelectAdapter(this,files);

        OnRecyclerItemClickListener listener = new OnRecyclerItemClickListener() {
            @Override
            public void onClick(View v, int pos) {
                File tmpFile = files.get(pos);
                if (tmpFile.isDirectory()){
                    pathStack.add(tmpFile);
                    getSubFiles(tmpFile);
                    changePath();
                }else{
                    addFile(tmpFile);
                }
            }
        };

        adapter.setItemClickListener(listener);
        selectRecycle.setAdapter(adapter);

        addedAdapter = new AddedAdapter(this, addFileList);
        final RecyclerView.LayoutManager addLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        OnRecyclerItemClickListener addListener = new OnRecyclerItemClickListener() {
            @Override
            public void onClick(View v, int pos) {
                removeFile(addFileList.get(pos));
            }
        };
        addedAdapter.setItemClickListener(addListener);
        addRecycle.setLayoutManager(addLayoutManager);
        addRecycle.setAdapter(addedAdapter);

        pathAdapter = new PathAdapter(this, pathFile);
        final RecyclerView.LayoutManager pathLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        OnRecyclerItemClickListener pathListener = new OnRecyclerItemClickListener() {
            @Override
            public void onClick(View v, int pos) {
                int i = 0;
                while(!pathStack.isEmpty()){
                    if (pathFile.get(pos) == pathStack.peek()){
                        break;
                    }
                    pathStack.pop();
                }
                //TODO : 有个bug, 在选择文件界面一进去点上面path路径的0会奔溃，栈空错误
                getSubFiles(pathStack.peek());
                changePath();
            }
        };

        pathAdapter.setItemClickListener(pathListener);
        pathRecycle.setLayoutManager(pathLayoutManager);
        pathRecycle.setAdapter(pathAdapter);
    }

    private void getSubFiles(File file){
        if (file.isDirectory()){
            File[] subFiles = file.listFiles();
            file.list();
            files.clear();
            Collections.addAll(files, subFiles);
            adapter.notifyDataSetChanged();
        }
    }

    private void getCurFiles(File file){
        files.clear();
        files.add(file);
        adapter.notifyDataSetChanged();
    }

    private void addFile(File file){
        if (file != null){
            addFileSet.add(file);
        }
        addFileList.clear();
        addFileList.addAll(addFileSet);
        addedAdapter.notifyDataSetChanged();
    }

    private void removeFile(File file){
        addFileSet.remove(file);
        addFile(null);
    }

    private void changePath(){
        pathFile.clear();
        pathFile.addAll(pathStack);
        pathAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (pathStack.isEmpty()){
            mySetRes();
            super.onBackPressed();
            return ;
        }

        pathStack.pop();
        changePath();
        if (!pathStack.isEmpty()){
            getSubFiles(pathStack.peek());
            return ;
        }
        getCurFiles(externalFile);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_sure:
                sure();
                break;
            case R.id.button_cancel:
                cancel();
                break;
        }
    }

    private void sure(){
        mySetRes();
        this.finish();
    }

    private void cancel(){
        mySetRes();
        this.finish();
    }

    private void mySetRes(){
        Intent intent = new Intent();
        FileList fileSet = new FileList(addFileList);
        intent.putExtra(FILE_SELECT_RES_KEY, fileSet);
        setResult(FILE_SELECT_RES_CODE, intent);
    }

}
