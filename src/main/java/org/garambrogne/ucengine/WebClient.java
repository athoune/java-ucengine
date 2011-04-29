/**
 * 
 */
package org.garambrogne.ucengine;

import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.DefaultHttpAsyncClient;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.nio.concurrent.FutureCallback;
import org.apache.http.nio.reactor.IOReactorException;
import org.jboss.netty.handler.codec.http.HttpMethod;

/**
 * @author mlecarme
 * 
 */
public class WebClient {
	private String ucengineUrl;
	private HttpAsyncClient httpclient;

	public WebClient(String url) throws IOReactorException {
		this.ucengineUrl = url;
		this.httpclient = new DefaultHttpAsyncClient();
		this.httpclient.start();
	}

	public void execute(HttpMethod method, String path)
			throws InterruptedException {
		execute(method, path, null);
	}

	public void execute(HttpMethod method, final String path,
			Map<String, String> body) throws InterruptedException {
		StringBuilder url = new StringBuilder(this.ucengineUrl);
		url.append("/api/0.4").append(path);
		httpclient.execute(new HttpGet(url.toString()),
				new FutureCallback<HttpResponse>() {
					public void completed(final HttpResponse response) {
						System.out.println(path + "->"
								+ response.getStatusLine());
					}
					public void failed(final Exception ex) {
						ex.printStackTrace();
					}
					public void cancelled() {
					}

				});
	}

	public void shutdown() throws InterruptedException {
		httpclient.shutdown();
	}

}
