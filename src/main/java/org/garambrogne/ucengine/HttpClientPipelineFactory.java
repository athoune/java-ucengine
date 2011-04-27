

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
package org.garambrogne.ucengine;
  
  import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.http.HttpClientCodec;
import org.jboss.netty.handler.codec.http.HttpContentDecompressor;
  
/**
30   * @author <a href="http://www.jboss.org/netty/">The Netty Project</a>
31   * @author Andy Taylor (andy.taylor@jboss.org)
32   * @author <a href="http://gleamynode.net/">Trustin Lee</a>
33   *
34   * @version $Rev: 2226 $, $Date: 2010-03-31 11:26:51 +0900 (Wed, 31 Mar 2010) $
35   */
  public class HttpClientPipelineFactory implements ChannelPipelineFactory {
  
      private final boolean ssl;
  
      public HttpClientPipelineFactory(boolean ssl) {
          this.ssl = ssl;
      }
  
      public ChannelPipeline getPipeline() throws Exception {
          // Create a default pipeline implementation.
          ChannelPipeline pipeline = pipeline();
  
          // Enable HTTPS if necessary.
          if (ssl) {
//              SSLEngine engine =
//                  SecureChatSslContextFactory.getClientContext().createSSLEngine();
//              engine.setUseClientMode(true);
  
//              pipeline.addLast("ssl", new SslHandler(engine));
          }
  
          pipeline.addLast("codec", new HttpClientCodec());
  
          // Remove the following line if you don't want automatic content decompression.
          pipeline.addLast("inflater", new HttpContentDecompressor());
  
          // Uncomment the following line if you don't want to handle HttpChunks.
          //pipeline.addLast("aggregator", new HttpChunkAggregator(1048576));
  
          pipeline.addLast("handler", new HttpResponseHandler());
          return pipeline;
      }
  }