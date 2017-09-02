package com.netty.serialize.coder;

import com.netty.serialize.message.Message;
import com.netty.serialize.message.MsgHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by sdc on 2017/8/26.
 */
public class MsgDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) throws Exception {
        try{
            //内部协议这块可以约定一下，超过多大的长度就不可以了。
//            if(in.readableBytes() < 12){
//                return;
//            }
            System.out.println("开始解码消息，消息长度:" + in.readableBytes());

            in.markReaderIndex();
            //设置一些消息的属性
            Message message = new Message();
            MsgHeader header = new MsgHeader();
            header.setStartTag(in.readByte());

            byte[] cmdCode = new byte[4];
            in.readBytes(cmdCode);
            header.setCmdCode(cmdCode);
            System.out.println(new String(cmdCode, "UTF-8"));

            //长度从byte[4]转int
            byte[] lengthBytes = new byte[4];
            in.readBytes(lengthBytes);
            int length = toInt(lengthBytes);
            System.out.println("header:" + length);
            header.setLength(length);
            if(length < 0 || length > 10240){//过长消息或不合法消息
                throw new IllegalArgumentException("wrong message length");
            }

            byte[] version = new byte[2];
            in.readBytes(version);
            header.setVersion(version);
            System.out.println("version:" + new String(version, "UTF-8"));

            if(header.getLength() > 0){
                System.out.println("bytebuffer可读的范围" + in.readableBytes());
                if(in.readableBytes() > length + 1){
                    in.resetReaderIndex();
                    System.out.println("返回了");
                    return;
                }
                //读取body里的内容
                byte[] bodyBytes = new byte[header.getLength()];
                in.readBytes(bodyBytes);
                message.setBody(new String(bodyBytes, "UTF-8"));
            }
            //crccode暂时去掉
//            message.setCrcCode(in.readByte());

            //设置头部
            message.setHeader(header);
            System.out.println("body:" + message.getBody());

            out.add(message);
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public static int toInt(byte[] bytes){
        int value = 0;
        for(int i=0; i<bytes.length; i++){
            int num = (int)Math.pow(10, bytes.length - 1 - i);
            value += num * bytes[i];
        }
        return value;
    }
}
