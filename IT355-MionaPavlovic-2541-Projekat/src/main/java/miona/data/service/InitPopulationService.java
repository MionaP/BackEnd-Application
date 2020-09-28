package miona.data.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import miona.data.dao.RolesDao;
import miona.data.dao.UserAccountDao;

@Service
public class InitPopulationService {

	@Autowired
	private RolesDao rolesDao;
	
	@Autowired
	private UserAccountDao userAccountDao;
	
	/**
	 * Method is executed after dependency injection is done and used for populating predefined values in DB
	 * <p> populates Roles table  
	 * <p> populates User_accounts table
	 */
	@PostConstruct
	private void init() {
		rolesDao.populateRoles();
		userAccountDao.populateUserAccounts();
	}
}
