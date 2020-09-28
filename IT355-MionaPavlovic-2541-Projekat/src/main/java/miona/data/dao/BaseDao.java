package miona.data.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


/**
 * Dao object for any repository class. It is used for inheritance of necessary DAO methods
 */
@Transactional
public class BaseDao {
	/*
	 * Spring data (JpaRepository interface) could be used instead standard repository classes
	 */

	protected Class<?> entityClass;

	@Autowired
	private SessionFactory sessionFactory;

	/**A method is used to save an object into database
	 * @param transientInstance
	 */
	public void persist(Object transientInstance) {
		sessionFactory.getCurrentSession().persist(transientInstance);
	}

	/**A method is used for saving or updating object in database depending on objects existence in database
	 * @param instance
	 */
	public void attachDirty(Object instance) {
		sessionFactory.getCurrentSession().saveOrUpdate(instance);
	}

	/***A method used to lock object in database, meaning it can't be  modified or read from multiple sources simultaneously
	 * @param instance
	 */
	public void attachClean(Object instance) {
		sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
	}

	/**A method used to delete object from database
	 * @param persistentInstance
	 */
	public void delete(Object persistentInstance) {
		sessionFactory.getCurrentSession().delete(persistentInstance);
	}

	/**A method used to delete object with certain id from database
	 * @param id
	 */
	public void delete(int id) {
		sessionFactory.getCurrentSession().delete(findById(id));
	}

	/**A method used for updating existing object from database
	 * @param detachedInstance
	 * @return Object updated
	 */
	public <T> Object merge(T detachedInstance) {
		return sessionFactory.getCurrentSession().merge(detachedInstance);
	}

	/**A method used for extracting object with certain id from database
	 * @param id
	 * @return Object with id received as parameter
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public <T> T findById(int id) {
		return (T) sessionFactory.getCurrentSession().get(entityClass, id);
	}


	/**A method used or extracting all objects from database
	 * @return List<Object> containing all objects from database
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public <T> List<T> findAll() {
		CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<T> query = (CriteriaQuery<T>) builder.createQuery(entityClass);
		Root<T> root = (Root<T>) query.from(entityClass);
		query.select(root);
		Query<T> q = sessionFactory.getCurrentSession().createQuery(query);
		List<T> results = q.getResultList();
		return results;
	}
	
	
	/**
	 * A method used or extracting all objects from database including pagination
	 * @param page
	 * @param size
	 * @return List<Object> containing all objects from database 
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public <T> List<T> findAll(int page, int size) {
		CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<T> query = (CriteriaQuery<T>) builder.createQuery(entityClass);
		Root<T> root = (Root<T>) query.from(entityClass);
		query.select(root);
		Query<T> q = sessionFactory.getCurrentSession().createQuery(query);
		q.setFirstResult((page - 1) * size);
		q.setMaxResults(size);
		List<T> results = q.getResultList();
		return results;
	}
}
