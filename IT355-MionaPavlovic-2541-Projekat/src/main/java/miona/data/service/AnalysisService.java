package miona.data.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import miona.customexceptions.BusinessValidation;
import miona.customexceptions.ValidationException;
import miona.data.dao.LinksDao;
import miona.data.entities.Links;
import miona.data.entities.Tags;


@Service
public class AnalysisService {
	
	@Autowired
	private LinksDao linksDao;
	
	@Autowired
	private BusinessValidation businessValidation;

	/**
	 * Method for getting page content from url
	 * 
	 * @param url
	 * @return String content
	 */
	public String getContent(String url) {
		
		String urlWithouTags = url.split("\\?")[0];//without tags
		
		try {
			URLConnection connection = new URL(urlWithouTags).openConnection();
			connection.connect();

			InputStream is = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			Document doc = Jsoup.parse(sb.toString());
			String content = doc.body().text();
			return content;
		} catch (Exception e) {
			throw new ValidationException("Url does not exist or content can not be parsed. You can not get analysis for this url", urlWithouTags);
		}
	}

	/**
	 * Method used to create map based on word repeat in text.
	 * Key is actual word and value is number of repeating.
	 * Method will return map with maximum 10 entries (sorted desc).
	 * @param text
	 * @return
	 */
	public Map<String,Integer> createWordMap(String text) {
		Map<String, Integer> wordMap = new HashMap<String, Integer>();
		text = text.toLowerCase();
		String[] words = text.split("\\W+");
		for (String word : words) {
			//consider just words longer than 3 characters
			if(word.length()>3) {
				if (wordMap.containsKey(word)) {
					wordMap.put(word, (wordMap.get(word) + 1));
				} else {
					wordMap.put(word, 1);
				}
			}
		}
		//sort by value and limit to return maximum 10 entires
		Map<String,Integer> topTen =
				wordMap.entrySet().stream()
			       .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
			       .limit(10)
			       .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		
		return topTen;
	}

	/**
	 * Method used for getting analysis of content for given link.
	 * @param linkId
	 * @return Map which contains top 10 suggested tags and all existing tags for the link
	 */
	public Map<String, Object> getContentAnalysis(Integer linkId) {
		Links link =businessValidation.checkLinkBelonging(linkId);
		String pageContent = this.getContent(link.getUrl());
		Map<String, Integer> topTen = this.createWordMap(pageContent);
		Map<String, Object> returnMap = this.createResponseMap(topTen, link);
		return returnMap;
	}

	/**
	 * Method for getting analysis for given link by other users
	 * @param linkId
	 * @return Map which contains top 10 suggested tags and all existing tags for the link
	 */
	public Map<String, Object> getLinkAnalysis(Integer linkId) {
		Links link =businessValidation.checkLinkBelonging(linkId);
		List<Links> allLinksByBaseUrl = linksDao.findByBaseUrl(link.getUrl());
		if(allLinksByBaseUrl == null || allLinksByBaseUrl.isEmpty()) {
			throw new ValidationException("There are not suggestions for given link, try with content analysis", link.getUrl());
		}
		//put all tagNames in one string
		StringBuffer sb = new StringBuffer();
		for (Links links : allLinksByBaseUrl) {
			for(Tags tag : links.getListTags()) {
				sb.append(tag.getTagName());
				sb.append(",");
			}
		}
		Map<String, Integer> topTen = this.createWordMap(sb.toString());
		Map<String, Object> returnMap = this.createResponseMap(topTen, link);
		return returnMap;
	}
	
	/**
	 * Method used for creating map for response
	 * Map contains 10 suggested tags for given link and existing tags for given link
	 * @param topTen
	 * @param link
	 * @return
	 */
	public Map<String, Object> createResponseMap(Map<String, Integer> topTen, Links link) {
		Map<String, String> existingTagMap = new HashMap<String, String>();
		for(Tags tag : link.getListTags()) {
			existingTagMap.put(tag.getTagName(), tag.getTagValue());
		}
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("suggestedTags", topTen);
		returnMap.put("existingTag", existingTagMap);
		return returnMap;
	}
	
	
}
