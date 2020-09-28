package miona.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;


public class AccessDeniedExceptionHandler implements AccessDeniedHandler  {

	
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) {

		try {
			response.getWriter().write("AccessDeniedException!");
			response.setStatus(403);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}