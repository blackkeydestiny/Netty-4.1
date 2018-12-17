package io.netty.example.time;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.UnsupportedEncodingException;

/**
 * Handler implementation for the echo client.  It initiates the ping-pong
 * traffic between the time client and server by sending the first message to
 * the server.
 */
public class TimeCllientHandler extends ChannelInboundHandlerAdapter {
    // Demo Code 1 : time server
    //private final ByteBuf firstMessage;

    // Demo Code 2 : Sticky and unpacking bag
    private int counter;
    private final byte[] req;

    /**
     * Creates a client-side handler.
     */
    public TimeCllientHandler() {
        // Demo Code 1 : time server
//        byte[] req = "QUERY TIME ORDER".getBytes();
//        firstMessage = Unpooled.buffer(req.length);
//        firstMessage.writeBytes(req);

        // Demo Code 2 : Sticky and unpacking bag
        req = ("QUERY TIME ORDER" + System.getProperty("line.separator")).getBytes();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // Demo Code 2 : Sticky and unpacking bag
        ByteBuf message;
        for (int i = 0; i < 100; i++){
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        // Demo Code 1 : time server
//        ByteBuf buf = (ByteBuf)msg;
//        byte[] req = new byte[buf.readableBytes()];
//        buf.readBytes(req);
//        String body = new String(req, "UTF-8");
//        System.out.println("Now is : " + body);

        // Demo Code 2 : Sticky and unpacking bag
//        ByteBuf buf = (ByteBuf)msg;
//        byte[] req = new byte[buf.readableBytes()];
//        buf.readBytes(req);
//        String body = new String(req, "UTF-8");
//        System.out.println("Now is : " + body + " ; the counter is : " + ++counter);

        // Demo Code 2 : resolve sticky and unpacking bag
        String body = (String) msg;
        System.out.println("Now is : " + body + " ; the counter is : " + ++counter);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
