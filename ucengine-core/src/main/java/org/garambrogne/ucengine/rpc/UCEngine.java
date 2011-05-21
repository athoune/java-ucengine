/**
 * 
 */
package org.garambrogne.ucengine.rpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.tapestry5.json.JSONObject;
import org.garambrogne.ucengine.event.Connectable;

/**
 * @author mlecarme
 * 
 */
public class UCEngine {
	private String host, protocol;
	private int port;
	private HttpClient httpclient;
	public static final String VERSION = "/api/0.5";
	private final ExecutorService pool;

	public UCEngine(String zurl) throws MalformedURLException {
		URL url = new URL(zurl);
		this.host = url.getHost();
		this.port = url.getPort();
		this.protocol = url.getProtocol();
		this.pool = Executors.newFixedThreadPool(15);
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(
		         new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		schemeRegistry.register(
		         new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(schemeRegistry);
		// Increase max total connection to 200
		cm.setMaxTotal(200);
		// Increase default max connection per route to 20
		cm.setDefaultMaxPerRoute(20);
		this.httpclient = new DefaultHttpClient(cm);
		((AbstractHttpClient) httpclient).setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {

		    public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
		        // Honor 'keep-alive' header
		        HeaderElementIterator it = new BasicHeaderElementIterator(
		                response.headerIterator(HTTP.CONN_KEEP_ALIVE));
		        while (it.hasNext()) {
		            HeaderElement he = it.nextElement();
		            String param = he.getName(); 
		            String value = he.getValue();
		            if (value != null && param.equalsIgnoreCase("timeout")) {
		                try {
		                    return Long.parseLong(value) * 1000;
		                } catch(NumberFormatException ignore) {
		                }
		            }
		        }
		        HttpHost target = (HttpHost) context.getAttribute(
		                ExecutionContext.HTTP_TARGET_HOST);
		        if ("www.naughty-server.com".equalsIgnoreCase(target.getHostName())) {
		            // Keep alive for 5 seconds only
		            return 5 * 1000;
		        } else {
		            // otherwise keep alive for 30 seconds
		            return 30 * 1000;
		        }
		    }
		    
		});
	}

	public static List<NameValuePair> buildArguments(String uid, String sid) {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("uid", uid));
		qparams.add(new BasicNameValuePair("sid", sid));
		return qparams;
	}
	
	private static Response buildResponse(HttpResponse response) throws UceException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream(new Long(response.getEntity().getContentLength()).intValue());
		try {
			response.getEntity().writeTo(buffer);
		} catch (IOException e) {
			throw new UceException();
		}
		Response uceResponse = new Response(response.getStatusLine().getStatusCode(), buffer.toByteArray()); 
		if(response.getStatusLine().getStatusCode() >= 300) {
			throw new UceException(uceResponse.getValues().getString("error"));
		}
		return uceResponse;
	}
	
	public URI uri(String path, String query) throws URISyntaxException {
		return URIUtils.createURI(this.protocol, this.host, this.port, VERSION + path, query, null);
	}
	
	public URI uri(String path) throws URISyntaxException {
		return URIUtils.createURI(this.protocol, this.host, this.port, VERSION + path, null, null);
	}
	
	public URI uri(String path, List<NameValuePair> qparams) throws URISyntaxException {
		return uri(path, URLEncodedUtils.format(qparams, "UTF-8"));
	}
		
	public Response get(String path, List<NameValuePair> qparams) throws HttpException, UceException {
		return get(path, URLEncodedUtils.format(qparams, "UTF-8"));
	}

	public Response get(String path, String query) throws HttpException, UceException {
		try {
			return execute(new HttpGet(uri(path, query)));
		} catch (URISyntaxException e) {
			throw new HttpException(e);
		}
	}

	public Response get(String path) throws HttpException, UceException {
		try {
			return execute(new HttpGet(uri(path)));
		} catch (URISyntaxException e) {
			throw new HttpException(e);
		}
	}

	public Response post(String path, List<NameValuePair> params) throws HttpException, UceException {
		HttpPost post;
		try {
			System.out.println(uri(path));
			post = new HttpPost(uri(path));
		} catch (URISyntaxException e) {
			throw new HttpException(e);
		}
		try {
			post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new HttpException(e);
		}
		return execute(post);
	}

	public Response execute(HttpRequestBase request) throws UceException, HttpException {
		HttpResponse response;
		try {
			response = httpclient.execute(request);
		} catch (ClientProtocolException e) {
			throw new HttpException(e);
		} catch (IOException e) {
			throw new HttpException(e);
		}
		return buildResponse(response);
	}

	/**
	 * @return the pool
	 */
	public ExecutorService getPool() {
		return pool;
	}

	public Session connection(Connectable connectable, String credential) throws HttpException, UceException {
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("name", connectable.getName()));
		formparams.add(new BasicNameValuePair("credential", credential));
		Map<String, Object> meta = connectable.metaDataOnConnection();
		if(meta != null) {
			for (Entry<String, Object> entry : meta.entrySet()) {
				formparams.add(new BasicNameValuePair(new StringBuffer("metadata[").append(entry.getKey()).append("]").toString(), entry.getValue().toString()));
			}
		}
		Response response = post("/presence/", formparams);
		JSONObject result = response.getValues().getJSONObject("result");
		connectable.setUid(result.getString("uid"));
		connectable.setSid(result.getString("sid"));
		Session session = new Session(this, result.getString("uid"), result.getString("sid"));
		session.getLoop().register(connectable);
		connectable.setSession(session);
		return session;
	}

	public Date time() throws HttpException, UceException {
		Response response = this.get("/time");
		return new Date(response.getValues().getLong("result"));
	}
}
