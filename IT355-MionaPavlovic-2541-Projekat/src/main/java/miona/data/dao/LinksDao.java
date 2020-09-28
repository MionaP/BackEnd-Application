package miona.data.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import miona.data.entities.Links;
import miona.security.SecurityUtils;


@Repository(value = "linksDao")
public class LinksDao extends BaseDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public LinksDao() {
		entityClass = Links.class;
	}

	/**
	 * Method for finding all links by given base url
	 * @param baseUrl
	 * @return List of {@link Links}
	 */
	@Transactional
	public List<Links> findByUserAndBaseUrl(String baseUrl) {
		String hql = "from Links links where links.userAccount.id = :userId and links.url like :baseUrl";
		List<Links> linksByBaseUrl = sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("userId", SecurityUtils.getUserFromContext().getId())
				.setParameter("baseUrl", "%"+baseUrl+"%").list();
		return linksByBaseUrl;
	}
	
	/**
	 * Method for finding all {@link Links} by logged
	 * @return List of {@link Links}
	 */
	@Transactional
	public List<Links> findAllByUser() {
		String hql = "from Links links where links.userAccount.id = :userId";
		List<Links> linksByUser = sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("userId", SecurityUtils.getUserFromContext().getId()).list();
		return linksByUser;
	}

	/**
	 * Method for finding {@link Links} by given id and logged user
	 * @param linkId
	 * @return
	 */
	@Transactional
	public Links findByUserAndLinkId(Integer linkId) {
		String hql = "from Links links where links.id = :linkId and links.userAccount.id = :userId";
		Links link = (Links) sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("userId", SecurityUtils.getUserFromContext().getId())
				.setParameter("linkId", linkId).uniqueResult();
		return link;
	}

	/**
	 * Method for finding list of {@link Links} by given tag name
	 * @param tagName
	 * @return
	 */
	@Transactional
	public List<Links> findByTagNameForUser(String tagName) {
		String hql = "select tag.link from Tags tag where tag.tagName = :tagName and tag.link.userAccount.id = :userId";
		List<Links> linkList = sessionFactory.getCurrentSession().createQuery(hql)
															.setParameter("userId", SecurityUtils.getUserFromContext().getId())
															.setParameter("tagName", tagName)
															.list();
		return linkList;
	}

	/**
	 * Method for finding all {@link Links} by url without tags for the all other users except logged one
	 * @param url
	 * @return
	 */
	@Transactional
	public List<Links> findByBaseUrl(String url) {
		String urlWithouTags = url.split("\\?")[0];//without tags
		String hql = "from Links links where links.userAccount.id != :userId and links.url like :baseUrl";
		List<Links> linksByBaseUrl = sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("userId", SecurityUtils.getUserFromContext().getId())
				.setParameter("baseUrl", "%"+urlWithouTags+"%").list();
		return linksByBaseUrl;
	}

	
}
