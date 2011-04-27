/**
 * 
 */
package org.garambrogne.ucengine;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;

/**
 * @author mlecarme
 * 
 */
public class WebClient {
	private boolean ssl = false;
	private ExecutorService boss;
	private ExecutorService workers;
	private String host;
	private int port;

	public WebClient(URI uri) {
		this.boss = Executors.newCachedThreadPool();
		this.workers = Executors.newCachedThreadPool();
		String scheme = uri.getScheme() == null ? "http" : uri.getScheme();
		host = uri.getHost() == null ? "localhost" : uri.getHost();
		port = uri.getPort();
		if (port == -1) {
			if (scheme.equalsIgnoreCase("http")) {
				port = 80;
			} else if (scheme.equalsIgnoreCase("https")) {
				port = 443;
			}
		}
	}
	
	public void execute(HttpMethod method, URI uri) {
		// Configure the client.
		ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						this.boss,
						this.workers));
		// Set up the event pipeline factory.
		bootstrap.setPipelineFactory(new HttpClientPipelineFactory(ssl));

		// Start the connection attempt.
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(host,
				port));

		// Wait until the connection attempt succeeds or fails.
		Channel channel = future.awaitUninterruptibly().getChannel();
		if (!future.isSuccess()) {
			future.getCause().printStackTrace();
			bootstrap.releaseExternalResources();
			return;
		}

		// Prepare the HTTP request.
		HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1,
				method, uri.toASCIIString());
		request.setHeader(HttpHeaders.Names.HOST, host);
		request.setHeader(HttpHeaders.Names.CONNECTION,
				HttpHeaders.Values.CLOSE);
		request.setHeader(HttpHeaders.Names.ACCEPT_ENCODING,
				HttpHeaders.Values.GZIP);
		// Send the HTTP request.
		channel.write(request);

		// Wait for the server to close the connection.
		channel.getCloseFuture().awaitUninterruptibly();

		// Shut down executor threads to exit.
		bootstrap.releaseExternalResources();
		
	}
	
	public void get(URI uri) {
		execute(HttpMethod.GET, uri);
	}

}
