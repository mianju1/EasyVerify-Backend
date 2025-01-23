package cn.mianju.exception;

/**
 * 请求频繁 抛出异常接口
 * @author MianJu 2024/12/13
 * EasyVerify cn.mianju.exception
 */
public class RequestFrequencyException extends RuntimeException {
    public RequestFrequencyException(String message) {
        super(message);
    }

    public RequestFrequencyException() {
        super("请求频繁，稍后再试");
    }

}
