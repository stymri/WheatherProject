package rai.satyam.wheatherproject.util.http;

import java.io.InputStream;

public class DownloadTaskResult {
    private InputStream response;
    private Exception exception;

    public DownloadTaskResult(InputStream response) {
        this.response = response;
    }

    public DownloadTaskResult(Exception exception) {
        this.exception = exception;
    }

    public InputStream getResponse() {
        return response;
    }

    public void setResponse(InputStream response) {
        this.response = response;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
