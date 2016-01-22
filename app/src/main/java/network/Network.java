package network;

import android.content.Context;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.internal.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;
import java.util.Map;
import java.util.Set;

import network.cookie.PersistentCookieStore;
import network.listener.DownloadProcessListener;
import network.listener.UploadProcessListener;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import utils.Logger;

/**
 * Created by zy on 15-11-21.
 */
public class Network {

    private static final int DOWNLOAD_BUFF_SIZE = 2048;
    private static final int UPLOAD_BUFF_SIZE = DOWNLOAD_BUFF_SIZE;

    private static Network network;
    private static OkHttpClient defaultClient;

    private Network(){
        defaultClient = new OkHttpClient();
    }

    private static synchronized void syncInit(){
        if (network == null){
            network = new Network();
        }
    }

    public static Network getInstance(){
        if (network == null){
            syncInit();
        }

        return network;
    }

    public OkHttpClient getDefaultClient(){
        return defaultClient;
    }

    public void setDefaultCookie(Context context){
        defaultClient.setCookieHandler(new CookieManager(new PersistentCookieStore(context),
                CookiePolicy.ACCEPT_ALL));
    }

    public void setCookie(CookieManager cookie){
        defaultClient.setCookieHandler(cookie);
    }

    public Response get(String url) throws IOException {
        return get(defaultClient,url,null);
    }

    public Response get(String url,Map<String,String> para) throws IOException {
        return get(defaultClient,url,HttpHeader.getHeader(),para);
    }

    public Response get(OkHttpClient client, String url, Map<String,String> para) throws IOException {
        return get(client,url,HttpHeader.getHeader(),para);
    }

    public Response get(OkHttpClient client, String url,
                        Map<String,String> header, Map<String,String> para) throws IOException {
        if (client == null || url == null || url.isEmpty()){
            return null;
        }

        StringBuilder newUrl = new StringBuilder(url);

        if (para != null && ! para.isEmpty()){
            buildUrl(newUrl,para);
        }

        Request.Builder builder = new Request.Builder();

        if (header != null && ! header.isEmpty()){
            setHeader(builder,header);
        }

        Request request = builder.url(newUrl.toString())
                .build();

        Response response = client.newCall(request).execute();

        return response;
    }

    public Response post(String url,Map<String,String> para) throws IOException {
        return post(defaultClient, url, para);
    }

    public Response post(OkHttpClient client, String url, Map<String,String> para) throws IOException {
        return post(client, url, HttpHeader.getHeader(), para);
    }

    public Response post(OkHttpClient client, String url,
                     Map<String,String> header, Map<String,String> para) throws IOException {
        if (client == null || url == null || url.isEmpty()){
            return null;
        }

        Request.Builder builder = new Request.Builder();

        if (header != null && ! header.isEmpty()){
            setHeader(builder,header);
        }

        if (para != null && ! para.isEmpty()){
            buildPostPara(builder, para);
        }
        builder.url(url);

        Request request = builder.build();

        Response response = client.newCall(request).execute();

        return response;
    }

    public Response postFile(String url,Map<String,String> para,Map<String,File> file) throws IOException {
        return postFile(defaultClient, url, para, file);
    }

    public Response postFile(OkHttpClient client,String url,
                             Map<String,String> para,Map<String,File> file) throws IOException {
        return postFile(client, url, HttpHeader.getHeader(), para, file);
    }

    public Response postFile(OkHttpClient client,String url,
                             Map<String,String> header,Map<String,String> para,Map<String,File> files) throws IOException {
        if (client == null || url == null || url.isEmpty()
                || files == null || files.isEmpty()){
            return null;
        }

        Request.Builder builder = new Request.Builder();

        if (header != null){
            setHeader(builder, header);
        }

        buildPostFile(builder, para, files);

        builder.url(url);
        Request request = builder.build();

        Response response = client.newCall(request).execute();

        return  response;
    }

    public Response postFile(String url,Map<String,String> para,Map<String,List<File>> file,
                             boolean mulFile) throws IOException {
        return postFile(defaultClient, url, para,file, mulFile);
    }

    public Response postFile(String url,Map<String,String> para,Map<String,List<File>> file,
                             Map<String, UploadProcessListener> listener) throws IOException {
        return postFile(defaultClient, url, HttpHeader.getHeader(),para, file, listener);
    }

    public Response postFile(OkHttpClient client,String url,
                             Map<String,String> para,Map<String,List<File>> file, boolean mulFile) throws IOException {
        return postFile(client, url, HttpHeader.getHeader(),para, file, mulFile);
    }

    public Response postFile(OkHttpClient client,String url,
                             Map<String,String> header, Map<String,String> para,
                             Map<String,List<File>> files, boolean mulFile) throws IOException {
        if (client == null || url == null || url.isEmpty()
                || files == null || files.isEmpty()){
            return null;
        }

        Request.Builder builder = new Request.Builder();

        if (header != null){
            setHeader(builder,header);
        }

        buildPostFile(builder, para, files, mulFile);

        builder.url(url);
        Request request = builder.build();

        Response response = client.newCall(request).execute();

        return  response;
    }

    public Response postFile(OkHttpClient client,String url,
                             Map<String,String> header, Map<String,String> para,
                             Map<String,List<File>> files, Map<String, UploadProcessListener> listener) throws IOException {
        if (client == null || url == null || url.isEmpty()
                || files == null || files.isEmpty()){
            return null;
        }

        Request.Builder builder = new Request.Builder();

        if (header != null){
            setHeader(builder,header);
        }

        buildPostFile(builder, para, files, listener);

        builder.url(url);
        Request request = builder.build();

        Response response = client.newCall(request).execute();

        return  response;
    }

