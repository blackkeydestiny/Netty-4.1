package io.netty.nettyBook2Example.chapterVII_MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * MassgePack编码器：MsgpackEncoder继承自MessageToByteEncoder，负责将Object类型的POJO对象编码为byte数组，然后写入到ByteBuf中;
 */
public class MsgpackEncoder extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        MessagePack msgpack = new MessagePack();

        //serialize
        byte[] raw = msgpack.write(msg);
        out.writeBytes(raw);
    }
}
