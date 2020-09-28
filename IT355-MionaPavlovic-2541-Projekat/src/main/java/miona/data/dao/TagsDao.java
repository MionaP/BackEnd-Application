package miona.data.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import miona.data.entities.Links;
import miona.data.entities.Tags;

@Repository(value = "tagsDao")
public class TagsDao extends BaseDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public TagsDao() {
		entityClass = Tags.class;
	}

	/**
	 * Method for finding {@link Tags} by given name and link id
	 * @param linkId - of {@link Links}
	 * @param tagName
	 * @return
	 */
	public Tags findByLinkAndTagName(Integer linkId, String tagName) {
		String hql = "from Tags tag where tag.tagName = :tagName and tag.link.id = :linkId";
		Tags tag = (Tags) sessionFactory.getCurrentSession().createQuery(hql)
															.setParameter("tagName", tagName)
															.setParameter("linkId", linkId)
															.uniqueResult();
		return tag;
	}
}
