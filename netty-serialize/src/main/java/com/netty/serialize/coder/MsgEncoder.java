package com.netty.serialize.coder;

import com.netty.serialize.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by sdc on 2017/8/26.
 */
public class MsgEncoder extends MessageToByteEncoder {

    public static byte getIndexToByte(int i, int index){
        if(index == 0){
            return (byte)(i % 10);
        }else{
            int num = (int)Math.pow(10, index);
            return (byte)((i / num) % 10);
        }
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf out) throws Exception {
        if (o instanceof Message) {
            try {
                Message msg = (Message)o;
                if (msg == null || msg.getHeader() == null) {
                    throw new Exception("The encode message is null");
                }

                out.writeByte(msg.getHeader().getStartTag());
                out.writeBytes(msg.getHeader().getCmdCode());

                //占位
                byte[] lengthBytes = new byte[]{0, 0, 0, 0};
                out.writeBytes(lengthBytes);

                out.writeBytes(msg.getHeader().getVersion());
                String body = (String) msg.getBody();
                int length = 0;
                if (body != null) {
                    byte[] bodyBytes = body.getBytes();
                    out.writeBytes(bodyBytes);
                    length = bodyBytes.length;

//                    if (Constants.CRCCODE_DEFAULT != msg.getCrcCode()) {
//                        msg.setCrcCode(CRC8.calcCrc8(bodyBytes));
//                    }
//                    msg.setCrcCode();
                }

                //长度从int转换为byte[4]
                byte l1 = getIndexToByte(length, 3);
                byte l2 = getIndexToByte(length, 2);
                byte l3 = getIndexToByte(length, 1);
                byte l4 = getIndexToByte(length, 0);
                lengthBytes = new byte[]{l1, l2, l3, l4};
                out.setBytes(5, lengthBytes);
                System.out.println("encoder:" + msg.getBody());
//                out.writeByte(msg.getCrcCode());
            }catch(Exception e){
                e.printStackTrace();
                throw e;
            }
        }
    }
}
