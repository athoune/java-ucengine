/**
 * 
 */
package org.garambrogne.ucengine.rpc;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.apache.tapestry5.json.JSONObject;
import org.garambrogne.ucengine.event.EventLoop;

/**
 * @author mlecarme
 *
 */
public class Session {
	private String uid, sid;
	private UCEngine engine;
	private EventLoop loop;
	
	public EventLoop getLoop() {
		return loop;
	}
	protected Session(UCEngine engine, String uid, String sid) {
		this.engine = engine;
		this.uid = uid;
		this.sid = sid;
		this.loop = new EventLoop(engine);
	}
	public JSONObject infos() throws HttpException, UceException {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("uid", this.uid));
		qparams.add(new BasicNameValuePair("sid", this.sid));
		Response response = engine.get("/infos", qparams);
		System.out.println(response.getValues());
		return response.getValues().getJSONObject("result");
	}
	
	public void startLoop() throws HttpException {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("uid", this.uid));
		qparams.add(new BasicNameValuePair("sid", this.sid));
		qparams.add(new BasicNameValuePair("_async", "lp"));
		try {
			loop.startLoop(new HttpGet(engine.uri("/event", qparams)));
		} catch (URISyntaxException e) {
			throw new HttpException(e);
		}
	}

}
