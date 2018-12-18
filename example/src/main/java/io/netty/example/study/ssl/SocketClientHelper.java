package io.netty.example.study.ssl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.text.MessageFormat;

import javax.net.ssl.TrustManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketClientHelper {
    static final Logger logger = LoggerFactory.getLogger(SocketClientHelper.class);
    
    public static void main(String[] args) {
        Channel channel = SocketClientHelper.createChannel("localhost",9912);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SocketHelper.writeMessage(channel, "ssh over tcp test 1");
        SocketHelper.writeMessage(channel, "ssh over tcp test 2");
        SocketHelper.writeMessage(channel, "ssh over tcp test 3");
        SocketHelper.writeMessage(channel, "ssh over tcp test 4");
        SocketHelper.writeMessage(channel, "ssh over tcp test 5");
    }
    
    public static Channel createChannel(String host, int port) {
        Channel channel = null;  
        Bootstrap b = getBootstrap();
        try {  
            channel = b.connect(host, port).sync().channel();
            logger.info(MessageFormat.format("connect to spider server ({0}:{1,number,#}) success for thread [" + Thread.currentThread().getName() + "].", host,port));
        } catch (Exception e) {
            e.printStackTrace();
        }  
        return channel;
    }
    
    public static Bootstrap getBootstrap(){  
        EventLoopGroup group;
        Class<? extends Channel> channelClass = NioSocketChannel.class;
        group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();  
        b.group(group).channel(channelClass);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.option(ChannelOption.TCP_NODELAY, true);
        b.option(ChannelOption.SO_REUSEADDR, true);
        b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        TrustManagerFactory tf = null; 
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream("D:\\security\\client\\cChat.jks"), "cNetty".toCharArray());
            tf = TrustManagerFactory.getInstance("SunX509");
            tf.init(keyStore);
            SslContext sslContext = SslContextBuilder.forClient().trustManager(tf).build();
            b.handler(new SslChannelInitializer(sslContext));
            return b;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}