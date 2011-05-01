/**
 * 
 */
package org.garambrogne.ucengine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.tapestry5.json.JSONObject;


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
		this.engine = engine;
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("name", this.name));
		formparams.add(new BasicNameValuePair("credential", credential));
		formparams.add(new BasicNameValuePair("metadata[nickname]", this.name));
		Response response = engine.execute(HttpMethod.POST, "/presence/", null, formparams);
		System.out.println(response.getValues());
		JSONObject result = response.getValues().getJSONObject("result");
		this.uid = result.getString("uid");
		this.sid = result.getString("sid");
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("uid", this.uid));
		qparams.add(new BasicNameValuePair("sid", this.sid));
		qparams.add(new BasicNameValuePair("_async", "lp"));
		this.startLoop(engine.buildRequest(HttpMethod.GET, "/event", qparams, null));
	}
	
	public Map<String, Object> infos() {
		return null;
	}
	
}
