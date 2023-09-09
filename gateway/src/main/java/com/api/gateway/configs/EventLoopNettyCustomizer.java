package com.api.gateway.configs;

import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import reactor.netty.http.server.HttpServer;

public class EventLoopNettyCustomizer implements NettyServerCustomizer {

	@Override
	public HttpServer apply(final HttpServer httpServer) {
		final EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		eventLoopGroup.register(new NioServerSocketChannel());
		return httpServer.runOn(eventLoopGroup);
	}
}
