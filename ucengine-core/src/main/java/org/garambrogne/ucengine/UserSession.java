/**
 * 
 */
package org.garambrogne.ucengine;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;

/**
 * @author mlecarme
 *
 */
public class UserSession extends Session{

	protected UserSession(UCEngine engine, String uid, String sid) {
		super(engine, uid, sid);
	}
	public void presence() throws HttpException  {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("uid", this.uid));
		qparams.add(new BasicNameValuePair("sid", this.sid));
		qparams.add(new BasicNameValuePair("_async", "lp"));
		try {
			this.startLoop(new HttpGet(this.engine.uri("/event", qparams)));
		} catch (URISyntaxException e) {
			throw new HttpException(e);
		}
	}

}
