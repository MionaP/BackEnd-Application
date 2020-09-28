package miona.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import miona.data.entities.UserAccount;
import miona.data.service.UserAccountService;
import miona.rest.dto.RestResponseDto;

@RestController
@RequestMapping("/rest")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserAccountService userAccountService;
	
	/**
	 * Method used for authenticate user
	 * @param userAccount
	 * @return
	 */
	@RequestMapping(value = "/authentication", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponseDto> createUser(@RequestBody UserAccount userAccount) {
		
		Authentication authentication = null;
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				userAccount.getUsername(), userAccount.getPassword());
		
		authentication = this.authenticationManager.authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return new ResponseEntity<>(new RestResponseDto("Authentication success"),	HttpStatus.OK);
	}
	
	/**
	 * Temporary controller to test security
	 * @return
	 */
	@RequestMapping(value = "/testSecurity", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponseDto> testSecurity() {
		return new ResponseEntity<>(new RestResponseDto("secure"),	HttpStatus.OK);
		
	}
	
	/**
	 * Method used for register user
	 * @param userAccount
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponseDto> registerUser(@RequestBody UserAccount userAccount) {
		userAccountService.validateAndRegister(userAccount);
		return new ResponseEntity<>(new RestResponseDto("Registration successful and confirmation mail sent"),	HttpStatus.OK);
	}
	
	/**
	 * Method used for activate user. 
	 * Clicking on activation link which is sent in confirmation e-mail this method will be executed
	 * @param mailHash
	 * @return
	 */
	@RequestMapping(value = "/activate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponseDto> activate(@RequestParam String mailHash) {
		userAccountService.activatUser(mailHash);
		return new ResponseEntity<>(new RestResponseDto("User activated"),	HttpStatus.OK);
	}
	

	/**
	 * Method used when logout is success
	 * @return
	 */
	@RequestMapping(value = "/success/logout", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponseDto> logout() {
		//TODO
		return new ResponseEntity<>(new RestResponseDto("logout success"),	HttpStatus.OK);
	}

}
