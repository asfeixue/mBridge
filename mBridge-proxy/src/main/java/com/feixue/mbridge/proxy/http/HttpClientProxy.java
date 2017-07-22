package com.feixue.mbridge.proxy.http;

import com.feixue.mbridge.proxy.ClientProxy;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by zxxiao on 2017/7/12.
 */
public class HttpClientProxy implements ClientProxy {

    private EventLoopGroup workerGroup;

    @Override
    public void connect(String host, int port) throws Exception {
        workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                        }
                    });
            bootstrap.connect(host, port).sync().channel().closeFuture().sync();
        } finally {
            close();
        }
    }

    @Override
    public void close() {
        workerGroup.shutdownGracefully();
    }
}
