package com.cisco.cmad.stackflood.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class AuthFilter implements Filter{
	
	private static final String IGNORE_AUTH_PROP_NAME = "bypassAuthPatterns";

    private Set<String> bypassUrls = new HashSet<String>();
    
    public void init(FilterConfig filterConfig) throws ServletException {
		String ignoreUrls = filterConfig.getInitParameter(IGNORE_AUTH_PROP_NAME);
        if (ignoreUrls != null) {
            bypassUrls.addAll(Arrays.asList(ignoreUrls.split(",")));
        }
	}

	public void destroy() {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String contextPath = httpRequest.getContextPath();
		String path = httpRequest.getRequestURI();
        path = path.substring(contextPath.length());
		if (checkIfBypassUrl(path)) {
            filterChain.doFilter(request, response);
        } else{
        	HttpSession session = httpRequest.getSession(false);
    		Cookie[] cookies = httpRequest.getCookies();
    		String sessionUserName = null;
    		String cookieUserName = null;
    		if(null != cookies && cookies.length > 0){
    			for (Cookie cookie : cookies) {
    				if(cookie.getName().equals("user-name")){
    					cookieUserName = cookie.getValue();
    					break;
    				}
    			}
    		}
    		System.out.println("cookieUserName: "+cookieUserName);
    		sessionUserName = (null != session?session.getAttribute("user-name").toString():null);
    		System.out.println("sessionUserName: "+sessionUserName);
            if (session != null && sessionUserName != null && cookieUserName != null && sessionUserName.equals(cookieUserName)) {
            	filterChain.doFilter(request, response);
            } else {
            	return;
            }
        }
		
	}

	private boolean checkIfBypassUrl(String path) {
        boolean bypassUrl = false;
        if (!bypassUrls.isEmpty()) {
            for (String urlPattern : bypassUrls) {
            	System.out.println("Path : "+path);
            	System.out.println("UrlPattern : "+urlPattern);
                if (path.equals(urlPattern)) {
                    bypassUrl = true;
                    break;
                }
            }
        }
        return bypassUrl;
    }
	
	//@Context
    /*private HttpServletResponse servletResponse;

	@SuppressWarnings("rawtypes")
	public void filter(ContainerRequestContext requestContext) throws IOException{
		System.out.println("My filter");
        String userName = null;
        Iterator it = requestContext.getCookies().entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            if(pairs.getKey().equals("user-name")) {
            	userName = pairs.getValue().toString();
            }
        }
        if(null == userName || "".equals(userName)){
        	servletResponse.sendError(403, "Please login");
        }
		return;
	}*/
}
