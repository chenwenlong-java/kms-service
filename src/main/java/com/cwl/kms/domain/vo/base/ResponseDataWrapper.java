package com.cwl.kms.domain.vo.base;

import lombok.Data;

/**
 * ClassName: ResponseDataWrapper
 * Package: com.cwl.kms.domain.vo.base
 * Description:
 *
 * @Author chenwenlong
 * @Create 2023/9/15 21:42
 * @Version 1.0
 */
@Data
public class ResponseDataWrapper<T> {
    private String message;
    private long code = 0L;
    private T data;

    public ResponseDataWrapper(String message) {
        this.message = message;
    }

    public ResponseDataWrapper(T data) {
        this.data = data;
    }

    public static <T> ResponseDataWrapper<T> success(T data) {
        return new ResponseDataWrapper<>(data);
    }

    public static <T> ResponseDataWrapper<T> failure(String message) {
        return new ResponseDataWrapper<>(message);
    }
}
