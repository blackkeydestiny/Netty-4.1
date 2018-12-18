package io.netty.nettyBook2Example.chapterVII_MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * MessagePack解码器：首先从数据包msg获取需要解码的byte数组，然后后调用MessagePack的read方法将其反序列化为Object对象，将解码后的对象加入到解码的对象列表out中;
 */
public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
      final byte[] array;
      final int length = msg.readableBytes();
      array = new byte[length];
      msg.getBytes(msg.readerIndex(), array, 0, length);
      MessagePack msgpack = new MessagePack();
      out.add(msgpack.read(array));
    }
}
