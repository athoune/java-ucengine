/**
 * 
 */
package org.garambrogne.ucengine.rpc;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.tapestry5.json.JSONObject;
import org.garambrogne.ucengine.event.EventLoop;
import org.garambrogne.ucengine.event.impl.ThreadEventLoop;

/**
 * @author mlecarme
 *
 */
public class Session {
	private String uid, sid;
	private UCEngine engine;
	private EventLoop loop;
	
	public String getUid() {
		return uid;
	}
	public String getSid() {
		return sid;
	}
	public EventLoop getLoop() {
		return loop;
	}
	protected Session(UCEngine engine, String uid, String sid) {
		this.engine = engine;
		this.uid = uid;
		this.sid = sid;
		this.loop = new ThreadEventLoop(this);
	}
	public JSONObject infos() throws HttpException, UceException {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("uid", this.uid));
		qparams.add(new BasicNameValuePair("sid", this.sid));
		Response response = engine.get("/infos", qparams);
		System.out.println(response.getValues());
		return response.getValues().getJSONObject("result");
	}
	
	public UCEngine getEngine() {
		return engine;
	}
	public void listenAllEvents() throws HttpException {
		loop.start("/event");
	}
	
	public Response GET(String path) throws HttpException, UceException {
		return GET(path, new Arguments());
	}
	
	public Response GET(String path, Arguments arguments) throws HttpException, UceException {
		arguments.add("uid", getUid());
		arguments.add("sid", getSid());
		return engine.get(path, arguments.values());
	}

}
