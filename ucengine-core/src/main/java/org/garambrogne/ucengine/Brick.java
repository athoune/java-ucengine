/**
 * 
 */
package org.garambrogne.ucengine;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.tapestry5.json.JSONObject;
import org.garambrogne.ucengine.event.Eventualy;
import org.garambrogne.ucengine.rpc.HttpMethod;

/**
 * @author mlecarme
 * Distant plugin
 */
public abstract class Brick extends Eventualy {
	
	private String uid;

	public Brick connect(UCEngine engine, String uid, String credential) throws ClientProtocolException, IOException, URISyntaxException {
		this.engine = engine;
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("name", uid));
		formparams.add(new BasicNameValuePair("credential", credential));
//		JSONObject result = super.rawconnect(formparams);
//		this.uid = result.getString("uid");
//		this.sid = result.getString("sid");
		return this;
	}
	
	public void suscribe(String location, int start) {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("uid", this.uid));
		qparams.add(new BasicNameValuePair("sid", this.sid));
		qparams.add(new BasicNameValuePair("_async", "lp"));
		//this.startLoop(engine.buildRequest(HttpMethod.GET, "/event", qparams, null));
	}

	public void suscribe(String location) {
		suscribe(location, 0);
	}
}
