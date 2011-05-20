/**
 * 
 */
package org.garambrogne.ucengine;

import java.net.MalformedURLException;

import org.garambrogne.ucengine.elasticsearch.IndexingBrick;
import org.garambrogne.ucengine.rpc.HttpException;
import org.garambrogne.ucengine.rpc.Session;
import org.garambrogne.ucengine.rpc.UCEngine;
import org.garambrogne.ucengine.rpc.UceException;
import org.junit.Test;

/**
 * @author mlecarme
 *
 */
public class BrickTest {

	@Test
	public void index() throws HttpException, UceException, MalformedURLException {
		UCEngine engine = new UCEngine("http://demo.ucengine.org");
		IndexingBrick brick = new IndexingBrick("victor.goya@af83.com");
		Session session = engine.connection(brick, "pwd");
		brick.suscribe();
	}
}
