package com.lu.schoolproject.file;

import cn.dev33.satoken.util.SaResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@Order(-1) // 保证在其他 Filter 前执行
public class SaTokenExceptionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.setContentType("application/json;charset=UTF-8");
            resp.setStatus(500);
            log.error(e.getMessage(),e);
            resp.getWriter().write(SaResult.error("服务器异常：" + e.getMessage()).setCode(500).toString());
        }
    }
}
