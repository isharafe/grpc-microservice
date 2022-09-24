package com.ruchira.learn.grpc.util.filter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class RequestLoggerFilter implements Filter {

	private Logger logger = LoggerFactory.getLogger(RequestLoggerFilter.class);

	private String localHostAdrs;
	private String localHostName;

	public RequestLoggerFilter() {
		try {
			this.localHostAdrs = InetAddress.getLocalHost().getHostAddress();
			this.localHostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String reqUid = UUID.randomUUID().toString();
		String url = (request instanceof HttpServletRequest) ?
				((HttpServletRequest) request).getRequestURI() : null;
		logger.info("Request receive: from: {}, url: {}, host: {} - {}. reqId:{}",
				request.getRemoteHost(), url, localHostAdrs, localHostName, reqUid);

		long t1 = System.currentTimeMillis();
		chain.doFilter(request, response);
		long t2 = System.currentTimeMillis();

		logger.info("Request completed. time:{}. reqId:{}", (t2-t1), reqUid);
	}

}
