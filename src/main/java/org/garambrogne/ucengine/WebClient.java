/**
 * 
 */
package org.garambrogne.ucengine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.DefaultHttpAsyncClient;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.nio.concurrent.FutureCallback;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.tapestry5.json.JSONObject;
import org.jboss.netty.handler.codec.http.HttpMethod;

/**
 * @author mlecarme
 * 
 */
public class WebClient {
	private String ucengineUrl;
	private HttpAsyncClient httpclient;
	public static final String VERSION = "/api/0.5";

	public WebClient(String url) throws IOReactorException {
		this.ucengineUrl = url;
		this.httpclient = new DefaultHttpAsyncClient();
		this.httpclient.start();
	}

	public void execute(HttpMethod method, String path, FutureCallback<Response> future)
			throws InterruptedException {
		execute(method, path, future, null);
	}

	public void execute(HttpMethod method, final String path,
			final FutureCallback<Response> future, Map<String, Object> body) throws InterruptedException {
		StringBuilder url = new StringBuilder(this.ucengineUrl);
		url.append(VERSION).append(path);
		httpclient.execute(new HttpGet(url.toString()),
				new FutureCallback<HttpResponse>() {
					public void completed(final HttpResponse response) {
						ByteArrayOutputStream buffer = new ByteArrayOutputStream(new Long(response.getEntity().getContentLength()).intValue());
						try {
							response.getEntity().writeTo(buffer);
						} catch (IOException e) {
							future.failed(e);
						}
						try {
							JSONObject resp = new JSONObject(buffer.toString("UTF8"));
							future.completed(new Response(response.getStatusLine().getStatusCode(), resp));
						} catch (UnsupportedEncodingException e) {
							future.failed(e);
						}
					}
					public void failed(final Exception ex) {
						future.failed(ex);
					}
					public void cancelled() {
						future.cancelled();
					}
				});
	}

	public void shutdown() throws InterruptedException {
		httpclient.shutdown();
	}

}
