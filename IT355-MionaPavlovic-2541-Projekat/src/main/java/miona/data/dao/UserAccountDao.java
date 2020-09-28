package miona.data.dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import miona.data.entities.UserAccount;


@Repository(value = "userAccountDao")
public class UserAccountDao extends BaseDao implements UserDetailsService{

	@Autowired
	private SessionFactory sessionFactory;
	
	
	public UserAccountDao() {
		entityClass = UserAccount.class;
	}
	
	
	@Transactional
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Create CriteriaBuilder
		CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();

		// Create CriteriaQuery
		CriteriaQuery<UserAccount> criteria = builder.createQuery(UserAccount.class);
		Root<UserAccount> pet = criteria.from(UserAccount.class);
		criteria.where(builder.equal(pet.get("username"), username));
		
		UserAccount userAccount = sessionFactory.getCurrentSession().createQuery(criteria).uniqueResult();

		return userAccount;
	}

	/**
	 * Method populates USER_ACCOUNT table with predefined values
	 */
	@Transactional
	public void populateUserAccounts() {
		
		String sql = "insert into USER_ACCOUNT (id, username, password, role, email, enabled, mail_hash, mail_hash_timestamp) values "
				+ "(1, 'user', '$2y$11$DSQVqKtU85ubRAsRoqhsi.AYBx.vg8aPhxVc.yWkmRNmiWLm5Ay4O', 1, 'test2@tast.er', 1, null, null),"
				+ "(2, 'user1', '$2y$11$DSQVqKtU85ubRAsRoqhsi.AYBx.vg8aPhxVc.yWkmRNmiWLm5Ay4O', 1, 'test1@tast.er', 1, null, null)";
		
		sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
			
	}
	
	/**
	 * Method for finding {@link UserAccount} by given email
	 * @param email
	 * @return
	 * @throws UsernameNotFoundException
	 */
	@Transactional
	public UserAccount findByEmail(String email) {
		// Create CriteriaBuilder
		CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();

		// Create CriteriaQuery
		CriteriaQuery<UserAccount> criteria = builder.createQuery(UserAccount.class);
		Root<UserAccount> pet = criteria.from(UserAccount.class);
		criteria.where(builder.equal(pet.get("email"), email));
		
		UserAccount userAccount = sessionFactory.getCurrentSession().createQuery(criteria).uniqueResult();

		return userAccount;
	}
	
	/**
	 * Method for finding {@link UserAccount} by given mail hash
	 * @param mailHash
	 * @return
	 */
	@Transactional
	public UserAccount findByMailHash(String mailHash) {
		String hql = "from UserAccount ua where ua.mailHash = :mailHash";
		UserAccount userAccount = (UserAccount) sessionFactory.getCurrentSession().createQuery(hql).setParameter("mailHash", mailHash).uniqueResult();
		return userAccount;
	}

}
