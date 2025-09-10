package cn.handsome.core.http;

/**
 * HttpClient Filter
 *
 * @author shay
 * @date 2021/3/18
 */
public interface HttpClientFilter {
    /**
     * 开始请求之前
     *
     * @param request request
     */
    void before(HttpRequest request);

    /**
     * 请求完成时
     *
     * @param response response
     * @param request  request
     */
    void after(HttpResponse response, HttpRequest request);
}
