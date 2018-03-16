package com.feixue.mbridge.proxy.http.network;

import com.feixue.mbridge.proxy.Channel;
import com.feixue.mbridge.proxy.Invocation;
import com.feixue.mbridge.proxy.Invoker;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import org.apache.commons.lang3.StringUtils;

public class HttpChannel implements Channel {

    /**
     * mock client
     */
    public static String CLIENT_MODEL = "mockClient";

    /**
     * proxy
     */
    public static String PROXY_MODEL = "proxy";

    /**
     * mock server
     */
    public static String SERVER_MODEL = "mockServer";

    //当前模式
    private String nowModel;

    //关联端口
    private int port;

    public HttpChannel(String nowModel, int port) throws InterruptedException {
        this.nowModel = nowModel;
        this.port = port;

        if (StringUtils.isEmpty(nowModel)) {

        } else if (nowModel.equals(SERVER_MODEL)) {
            initServer();
        }
    }

    private void initServer() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new HttpServerCodec());
                        ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(Integer.MAX_VALUE));
                        ch.pipeline().addLast(new HttpServerExpectContinueHandler());
                        ch.pipeline().addLast("server-channel", new HttpServerChannelHandler());
                    }
                });
        bootstrap.bind(port).sync().channel().closeFuture().sync();
    }

    @Override
    public void inbound(Invoker invoker, Invocation invocation) {
    }

    @Override
    public void outbound(Invoker invoker, Invocation invocation) {
        //no thing to do!
    }
}
