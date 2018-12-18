package io.netty.example.study.ssl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//https://www.cnblogs.com/zhjh256/p/6488668.html
public class SocketServerHelper {
    static final Logger logger = LoggerFactory.getLogger(SocketServerHelper.class);
    private static int WORKER_GROUP_SIZE = Runtime.getRuntime().availableProcessors() * 2; 

    private static EventLoopGroup bossGroup; 
    private static EventLoopGroup workerGroup;  
    
    private static Class<? extends ServerChannel> channelClass;
    
    public static void startSpiderServer() throws Exception {
        ServerBootstrap b = new ServerBootstrap();
        b.childOption(ChannelOption.TCP_NODELAY, true)
        .childOption(ChannelOption.SO_KEEPALIVE, true)
        .childOption(ChannelOption.SO_REUSEADDR, true)    
        .childOption(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(false))
        .childOption(ChannelOption.SO_RCVBUF, 1048576)
        .childOption(ChannelOption.SO_SNDBUF, 1048576);
        
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup(WORKER_GROUP_SIZE);
        channelClass = NioServerSocketChannel.class;
        logger.info("workerGroup size:" + WORKER_GROUP_SIZE);
        logger.info("preparing to start spider server...");
        b.group(bossGroup, workerGroup);  
        b.channel(channelClass);
        KeyManagerFactory keyManagerFactory = null;
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream("D:\\security\\server\\sChat.jks"), "sNetty".toCharArray());
        keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore,"sNetty".toCharArray());
        SslContext sslContext = SslContextBuilder.forServer(keyManagerFactory).build();
        b.childHandler(new SslChannelInitializer(sslContext)); 
        b.bind(9912).sync();  
        logger.info("spider server start sucess, listening on port " + 9912 + ".");  
    }
    
    public static void main(String[] args) throws Exception {
        SocketServerHelper.startSpiderServer();
    }
      
    public static void shutdown() {  
        logger.debug("preparing to shutdown spider server...");
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();  
        logger.debug("spider server is shutdown.");
    }
}