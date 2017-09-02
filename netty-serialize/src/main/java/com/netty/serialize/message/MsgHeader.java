package com.netty.serialize.message;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by sdc on 2017/8/26.
 */
public class MsgHeader implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 4923081103118853877L;

    //固定头
    private byte startTag;

    //命令码，4位
    private byte[] cmdCode;

    //版本 2位
    private byte[] version;

    private int length;

    public byte[] getVersion() {
        return version;
    }

    public void setVersion(byte[] version) {
        this.version = version;
    }

    public byte[] getCmdCode() {
        return cmdCode;
    }

    public void setCmdCode(byte[] cmdCode) {
        this.cmdCode = cmdCode;
    }

    public byte getStartTag() {
        return startTag;
    }

    public void setStartTag(byte startTag) {
        this.startTag = startTag;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "MsgHeader{" +
                "startTag=" + startTag +
                ", cmdCode=" + Arrays.toString(cmdCode) +
                ", version=" + Arrays.toString(version) +
                ", length=" + length +
                '}';
    }
}
