package com.netty.serialize.handler;

import com.netty.serialize.message.Message;
import com.netty.serialize.message.MsgHeader;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 用于测试服务端实现的
 * Created by sdc on 2017/8/29.
 */
public class ServerHandler extends ChannelHandlerAdapter{

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
//        System.out.println("active");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
//        ctx.close();
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message newMsg = (Message)msg;
//        String msgStrClient = (String)msg;
        System.out.println("获取客户端里的内容:" + newMsg);

        Message message = new Message();
        String msgStr = "客户端接受到通知";
        MsgHeader header = new MsgHeader();
        header.setStartTag(new Byte("0"));
        header.setCmdCode("1234".getBytes());
        header.setLength(msgStr.length());
        header.setVersion("11".getBytes());

        message.setBody(msgStr);
        message.setHeader(header);

        ctx.writeAndFlush(message);
    }

}
