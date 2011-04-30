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
import org.apache.http.params.HttpParams;
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

	public void executeAsync(HttpMethod method, final String path,
			final FutureCallback<Response> future, HttpParams params, Map<String, Object> body) throws InterruptedException {
		asyncHttpclient.execute(buildRequest(method, path, params),
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
	
	protected HttpRequestBase buildRequest(HttpMethod method, String path, HttpParams params) {
		StringBuilder url = new StringBuilder(this.ucengineUrl);
		url.append(VERSION).append(path);
		HttpRequestBase base = null;
		if(method.equals(HttpMethod.GET)) {
			base = new HttpGet(url.toString());
		}
		if(method.equals(HttpMethod.POST)) {
			base = new HttpPost(url.toString());
		}
		if(params != null) {
			base.setParams(params);
		}
		return base;
	}
	
	private Response buildResponse(HttpResponse response) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream(new Long(response.getEntity().getContentLength()).intValue());
		response.getEntity().writeTo(buffer);
		JSONObject resp = new JSONObject(buffer.toString("UTF8"));
		return new Response(response.getStatusLine().getStatusCode(), resp);
	}
	
	public Response execute(HttpMethod method, String path, HttpParams params, List<NameValuePair> formparams) throws ClientProtocolException, IOException {
		HttpRequestBase request = buildRequest(method, path, params);
		if(formparams != null) {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
			((HttpPost)request).setEntity(entity);
		}
		return execute(request);
	}
	
	public Response execute(HttpRequestBase request) throws ClientProtocolException, IOException {
		HttpResponse response = httpclient.execute(request);
		return buildResponse(response);
	}

	public void shutdown() throws InterruptedException {
		asyncHttpclient.shutdown();
	}

}