    public void downloadFile(DownloadProcessListener listener, String url, File toFile){
        downloadFile(defaultClient, listener, url, toFile);
    }

    public void downloadFile(OkHttpClient client, final DownloadProcessListener listener, final String url, final File toFile){
        client.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                return response.newBuilder()
                        .body(new ProgressResponseBody(response.body(), listener))
                        .build();
            }
        });

        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                Logger.e("file download failed --- url : " + url);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                int len;
                byte[] buff = new byte[DOWNLOAD_BUFF_SIZE];
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = new FileOutputStream(toFile);

                while((len = inputStream.read(buff)) != -1){
                    fileOutputStream.write(buff, 0, len);
                }

                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
            }
        });
    }

    public void buildPostPara(Request.Builder builder, Map<String,String> para){
        if (builder == null || para == null || para.isEmpty()){
            return ;
        }

        FormEncodingBuilder paraBuilder = new FormEncodingBuilder();

        Set<String> key = para.keySet();
        for (String k : key){
            paraBuilder.add(k, para.get(k));
        }

        builder.post(paraBuilder.build());
    }

    public void buildPostFile(Request.Builder builder,Map<String,String> para,Map<String,File> files){
        if (builder == null || files == null || files.isEmpty()){
            return ;
        }

        MultipartBuilder fileBuilder = new MultipartBuilder();
        fileBuilder.type(MultipartBuilder.FORM);

        Set<String> key = files.keySet();
        for (String k : key){
            fileBuilder.addFormDataPart(k, files.get(k).getName(),
                    RequestBody.create(null, files.get(k)));
        }

        key = para.keySet();
        for (String k : key){
            fileBuilder.addFormDataPart(k, para.get(k));
        }

        builder.post(fileBuilder.build());
    }

    public void buildPostFile(Request.Builder builder, Map<String,String> para, Map<String,List<File>> files,
                              boolean mulFile){

        if (builder == null || files == null || files.isEmpty()){
            return ;
        }

        MultipartBuilder fileBuilder = new MultipartBuilder();
        fileBuilder.type(MultipartBuilder.FORM);

        Set<String> key = files.keySet();
        for (String k : key){
            for (File f : files.get(k)){
                fileBuilder.addFormDataPart(k, f.getName(),
                        RequestBody.create(null, f));
            }
        }

        key = para.keySet();
        for (String k : key){
            fileBuilder.addFormDataPart(k, para.get(k));
        }

        builder.post(fileBuilder.build());
    }

    public void buildPostFile(Request.Builder builder, Map<String,String> para, Map<String,List<File>> files,
                              Map<String, UploadProcessListener > listeners){

        if (builder == null || files == null || files.isEmpty()){
            return ;
        }

        MultipartBuilder fileBuilder = new MultipartBuilder();
        fileBuilder.type(MultipartBuilder.FORM);

        Set<String> key = files.keySet();
        for (String k : key){
            for (File f : files.get(k)){
                if (listeners == null){
                    fileBuilder.addFormDataPart(k, f.getName(),
                            RequestBody.create(null, f));
                }else {
                    fileBuilder.addFormDataPart(k, f.getName(),
                            new ProgressRequestBody(null, f, listeners.get(f.getAbsolutePath())));
                }
            }
        }

        key = para.keySet();
        for (String k : key){
            fileBuilder.addFormDataPart(k, para.get(k));
        }

        builder.post(fileBuilder.build());
    }

    private void buildUrl(StringBuilder url, Map<String,String> para){
        if (para == null || para.isEmpty()){
            return ;
        }
        Set<String> key = para.keySet();
        url.append("?");
        for (String k : key){
            url.append(k)
                    .append("=")
                    .append(para.get(k))
                    .append("&");
        }
        url.deleteCharAt(url.length() - 1);
    }

    private void setHeader(Request.Builder builder,Map<String,String> header){
        Set<String> key = header.keySet();
        for (String k : key){
            builder.addHeader(k, header.get(k));
        }
    }

    public static class ProgressResponseBody extends ResponseBody{

        private DownloadProcessListener listener;
        private ResponseBody responseBody;
        private BufferedSource bufferedSource;

        public ProgressResponseBody(ResponseBody responseBody, DownloadProcessListener listener){
            this.responseBody = responseBody;
            this.listener = listener;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() throws IOException {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() throws IOException {
            if (bufferedSource == null){
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source){
            return new ForwardingSource(source) {
                long totalByteRead = 0L;
                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long byteRead = super.read(sink, byteCount);
                    if (byteRead != -1){
                        totalByteRead += byteRead;
                    }
                    listener.update(totalByteRead, responseBody.contentLength(), byteRead == -1);
                    return byteRead;
                }
            };
        }
    }

    public static class ProgressRequestBody extends RequestBody{

        private MediaType mediaType;
        private File file;
        private UploadProcessListener listener;

        public ProgressRequestBody(MediaType mediaType, File file, UploadProcessListener listener){
            this.mediaType = mediaType;
            this.file = file;
            this.listener = listener;
        }

        @Override
        public long contentLength() throws IOException {
            return file.length();
        }

        @Override
        public MediaType contentType() {
            return mediaType;
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            if (file == null){
                return ;
            }

            Source source = null;
            try {
                source = Okio.source(file);
                Buffer buffer = new Buffer();
                long hasWrite = 0l;
                long readCount = 0l;
                while ((readCount = source.read(buffer, UPLOAD_BUFF_SIZE)) != -1) {
                    sink.write(buffer, readCount);
                    hasWrite += readCount;
                    listener.update(hasWrite, contentLength(), readCount == -1);
                }
            }finally {
                Util.closeQuietly(source);
            }

        }
    }

}

