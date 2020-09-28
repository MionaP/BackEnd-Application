package miona.data.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import miona.customexceptions.BusinessValidation;
import miona.data.dao.LinksDao;
import miona.data.dao.TagsDao;
import miona.data.entities.Links;
import miona.data.entities.Tags;
import miona.security.SecurityUtils;

@Service
public class LinksService {
	
	@Autowired
	private LinksDao linksDao;
	
	@Autowired
	private TagsDao tagsDao;
	
	@Autowired
	private BusinessValidation linksTagsValidation;
	
	/**
	 * Method validates and adds new Link
	 * @param link
	 */
	@Transactional
	public void addNewLink(Links link) {
		link.setUserAccount(SecurityUtils.getUserFromContext());
		linksTagsValidation.validateLink(link);
		linksDao.attachDirty(link);
		List<Tags> tagsList = this.findTagsFromLink(link);
		for (Tags tags : tagsList) {
			tagsDao.attachDirty(tags);
		}
	}

	/**
	 * Method for getting all tags from link and saving tags 
	 * @param link
	 * @return List of {@link Tags}
	 */
	public List<Tags> findTagsFromLink(Links link) {
		List<Tags> tagsList = new ArrayList<>();
		String url = link.getUrl();
		String tagsPart = url.split("\\?")[1];// url must contain ? because validation says "Each link must have at least one tag"
		String[] tagsWithValues = tagsPart.split("&");//foo=bar , hello=world
		for (String tagWithValue : tagsWithValues) {
			String[] tagNameValue = tagWithValue.split("=");//foo , bar
			Tags tag = new Tags();
			tag.setLink(link);
			tag.setTagName(tagNameValue[0]);//foo
			tag.setTagValue(tagNameValue[1]);//bar
			tagsList.add(tag);
		}
		return tagsList;
	}

	
	/**
	 * Method for finding all {@link Links} by logged
	 * @return List of {@link Links}
	 */
	public List<Links> findAllByUser(){
		List<Links> linksByUser = linksDao.findAllByUser();
		return linksByUser;
	}

	/**
	 * Method for adding new {@link Tags} and updating link url
	 * @param linkId
	 * @param tag
	 * @return
	 */
	@Transactional
	public Links addNewTagToLink(Integer linkId, Tags tag) {
		Links link = linksTagsValidation.validateTag(linkId, tag);
		//update link
		String newUrl = link.getUrl()+"&"+tag.getTagName()+"="+tag.getTagValue();//add new tag to link url
		link.setUrl(newUrl);
		Links updatedLink = (Links) linksDao.merge(link);
		tag.setLink(updatedLink);
		tagsDao.attachDirty(tag);
		return updatedLink;
	}


	/**
	 * Method for finding list of {@link Links} by given tag name
	 * @param tagName
	 * @return
	 */
	public List<Links> fingByTagName(String tagName) {
		List<Links> linkList = linksDao.findByTagNameForUser(tagName);
		return linkList;
	}

}
