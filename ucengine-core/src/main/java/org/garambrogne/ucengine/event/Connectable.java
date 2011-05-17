/**
 * 
 */
package org.garambrogne.ucengine.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.garambrogne.ucengine.UCEngine;

/**
 * @author mlecarme
 * Abstract object that can be connected
 * @see Eventualy
 */
public abstract class Connectable {
	protected UCEngine engine;
	protected Log log = LogFactory.getLog(this.getClass());
	protected String uid, sid;

}
