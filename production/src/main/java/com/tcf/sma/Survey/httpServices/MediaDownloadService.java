package com.tcf.sma.Survey.httpServices;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.InputStream;


public class MediaDownloadService extends AsyncTask<Void, Void, Boolean> implements IHttpPayloadService {

    final Object tagData;
    IHttpPayloadRequestEventsHandler callbackHandler;
    String downloadUrl;
    byte[] data = null;

    public MediaDownloadService(IHttpPayloadRequestEventsHandler handler, String Url, Object tag) {
        callbackHandler = handler;
        downloadUrl = Url;
        tagData = tag;

    }

    @Override
    protected Boolean doInBackground(Void... params) {

        String url = downloadUrl;
        //String url = params[0].toString();
        //String name = params[1].toString();


        HttpClient client = new DefaultHttpClient();
        //HttpPost post = new HttpPost(url);
        HttpGet get = new HttpGet(url);
        //List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        //paramList.add(new BasicNameValuePair("name", name));


        try {
            //post.setEntity(new UrlEncodedFormEntity(paramList));
            HttpResponse resp = client.execute(get);
            InputStream is = resp.getEntity().getContent();
            int contentSize = (int) resp.getEntity().getContentLength();
            System.out.println("Content size [" + contentSize + "]");
            BufferedInputStream bis = new BufferedInputStream(is, 512);

            System.out.println(resp.getStatusLine().getStatusCode());
            if (resp.getStatusLine().getStatusCode() < 200 || resp.getStatusLine().getStatusCode() > 210)
                return false;

            data = new byte[contentSize];
            int bytesRead = 0;
            int offset = 0;

            while (bytesRead != -1 && offset < contentSize) {
                bytesRead = bis.read(data, offset, contentSize - offset);
                offset += bytesRead;
            }
        } catch (Throwable t) {
            // Handle error here
            t.printStackTrace();
            return false;
        }


        return true;

    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        if (result == true) {
            HttpEventsArgs args = new HttpEventsArgs(callbackHandler, this, tagData, data);
            callbackHandler.onHttpDownloadCompleted(this, args);
        } else {
            HttpEventsArgs args = new HttpEventsArgs(callbackHandler, this, tagData, null);
            callbackHandler.onHttpDownloadFailed(this, args);
        }
    }

    @Override
    public String getRequestURL() {
        return this.downloadUrl;
    }


}
