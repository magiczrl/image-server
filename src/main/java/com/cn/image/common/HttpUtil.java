package com.cn.image.common;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import javax.net.ssl.SSLContext;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Desc 
 * @author Z
 * @date 2020年9月14日 下午3:55:23
 */
public class HttpUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static int DEFAULT_CON_TIMEOUT;
    private static int DEFAULT_READ_TIMEOUT;
    @Value("${http.default.con.timeout:1000}")
    private int defaultConTimeout;
    @Value("${http.default.read.timeout:5000}")
    private int defaultReadTimeout;

    static ConnectionKeepAliveStrategy myStrategy = new ConnectionKeepAliveStrategy() {
        public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
            HeaderElementIterator it = new BasicHeaderElementIterator(
                    response.headerIterator(HTTP.CONN_KEEP_ALIVE));
            while (it.hasNext()) {
                HeaderElement he = it.nextElement();
                String param = he.getName();
                String value = he.getValue();
                if (value != null && "timeout".equalsIgnoreCase(param)) {
                    try {
                        return Long.parseLong(value) * 1000;
                    } catch (NumberFormatException ignore) {
                    }
                }
            }
            // HttpHost target = (HttpHost) context.getAttribute( 
            //      HttpClientContext.HTTP_TARGET_HOST); 
            // return KEEP_ALIVE_TIME_OUT * 1000; //服务端未指定长连接时间按20s（默认-1永久）处理
            return 5 * 1000;
        }
    };
    private static CloseableHttpClient httpclient = null;
    private static PoolingHttpClientConnectionManager cm = null;
    static {
        // 连接池设置
        cm = new PoolingHttpClientConnectionManager(getDefaultRegistry());
        cm.setMaxTotal(512); // 最多512个连接
        cm.setDefaultMaxPerRoute(64); // 每个路由64个连接
        // 创建client对象
        httpclient = HttpClients.custom().setConnectionManager(cm).setKeepAliveStrategy(myStrategy)
                .disableAutomaticRetries().disableRedirectHandling().disableCookieManagement()
                .disableConnectionState().build();
    }

    private static Registry<ConnectionSocketFactory> getDefaultRegistry() {
        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                    return true;
                }
            }).build();
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
                NoopHostnameVerifier.INSTANCE);
        return RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslsf).build();
    }

    /**
    *
    * @param url
    * @param jsonData
    * @return
    * @throws Exception
    */
    public static String sendHttpsPostRequest(String url, Map<String, String> header,
                                              String jsonData)
            throws Exception {
        try {
            HttpPost httpPost = new HttpPost();
            RequestConfig config = RequestConfig.custom().setConnectTimeout(DEFAULT_CON_TIMEOUT)
                    .setSocketTimeout(DEFAULT_READ_TIMEOUT).build();
            httpPost.setConfig(config);
            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
            if (header != null) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            StringEntity entity = new StringEntity(jsonData, Charset.forName("UTF-8"));
            httpPost.setURI(new URI(url));
            httpPost.setEntity(entity);
            // 发起请求
            CloseableHttpResponse response = httpclient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            String result = EntityUtils.toString(responseEntity);
            EntityUtils.consume(responseEntity);
            return result;
        } catch (IOException e) {
            logger.info("HttpUtil sendPost exception!", e);
            throw new RuntimeException("HttpUtil sendPost exception!", e);
        }
    }

    /**
     * 保存图片
     * @param url
     * @param header
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String postHttpsBinary(String url, Map<String, String> header, byte[] bytes)
            throws Exception {
        try {
            HttpPost httpPost = new HttpPost();
            RequestConfig config = RequestConfig.custom().setConnectTimeout(DEFAULT_CON_TIMEOUT)
                    .setSocketTimeout(DEFAULT_READ_TIMEOUT).build();
            httpPost.setConfig(config);
            if (header != null) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            httpPost.setURI(new URI(url));
            httpPost.setEntity(new ByteArrayEntity(bytes));
            CloseableHttpResponse response = httpclient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            String result = EntityUtils.toString(responseEntity);
            EntityUtils.consume(responseEntity);
            return result;
        } catch (IOException e) {
            logger.info("postHttpsBinary exception!", e);
            throw new RuntimeException("postHttpsBinary exception!", e);
        }
    }

    /**
     * 获取图片流
     *   zimg图片服务器需开启长连接：max_keepalives  = 0
     * @param url
     * @param header
     * @param bytes
     * @return
     * @throws Exception
     */
    public static byte[] getImageStream(String url, Map<String, String> header) throws Exception {
        CloseableHttpResponse response = null;
        //CloseableHttpClient httpClient = null;
        try {
            HttpGet httpGet = new HttpGet();
            RequestConfig config = RequestConfig.custom().setConnectTimeout(DEFAULT_CON_TIMEOUT)
                    .setSocketTimeout(DEFAULT_READ_TIMEOUT).build();
            httpGet.setConfig(config);
            if (header != null) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }
            httpGet.setURI(new URI(url));
            response = httpclient.execute(httpGet);
            HttpEntity responseEntity = response.getEntity();
            return EntityUtils.toByteArray(responseEntity);
        } catch (IOException e) {
            logger.info("getImageStream exception!", e);
            throw new RuntimeException("getImageStream exception!", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
    }
    
}
