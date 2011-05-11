/**
 * 
 */
package org.garambrogne.ucengine.rpc;

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
	private String raw;
	/**
	 * @param status
	 * @param values
	 */
	public Response(int status, String raw) {
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
			values = new JSONObject(raw);
		}
		return values;
	}
	public boolean isError() {
		return getValues().has("error");
	}
}
