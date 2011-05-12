/**
 * 
 */
package org.garambrogne.ucengine;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.garambrogne.ucengine.elasticsearch.IndexingBrick;
import org.junit.Test;

/**
 * @author mlecarme
 *
 */
public class BrickTest {

	@Test
	public void index() throws ClientProtocolException, IOException {
		UCEngine engine = new UCEngine("http://demo.ucengine.org");
		IndexingBrick brick = new IndexingBrick();
		brick.connect(engine, "victor.goya@af83.com", "pwd");
		brick.suscribe();
	}
}
