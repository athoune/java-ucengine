/**
 * 
 */
package org.garambrogne.ucengine;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.garambrogne.ucengine.elasticsearch.Brick;
import org.junit.Test;

/**
 * @author mlecarme
 *
 */
public class BrickTest {

	@Test
	public void index() throws ClientProtocolException, IOException {
		UCEngine engine = new UCEngine("http://demo.ucengine.org");
		User demo = new User("victor.goya@af83.com");
		new Brick(demo);
		demo.presence(engine, "pwd");
	}
}
