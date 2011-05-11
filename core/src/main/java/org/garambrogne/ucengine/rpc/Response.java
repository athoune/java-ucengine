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
	private JSONObject values;
	/**
	 * @param status
	 * @param values
	 */
	public Response(int status, JSONObject values) {
		super();
		this.status = status;
		this.values = values;
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
		return values;
	}
	public boolean isError() {
		return values.has("error");
	}
}
