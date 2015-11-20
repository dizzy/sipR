package org.sipr.core.sip.request.processor;

import javax.sip.header.Header;
import java.util.List;

public class RequestException extends Exception {
    Integer errorCode;
    List<? extends Header> errorHeaders;

    public RequestException(Integer code) {
        errorCode = code;
    }

    public RequestException(Integer code, List<? extends Header> headers) {
        errorCode = code;
        errorHeaders = headers;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public List<? extends Header> getHeaders() {
        return errorHeaders;
    }
}
