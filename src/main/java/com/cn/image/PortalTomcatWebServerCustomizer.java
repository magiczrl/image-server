package com.cn.image;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

/**
 * @Desc
 * @author magiczrl@foxmail.com
 * @date 2019/8/9 16:26
 */
@Component
public class PortalTomcatWebServerCustomizer
        implements WebServerFactoryCustomizer<WebServerFactory> {

    @Override
    public void customize(WebServerFactory container) {
        TomcatServletWebServerFactory containerFactory = (TomcatServletWebServerFactory) container;
        containerFactory.setProtocol("org.apache.coyote.http11.Http11NioProtocol");
        containerFactory.addConnectorCustomizers((Connector connector) -> {
            /**
             * 性能优化参数调优
             */
            Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
            // KeepAliveTimeout 默认10，调整为30
            protocol.setKeepAliveTimeout(30000);
            // MaxKeepAliveRequests 默认100，调整为8192*2
            protocol.setMaxKeepAliveRequests(16384);
            //设置最大连接数  
            protocol.setMaxConnections(2000);
            protocol.setConnectionTimeout(8000);
            //设置最大线程数  
            protocol.setMaxThreads(800);
        });
    }
}
