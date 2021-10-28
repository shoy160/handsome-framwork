package cn.handsome.web.filter;

import cn.handsome.core.utils.CommonUtils;
import cn.handsome.web.config.BaseProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 跨域过滤器
 *
 * @author shay
 * @date 2020/7/24
 */
@Deprecated
@RequiredArgsConstructor
public class CorsFilter implements Filter {

    private final BaseProperties properties;

    private final static String STR_ANY = "*";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        if (properties.isEnableCors() && servletRequest instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String origin = properties.getCorsOrigin();
            if (STR_ANY.equals(origin)) {
                origin = request.getHeader("Origin");
            }
            String headers = properties.getCorsHeaders();
            String requestHeaders = request.getHeader("Access-Control-Request-Headers");
            if (STR_ANY.equals(headers) && !CommonUtils.isEmpty(requestHeaders)) {
                headers = requestHeaders;
            }
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.addHeader("Access-Control-Allow-Credentials", "true");
            response.addHeader("Access-Control-Allow-Origin", origin);
            response.addHeader("Access-Control-Allow-Methods", properties.getCorsMethods());
            response.addHeader("Access-Control-Allow-Headers", headers);
            if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
                response.getWriter().println("ok");
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
