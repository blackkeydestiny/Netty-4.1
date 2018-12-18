package io.netty.nettyBook2Example.chapterVII_MessagePack;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * Sends one message when a connection is open and echoes back any received data
 * to the server. Simply put, the echo client initiates the ping-pong traffic
 * between the echo client and server by sending the first message to the
 * server.
 */
public final class EchoClientV2 {
    private final String host;
    private final int port;
    private final int sendNumber;

    public EchoClientV2(String host, int port, int sendNumber) {
        this.host = host;
        this.port = port;
        this.sendNumber = sendNumber;
    }

    public void run() throws Exception {
        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
             .option(ChannelOption.TCP_NODELAY, true)
             .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch)
                         throws Exception {
                     ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                     ch.pipeline().addLast("msgpack decoder", new MsgpackDecoder());
                     ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
                     ch.pipeline().addLast("msgpack encoder", new MsgpackEncoder());
                     ch.pipeline().addLast(
                             new EchoClientHandler(sendNumber));
                 }
             });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        // Print usage if no argument is specified.
        if (args.length < 2 || args.length > 3) {
            System.err.println("Usage: " + EchoClientV2.class.getSimpleName()
                               + " <host> <port> [<send message number>]");
            return;
        }

        // Parse options.
        final String host = args[0];
        final int port = Integer.parseInt(args[1]);
        final int sendNumber;
        if (args.length == 3) {
            sendNumber = Integer.parseInt(args[2]);
        } else {
            sendNumber = 1;
        }

        new EchoClientV2(host, port, sendNumber).run();
    }
}
