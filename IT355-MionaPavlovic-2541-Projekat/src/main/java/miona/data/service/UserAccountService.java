package miona.data.service;

import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;

import miona.customexceptions.BusinessValidation;
import miona.customexceptions.ValidationException;
import miona.data.dao.RolesDao;
import miona.data.dao.UserAccountDao;
import miona.data.entities.Roles;
import miona.data.entities.UserAccount;

@Service
public class UserAccountService {
	
	@Autowired
	private UserAccountDao userAccountsDao;
	
	@Autowired
	private RolesDao rolesDao;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private HttpServletRequest httpServletRequest;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private BusinessValidation businessValidation;
	
	/**
	 * Method validates user registration, saves new {@link UserAccount} and send 
	 * confirmation mail.
	 * @param userAccount
	 */
	public void validateAndRegister(UserAccount userAccount) {
		businessValidation.validateUserRegistration(userAccount);
		Roles userRole = rolesDao.findByRoleName("ROLE_USER");
		userAccount.setPassword(encoder.encode(userAccount.getPassword()));
		userAccount.setRoles(userRole);
		userAccount.setEnabled(false);
		String mailHash = this.createMailHashSecret(userAccount);
		userAccount.setMailHash(mailHash);
		userAccount.setMailHashTimestamp(new Date().getTime());
		
		int requestUrlLength = httpServletRequest.getRequestURL().length();
		int requestUriLength = httpServletRequest.getRequestURI().length();
		StringBuilder baseUrl = new StringBuilder(httpServletRequest.getRequestURL().replace(requestUrlLength - requestUriLength, requestUrlLength, httpServletRequest.getContextPath()));
		baseUrl.append("/rest/activate");
		baseUrl.append("?mailHash=");
		baseUrl.append(mailHash);
		
		mailService.sendMail(userAccount.getEmail(), "Activateion", "Dear " + userAccount.getUsername() + " thanks for registration on our system. Please activate your account by clicking on following link: " + baseUrl.toString());
		
		userAccountsDao.attachDirty(userAccount);
		
	}
	
	/**
	 * Method for creating mail hash secret code. Secret code is generated by applying SHA-256 algorithm on
	 * string (username + current time in miliseconds) 
	 * @param user
	 * @return
	 */
	public  String createMailHashSecret(UserAccount user){
		long currnetTime = System.currentTimeMillis();
		
		StringBuilder signatureBuilder = new StringBuilder();
		signatureBuilder.append(user.getUsername());
		signatureBuilder.append(currnetTime);

		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("No algorithm available!");
		}

		return new String(Hex.encode(digest.digest(signatureBuilder.toString().getBytes())));
	}



	/**
	 * Method find user by mailHash and activates user
	 * @param mailHash
	 */
	public void activatUser(String mailHash) {
		UserAccount userAccount = userAccountsDao.findByMailHash(mailHash);
		this.validateMailHash(userAccount);
		userAccount.setEnabled(true);
		userAccountsDao.attachDirty(userAccount);
		
	}

	/**
	 * Method checks mail hash. 
	 * If mail hash is older then 30 min system will throw an error
	 * @param userAccount
	 */
	private void validateMailHash(UserAccount userAccount) {
		if(userAccount == null) {
			throw new ValidationException("mail hash incorect");
		}else if(System.currentTimeMillis() - userAccount.getMailHashTimestamp() > 1800000) {//30 min in miliseconds TODO create constant
			throw new ValidationException("mail hash expired");
		}
		
	}

}
