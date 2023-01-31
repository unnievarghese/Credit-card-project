package com.UBank.GateWay;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletRequest;

public class SimplePreFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String token = request.getHeader("Authorization");
    try {
        token = token.replace("Bearer ", "");
        String Crn = Utils.getCrn(token);
        ctx.addZuulRequestHeader("CRN",Crn);
    }
    catch (Exception e){
        return null;
    }
        return null;
    }
}
