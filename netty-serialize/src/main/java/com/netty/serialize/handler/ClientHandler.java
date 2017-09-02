package com.netty.serialize.handler;

import com.netty.serialize.message.Message;
import com.netty.serialize.message.MsgHeader;
import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by sdc on 2017/8/29.
 */
public class ClientHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Message message = new Message();
        String msgStr = "我想发送一条消息";
        MsgHeader header = new MsgHeader();
        header.setStartTag(new Byte("0"));
        header.setCmdCode("1234".getBytes());
        header.setLength(msgStr.length());
        header.setVersion("11".getBytes());

        message.setBody(msgStr);
        message.setHeader(header);
        ctx.writeAndFlush(message).addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    // do sth
                    System.out.println("成功消息");
                } else {
                    // do sth
                    System.out.println("失败消息");
                }
            }
        });
//        ctx.writeAndFlush(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        ctx.close();
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            Message newMsg = (Message) msg;
            System.out.println("收到服务端的内容" + newMsg);
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

}
