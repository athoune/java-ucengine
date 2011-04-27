package org.garambrogne.ucengine;

/*
 2    * Copyright 2009 Red Hat, Inc.
 3    *
 4    * Red Hat licenses this file to you under the Apache License, version 2.0
 5    * (the "License"); you may not use this file except in compliance with the
 6    * License.  You may obtain a copy of the License at:
 7    *
 8    *    http://www.apache.org/licenses/LICENSE-2.0
 9    *
 10   * Unless required by applicable law or agreed to in writing, software
 11   * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 12   * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 13   * License for the specific language governing permissions and limitations
 14   * under the License.
 15   */

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.util.CharsetUtil;

/**
 * @author <a href="http://www.jboss.org/netty/">The Netty Project</a> 28 * @author
 * Andy Taylor (andy.taylor@jboss.org) 29 * @author <a
 * href="http://gleamynode.net/">Trustin Lee</a> 30 * 31 * @version $Rev: 2189
 * $, $Date: 2010-02-19 18:02:57 +0900 (Fri, 19 Feb 2010) $ 32
 */
public class HttpResponseHandler extends SimpleChannelUpstreamHandler {

	private boolean readingChunks;

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		if (!readingChunks) {
			HttpResponse response = (HttpResponse) e.getMessage();

			System.out.println("STATUS: " + response.getStatus());
			System.out.println("VERSION: " + response.getProtocolVersion());
			System.out.println();

			if (!response.getHeaderNames().isEmpty()) {
				for (String name : response.getHeaderNames()) {
					for (String value : response.getHeaders(name)) {
						System.out.println("HEADER: " + name + " = " + value);
					}
				}
				System.out.println();
			}

			if (response.getStatus().getCode() == 200 && response.isChunked()) {
				readingChunks = true;
				System.out.println("CHUNKED CONTENT {");
			} else {
				ChannelBuffer content = response.getContent();
				if (content.readable()) {
					System.out.println("CONTENT {");
					System.out.println(content.toString(CharsetUtil.UTF_8));
					System.out.println("} END OF CONTENT");
				}
			}
		} else {
			HttpChunk chunk = (HttpChunk) e.getMessage();
			if (chunk.isLast()) {
				readingChunks = false;
				System.out.println("} END OF CHUNKED CONTENT");
			} else {
				System.out
						.print(chunk.getContent().toString(CharsetUtil.UTF_8));
				System.out.flush();
			}
		}
	}
}