/**
 * 
 */
package org.garambrogne.ucengine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.tapestry5.json.JSONObject;
import org.garambrogne.ucengine.event.Eventualy;
import org.garambrogne.ucengine.rpc.HttpMethod;
import org.garambrogne.ucengine.rpc.Response;


/**
 * @author mlecarme
 *
 */
public class User extends Eventualy {
	private String name, uid, sid;
	/**
	 * @param uid
	 */
	public User(String name) {
		super();
		this.name = name;
	}
	public void presence(UCEngine engine, String credential) throws ClientProtocolException, IOException {
		presenceWithoutEvents(engine, credential);
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("uid", this.uid));
		qparams.add(new BasicNameValuePair("sid", this.sid));
		qparams.add(new BasicNameValuePair("_async", "lp"));
		this.startLoop(engine.buildRequest(HttpMethod.GET, "/event", qparams, null));
	}
	
	public void presenceWithoutEvents(UCEngine engine, String credential) throws ClientProtocolException, IOException {
		this.engine = engine;
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("name", this.name));
		formparams.add(new BasicNameValuePair("credential", credential));
		formparams.add(new BasicNameValuePair("metadata[nickname]", this.name));
		Response response = engine.execute(HttpMethod.POST, "/presence/", null, formparams);
		JSONObject result = response.getValues().getJSONObject("result");
		this.uid = result.getString("uid");
		this.sid = result.getString("sid");
		
	}
	
	public JSONObject infos() throws ClientProtocolException, IOException {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("uid", this.uid));
		qparams.add(new BasicNameValuePair("sid", this.sid));
		Response response = engine.execute(HttpMethod.GET, "/infos", qparams, null);
		return response.getValues().getJSONObject("result");
	}
	
}
