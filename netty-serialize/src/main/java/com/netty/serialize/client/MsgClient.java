package com.netty.serialize.client;

import com.netty.serialize.coder.MsgDecoder;
import com.netty.serialize.coder.MsgEncoder;
import com.netty.serialize.handler.ClientHandler;
import com.netty.serialize.handler.ServerHandler;
import com.netty.serialize.marshalling.MarshallingCodeCFactory;
import com.netty.serialize.message.Message;
import com.netty.serialize.message.MsgHeader;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by sdc on 2017/8/26.
 */
public class MsgClient {

    public void connect(String ip, int port) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

//        Message message = new Message();
//        String msgStr = "我想发送一条消息";
//        MsgHeader header = new MsgHeader();
//        header.setStartTag(new Byte("0"));
//        header.setCmdCode("1234".getBytes());
//        header.setLength(msgStr.length());
//        header.setVersion("11".getBytes());
//
//        message.setBody(msgStr);
//        message.setHeader(header);
        try {
            Bootstrap bs = new Bootstrap();
            bs.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)//
                    .handler(new ChildChannelHandler());

            ChannelFuture f = bs.connect(ip,port).sync();

            //写入消息
//            f.channel().writeAndFlush(message).sync();

            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public static class ChildChannelHandler extends ChannelInitializer {
        protected void initChannel(Channel channel) throws Exception {
            channel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
            channel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
            channel.pipeline().addLast(new ClientHandler());
        }
    }

    public static void main(String[] args){
        try {
            new MsgClient().connect("127.0.0.1", 8080);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
