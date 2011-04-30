/**
 * 
 */
package org.garambrogne.ucengine;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

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
		});
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
		});
		latch.await();
		client.shutdown();
	}
}
