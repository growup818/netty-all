package com.netty.serialize.server;

import com.netty.serialize.coder.MsgDecoder;
import com.netty.serialize.coder.MsgEncoder;
import com.netty.serialize.handler.ServerHandler;
import com.netty.serialize.marshalling.MarshallingCodeCFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by sdc on 2017/8/26.
 */
public class MsgServer {

    public void bind(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
//                    .childHandler(new ChildChannelHandler())
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                            channel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                            channel.pipeline().addLast(new ServerHandler());
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture cf = sb.bind(port).sync();
            System.out.println("服务端已启动");

            cf.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static class ChildChannelHandler extends ChannelInitializer {

        protected void initChannel(Channel channel) throws Exception {
            channel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
            channel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
            channel.pipeline().addLast(new ServerHandler());
        }

    }

    public static void main(String[] args){
        try {
            new MsgServer().bind(8080);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
