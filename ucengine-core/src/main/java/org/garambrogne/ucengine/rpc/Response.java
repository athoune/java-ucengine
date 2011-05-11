/**
 * 
 */
package org.garambrogne.ucengine.rpc;

import java.io.UnsupportedEncodingException;

import org.apache.tapestry5.json.JSONObject;

/**
 * @author mlecarme
 * 
 * RPC response
 *
 */
public class Response {
	private int status;
	private JSONObject values = null;
	private byte[] raw;
	/**
	 * @param status
	 * @param values
	 */
	public Response(int status, byte[] raw) {
		super();
		this.status = status;
		this.raw = raw;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @return the values
	 */
	public JSONObject getValues() {
		if(values == null) {
			try {
				values = new JSONObject(new String(raw, "UTF8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return values;
	}
	public boolean isError() {
		return getValues().has("error");
	}
}
