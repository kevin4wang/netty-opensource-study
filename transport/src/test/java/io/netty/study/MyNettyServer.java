package io.netty.study;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class MyNettyServer {
    private static final String IP = "127.0.0.1";
    private static final int PORT = 5656;

    private static final int BIZTHREADSIZE = 100;
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(BIZTHREADSIZE);

    public static void service() throws Exception {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<Channel>() {

            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
//                 pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
//                    pipeline.addLast(new LengthFieldPrepender(4));
//                    pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
//                    pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                pipeline.addLast(new TcpServerHandler());
            }

        });
        ChannelFuture f = bootstrap.bind(IP, PORT).sync();
        f.channel().closeFuture().sync();
        System.out.println("TCP服务器已启动");
    }

    protected static void shutdown() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("开始启动TCP服务器...");
        MyNettyServer.service();
//            HelloServer.shutdown();
    }
}