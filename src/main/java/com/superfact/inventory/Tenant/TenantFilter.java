package com.superfact.inventory.Tenant;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TenantFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(TenantFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String tenantId = request.getHeader("tenantId");

        logger.debug("TenantFilter: Processing request for tenantId: {}", tenantId);

        if (tenantId == null || tenantId.isEmpty()) {
            logger.error("TenantFilter: Missing tenantId header");
            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing tenantId header");
            return;
        }

        TenantContext.setCurrentTenant(tenantId);

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            logger.debug("TenantFilter: Clearing tenant context for tenantId: {}", tenantId);
            TenantContext.clearCurrentTenant();
        }
    }
}
