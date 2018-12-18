package io.netty.example.study.ssl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpiderClientBusiHandler extends SimpleChannelInboundHandler<Object> {

    static final Logger logger = LoggerFactory.getLogger(SpiderClientBusiHandler.class);
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object origMsg) {
        System.out.println(origMsg.toString());
    }
    
    @Override 
    public void exceptionCaught(ChannelHandlerContext ctx,  
            Throwable cause) throws Exception {  
        cause.printStackTrace();
    }
}