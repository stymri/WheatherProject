package rai.satyam.wheatherproject.util.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

import rai.satyam.wheatherproject.util.Utils;

public class DownloadTaskResult {
    private int responseStatus = 0;
    private String responseString;
    private Exception exception;
    private Bitmap responseImage;

    public int getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }

    public Bitmap getResponseImage() {
        return responseImage;
    }

    public DownloadTaskResult() {
    }

    public DownloadTaskResult(Exception exception) {
        this.exception = exception;
    }

    public String getResponseString() {
        return responseString;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public void processAndSaveResponse(String prm_sContentType ,InputStream prm_objInputStream ){
        if (prm_sContentType.contains("image")){
            responseImage = BitmapFactory.decodeStream(prm_objInputStream);
            responseStatus = 2;
        } else {
            responseString = Utils.getStringFromInputStream(prm_objInputStream);
            responseStatus = 1;
        }
    }
}
