package miona.data.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import miona.data.entities.Roles;
import miona.data.entities.UserAccount;


@Repository(value = "rolesDao")
public class RolesDao extends BaseDao{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	
	public RolesDao() {
		entityClass = Roles.class;
	}
	
	/**
	 * Method populates ROLES table with predefined values
	 */
	@Transactional
	public void populateRoles() {
		
		String sql = "insert into ROLES (id, name) values "
				+ "(1, 'ROLE_USER')";
		
		sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
			
	}
	
	/**
	 * Method for finding {@link Roles} by given role name
	 * @param roleName
	 * @return
	 */
	@Transactional
	public Roles findByRoleName(String roleName) {
		String hql = "from Roles role where role.name = :roleName";
		Roles role = (Roles) sessionFactory.getCurrentSession().createQuery(hql).setParameter("roleName", roleName).uniqueResult();
		return role;
	}

}
