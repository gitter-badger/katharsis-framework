package io.katharsis.errorhandling;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponseBuilder {
    private Iterable<ErrorData> data;
    private int status;

    public ErrorResponseBuilder setErrorData(Iterable<ErrorData> errorObjects) {
        this.data = errorObjects;
        return this;
    }
    public ErrorResponseBuilder setSingleErrorData(ErrorData errorData) {
        List<ErrorData> errorDatas = new ArrayList<>();
        errorDatas.add(errorData);
        this.data = errorDatas;
        return this;
    }

    public ErrorResponseBuilder setStatus(int status) {
        this.status = status;
        return this;
    }

    public ErrorResponse build() {
        return new ErrorResponse(data, status);
    }
}