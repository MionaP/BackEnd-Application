package miona.customexceptions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import miona.data.dao.LinksDao;
import miona.data.dao.TagsDao;
import miona.data.dao.UserAccountDao;
import miona.data.entities.Links;
import miona.data.entities.Tags;
import miona.data.entities.UserAccount;
import miona.data.service.LinksService;

@Component
public class BusinessValidation {
	
	@Autowired
	private LinksDao linksDao;
	
	@Autowired
	private TagsDao tagsDao;
	
	@Autowired
	private LinksService linksService;
	
	@Autowired
	private UserAccountDao userAccountsDao;
	
	/**
	 * Method validates user registration:
	 * <p> - username can not be empty
	 * <p> - username must not exist in database
	 * <p> - password can not be empty
	 * <p> - email can not be empty
	 * <p> - email must not exist in database 
	 * @param userAccount
	 */
	public void validateUserRegistration(UserAccount userAccount) {
		if(userAccount.getUsername() == null) {
			throw new ValidationException("username mandatory", userAccount);
		}
		if(userAccount.getPassword() == null) {
			throw new ValidationException("password mandatory", userAccount);
		}
		if(userAccount.getEmail() == null) {
			throw new ValidationException("email mandatory", userAccount);
		}
		if(userAccountsDao.loadUserByUsername(userAccount.getUsername()) != null) {
			throw new ValidationException("username already exists", userAccount);
		}
		if(userAccountsDao.findByEmail(userAccount.getEmail()) != null) {
			throw new ValidationException("email already exists", userAccount);
		}
		
	}
	

	/**
	 * Method validate does tag already exist for given link
	 * @param linkId
	 * @param tag
	 */
	public Links validateTag(Integer linkId, Tags tag) {
		//check tag exists
		Tags tagFromDb = tagsDao.findByLinkAndTagName(linkId, tag.getTagName());
		if(tagFromDb != null) {
			throw new ValidationException("Tag already exist", tag);
		}
		
		Links link = this.checkLinkBelonging(linkId);
		return link;
		
	}
	
	/**
	 * Method checks does link belongs to logged user
	 * @param linkId
	 * @return
	 */
	public Links checkLinkBelonging(Integer linkId) {
		
		Links link = linksDao.findByUserAndLinkId(linkId);
		if(link ==  null) {
			throw new ValidationException("You can view or change only link which is added by your account");
		}
		return link;
	}
	
	/**
	 * Method validates adding new link
	 * <p>Does link have at least one tag
	 * <p>Does link already exist for logged user with same tags
	 * @param link
	 */
	public void validateLink(Links link) {
		String url = link.getUrl();
		if(!url.contains("?")) {
			throw new ValidationException("Link must have at least one tag", url);
		}
		//handle different variations as same
		String baseUrl = url.replace("http://", "").split("\\?")[0];		
		List<Links> linksByBaseUrl = linksDao.findByUserAndBaseUrl(baseUrl);
		if(linksByBaseUrl != null && !linksByBaseUrl.isEmpty()) {
			for (Links linkByBaseUrl : linksByBaseUrl) {
				List<Tags> listTagsFromDb = linkByBaseUrl.getListTags();
				List<Tags> listTagsFromLink = linksService.findTagsFromLink(link);
				//Check does all tags from link (to save) exist in list of tags form link(from database) with the same base url
				boolean allExist = true;
				for (Tags tagFromLink : listTagsFromLink) {
					boolean exist = listTagsFromDb.stream().anyMatch(e -> e.getTagName().equals(tagFromLink.getTagName()) 
																		&& e.getTagValue().equals(tagFromLink.getTagValue()));
					if(!exist) {
						allExist = false;
					}
				}
				if(allExist) {
					throw new ValidationException("Link already exist", url);
				}
				
			}
		}//else url does not exist free to continue
		
	}

}
