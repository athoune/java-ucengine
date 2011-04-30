/**
 * 
 */
package org.garambrogne.ucengine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.nio.client.DefaultHttpAsyncClient;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.nio.concurrent.FutureCallback;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.tapestry5.json.JSONObject;

/**
 * @author mlecarme
 * 
 */
public class UCEngine {
	private String ucengineUrl;
	private HttpAsyncClient asyncHttpclient;
	private HttpClient httpclient;
	public static final String VERSION = "/api/0.5";

	public UCEngine(String url) throws IOReactorException {
		this.ucengineUrl = url;
		this.httpclient = new DefaultHttpClient();
		this.asyncHttpclient= new DefaultHttpAsyncClient();
		this.asyncHttpclient.start();
	}

	public void executeAsync(HttpMethod method, String path, FutureCallback<Response> future)
			throws InterruptedException {
		executeAsync(method, path, future, null);
	}

	public void executeAsync(HttpMethod method, final String path,
			final FutureCallback<Response> future, Map<String, Object> body) throws InterruptedException {
		asyncHttpclient.execute(buildRequest(method, path),
				new FutureCallback<HttpResponse>() {
					public void completed(final HttpResponse response) {
						try {
							future.completed(buildResponse(response));
						} catch (IOException e) {
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
	
	private HttpRequestBase buildRequest(HttpMethod method, String path) {
		StringBuilder url = new StringBuilder(this.ucengineUrl);
		url.append(VERSION).append(path);
		if(method.equals(HttpMethod.GET)) {
			return new HttpGet(url.toString());
		}
		if(method.equals(HttpMethod.POST)) {
			return new HttpPost(url.toString());
		}
		return null;
	}
	
	private Response buildResponse(HttpResponse response) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream(new Long(response.getEntity().getContentLength()).intValue());
		response.getEntity().writeTo(buffer);
		JSONObject resp = new JSONObject(buffer.toString("UTF8"));
		return new Response(response.getStatusLine().getStatusCode(), resp);
	}
	
	public Response execute(HttpMethod method, String path, List<NameValuePair> formparams) throws ClientProtocolException, IOException {
		HttpRequestBase request = buildRequest(method, path);
		if(formparams != null) {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
			((HttpPost)request).setEntity(entity);
		}
		HttpResponse response = httpclient.execute(request);
		return buildResponse(response);
	}

	public void shutdown() throws InterruptedException {
		asyncHttpclient.shutdown();
	}

}
