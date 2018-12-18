package io.netty.example.study.ssl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpiderServerBusiHandler extends SimpleChannelInboundHandler<Object> {
    static final Logger logger = LoggerFactory.getLogger(SpiderServerBusiHandler.class);
    
    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final Object msg)
            throws Exception {
        System.out.println(msg.toString());
    }
    
    @Override 
    public void exceptionCaught(ChannelHandlerContext ctx,  
            Throwable cause) throws Exception {  
        logger.error("channel " + ((InetSocketAddress)ctx.channel().remoteAddress()).toString() + " exception:",cause);
        ctx.close();
    }
}