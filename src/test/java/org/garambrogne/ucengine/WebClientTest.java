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

/**
 * @author mlecarme
 *
 */
public class WebClientTest {

	@Test
	public void time() throws URISyntaxException, IOReactorException, MalformedURLException, InterruptedException {
		WebClient client = new WebClient("http://demo.ucengine.org");
		final CountDownLatch latch = new CountDownLatch(2);
		client.execute(null, "/time", new FutureCallback<Response>() {
			public void failed(Exception arg0) {
				System.out.println(arg0);
				latch.countDown();
			}
			public void completed(Response response) {
				latch.countDown();
				System.out.println(response.getValues());
			}
			public void cancelled() {
				latch.countDown();
			}
		});
		client.execute(null, "/infos", new FutureCallback<Response>() {
			public void failed(Exception arg0) {
				System.out.println(arg0);
				latch.countDown();
			}
			public void completed(Response response) {
				latch.countDown();
				System.out.println(response.getValues());
			}
			public void cancelled() {
				latch.countDown();
			}
		});
		latch.await();
	}
}
