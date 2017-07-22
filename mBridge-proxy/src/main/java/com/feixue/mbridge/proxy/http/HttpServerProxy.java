package com.feixue.mbridge.proxy.http;

import com.feixue.mbridge.proxy.CallbackNotifyChain;
import com.feixue.mbridge.proxy.ServerProxy;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zxxiao on 2017/7/12.
 */
public class HttpServerProxy<HttpProxyContent, BusinessWrapper> implements ServerProxy<HttpProxyContent, BusinessWrapper> {

    private static final Logger logger = LoggerFactory.getLogger(HttpServerProxy.class);

    private CallbackNotifyChain notifyChain;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    @Override
    public void close() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    @Override
    public void bind(int port, final CallbackNotifyChain<HttpProxyContent, BusinessWrapper> callbackNotifyChain) throws Exception {
        this.notifyChain = callbackNotifyChain;
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HttpServerCodec());
                            ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(Integer.MAX_VALUE));
                            ch.pipeline().addLast(new HttpServerExpectContinueHandler());
                            ch.pipeline().addLast("server-channel", new HttpServerChannelHandler(notifyChain));
                        }
                    });
            bootstrap.bind(port).sync().channel().closeFuture().sync();
        } finally {
            close();
        }
    }

    @Override
    public CallbackNotifyChain<HttpProxyContent, BusinessWrapper> getNotifyChain() {
        return notifyChain;
    }
}
