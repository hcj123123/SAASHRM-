package com.ihrm.common.feign;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Configuration
public class FeignConfiguration {


    @Bean
    public RequestInterceptor requestInterceptor()
    {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
               ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
                if(requestAttributes!=null)
                {
                    HttpServletRequest request = requestAttributes.getRequest();
                    Enumeration<String> headerNames = request.getHeaderNames();
                    if(headerNames!=null)
                    {
                        while(headerNames.hasMoreElements())
                        {
                            String name=headerNames.nextElement();
                            String value=request.getHeader(name);
                            requestTemplate.header(name,value);
                        }
                    }

                }

            }
        };
    }
}
