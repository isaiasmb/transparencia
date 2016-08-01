/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parseador.http;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;

/**
 *
 * @author bruno
 */
public class HttpContainer {
    private CloseableHttpClient httpClient;
    private HttpContext httpContext;
    
    public HttpContainer(CloseableHttpClient httpClient, HttpContext httpContext) {
        this.httpClient = httpClient;
        this.httpContext = httpContext;
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public HttpContext getHttpContext() {
        return httpContext;
    }

    public void setHttpContext(HttpContext httpContext) {
        this.httpContext = httpContext;
    }
    
    
}
