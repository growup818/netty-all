package com.netty.serialize.message;

import java.io.Serializable;

/**
 * Created by sdc on 2017/8/26.
 */
public class Message implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 4923081103118853877L;

    private MsgHeader header;

    private Object body;

    //检验和
//    private byte crcCode;

//    public byte getCrcCode() {
//        return crcCode;
//    }
//
//    public void setCrcCode(byte crcCode) {
//        this.crcCode = crcCode;
//    }

    public MsgHeader getHeader() {
        return header;
    }

    public void setHeader(MsgHeader header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Message{" +
                "header=" + header +
                ", body=" + body +
//                ", crcCode=" + crcCode +
                '}';
    }
}
