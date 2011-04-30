/**
 * 
 */
package org.garambrogne.ucengine;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.nio.concurrent.FutureCallback;
import org.apache.http.nio.reactor.IOReactorException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author mlecarme
 *
 */
public class WebClientTest {

	@Test
	public void time() throws URISyntaxException, IOReactorException, MalformedURLException, InterruptedException {
		UCEngine client = new UCEngine("http://demo.ucengine.org");
		final CountDownLatch latch = new CountDownLatch(2);
		client.executeAsync(HttpMethod.GET, "/time", new FutureCallback<Response>() {
			public void failed(Exception e) {
				e.printStackTrace();
				assertTrue(false);
				latch.countDown();
			}
			public void completed(Response response) {
				latch.countDown();
				assertFalse(response.isError());
				assertTrue(200 == response.getStatus());
				System.out.println(response.getValues());
			}
			public void cancelled() {
				assertTrue(false);
				latch.countDown();
			}
		}, null, null);
		client.executeAsync(HttpMethod.GET, "/infos", new FutureCallback<Response>() {
			public void failed(Exception e) {
				assertTrue(false);
				e.printStackTrace();
				latch.countDown();
			}
			public void completed(Response response) {
				latch.countDown();
				assertTrue(response.isError());
				assertTrue(400 == response.getStatus());
				System.out.println(response.getValues());
			}
			public void cancelled() {
				assertTrue(false);
				latch.countDown();
			}
		}, null, null);
		latch.await();
		client.shutdown();
	}
	
	@Test
	public void demo() throws ClientProtocolException, IOException {
		UCEngine engine = new UCEngine("http://demo.ucengine.org");
		User demo = new User("victor.goya@af83.com");
		demo.presence(engine, "pwd");	
	}
}
