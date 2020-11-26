package rai.satyam.wheatherproject.util.http;

import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DownloadTask extends AsyncTask<String , Integer , DownloadTaskResult> {
    private DownloadCallback<DownloadTaskResult> g_objCallBack;

    public DownloadTask(DownloadCallback<DownloadTaskResult> prm_objCallBack) {
        this.g_objCallBack = prm_objCallBack;
    }

    public void setCallBack(DownloadCallback<DownloadTaskResult> prm_objCallBack) {
        this.g_objCallBack = prm_objCallBack;
    }

    @Override
    protected void onPreExecute() {
        NetworkInfo v_objNetworkInfo;
        if (this.g_objCallBack != null){
            v_objNetworkInfo = this.g_objCallBack.getActiveNetworkInfo();
            if (v_objNetworkInfo == null || !v_objNetworkInfo.isConnected() ){
                this.g_objCallBack.updateFromDownload(new DownloadTaskResult(new Exception("You are Offline")));
                cancel(true);
            }
        }
    }

    @Override
    protected DownloadTaskResult doInBackground(String... prm_arrUrls) {
        DownloadTaskResult v_Return = null;
        String v_sUrlString;
        InputStream v_objInputStream = null;
        try{
            if (!isCancelled() && prm_arrUrls != null && prm_arrUrls.length > 0) {
                v_sUrlString = prm_arrUrls[0];
                URL url = new URL(v_sUrlString);
                v_objInputStream = downloadUrl(url);
                if (v_objInputStream != null) {
                    v_Return = new DownloadTaskResult(v_objInputStream);
                } else {
                    v_Return = new DownloadTaskResult(new IOException("No response received."));
                }
            }
        } catch(Exception v_exException){
            v_Return = new DownloadTaskResult(v_exException);
            v_exException.printStackTrace();
        }
        return v_Return;
    }

    /**
     * Given a URL, sets up a connection and gets the HTTP response body from the server.
     * If the network request is successful, it returns the response body in String form. Otherwise,
     * it will throw an IOException.
     */
    private InputStream downloadUrl(URL url) throws IOException {
        InputStream v_objInputStream = null;
        HttpsURLConnection connection = null;
        String result = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
            // Timeout for reading InputStream arbitrarily set to 3000ms.
            connection.setReadTimeout(3000);
            // Timeout for connection.connect() arbitrarily set to 3000ms.
            connection.setConnectTimeout(3000);
            // For this use case, set HTTP method to GET.
            connection.setRequestMethod("GET");
            // Already true by default but setting just in case; needs to be true since this request
            // is carrying an input (response) body.
            connection.setDoInput(true);
            // Open communications link (network traffic occurs here).
            connection.connect();
            publishProgress(DownloadCallback.Progress.CONNECT_SUCCESS);
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            // Retrieve the response body as an InputStream.
            v_objInputStream = connection.getInputStream();
            publishProgress(DownloadCallback.Progress.GET_INPUT_STREAM_SUCCESS, 0);
        } finally {
            // Close Stream and disconnect HTTPS connection.
            if (connection != null) {
                connection.disconnect();
            }
        }
        return v_objInputStream;
    }

    @Override
    protected void onPostExecute(DownloadTaskResult prm_objDownloadTaskResult) {
        if (prm_objDownloadTaskResult != null && this.g_objCallBack != null){
            this.g_objCallBack.updateFromDownload(prm_objDownloadTaskResult);
            this.g_objCallBack.finishDownloading();
        }
    }
}
