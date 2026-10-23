package com.ltm.training.producthive.security;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import io.jsonwebtoken.JwtException;

/**
 * The filter runs for incoming requests, reads the bearer token, validates it, 
 * and establishes the authenticated identity in Spring Security.
 * 
 * A request without a token is not automatically rejected by this filter. 
 * The security configuration in the next step decides whether the requested 
 * endpoint requires authentication.
 * A JWT that is malformed, expired, or has an invalid signature receives an 
 * HTTP 401 Unauthorized response.
 */

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
		super();
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
	}


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String header = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (header == null || !header.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = header.substring(7);

		try {
			String email = jwtService.extractEmail(token);

			if (email != null
					&& SecurityContextHolder.getContext()
					.getAuthentication() == null) {

				UserDetails userDetails = userDetailsService.loadUserByUsername(email);

				if (jwtService.isTokenValid(token, email)) {

					UsernamePasswordAuthenticationToken authentication =
							new UsernamePasswordAuthenticationToken(
									userDetails,
									null,
									userDetails.getAuthorities());

					authentication.setDetails(
							new WebAuthenticationDetailsSource()
							.buildDetails(request));

					SecurityContextHolder.getContext()
					.setAuthentication(authentication);
				}
			}
		} catch (JwtException | IllegalArgumentException ex) {
			SecurityContextHolder.clearContext();
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Invalid or expired JWT");
			return;
		}

		filterChain.doFilter(request, response);

	}
}
