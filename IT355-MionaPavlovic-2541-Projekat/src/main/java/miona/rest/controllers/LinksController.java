package miona.rest.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import miona.data.entities.Links;
import miona.data.entities.Tags;
import miona.data.service.AnalysisService;
import miona.data.service.LinksService;
import miona.rest.dto.RestResponseDto;


@RestController
@RequestMapping("/rest/links")
public class LinksController {
	
	@Autowired
	private LinksService linksService;
	@Autowired
	private AnalysisService analysisService;
	
	/**
	 * Method for adding new link
	 * @param link
	 * @return ResponseEntity containing the request status message and HTTP status code
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponseDto> addLink(@RequestBody Links link) {
		linksService.addNewLink(link);
		return new ResponseEntity<>(new RestResponseDto("Link successfully added", link.getUrl()),	HttpStatus.OK);
	}
	
	/**
	 * Method for finding all Links added by logged user
	 * @return ResponseEntity containing the list of {@link Links} along with HTTP status code
	 */
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponseDto> getAllLinks() {
		List<Links> linksByUser = linksService.findAllByUser();
		return new ResponseEntity<>(new RestResponseDto(linksByUser), HttpStatus.OK);
	}
	
	/**
	 * Method for adding new tag and updating link url
	 * @param linkId
	 * @param tag
	 * @return ResponseEntity containing the request status message and HTTP status code
	 */
	@RequestMapping(value = "/tags", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponseDto> addTagToLink(@RequestParam Integer linkId, @RequestBody Tags tag) {
		Links link = linksService.addNewTagToLink(linkId, tag);
		return new ResponseEntity<>(new RestResponseDto("Tag successfully added and link updated", link.getUrl()),	HttpStatus.OK);
	}
	
	/**
	 * Method for finding list of {@link Links} by given tag name
	 * @param tagName
	 * @return ResponseEntity containing the list of {@link Links} along with HTTP status code
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponseDto> searchByTagName(@RequestParam String tagName) {
		List<Links> linkList = linksService.fingByTagName(tagName);
		return new ResponseEntity<>(new RestResponseDto(linkList),	HttpStatus.OK);
	}
	
	/**
	 * Method used for doing analysis of page content by given link. 
	 * @param linkId
	 * @return ResponseEntity containing the map with suggested tags along with HTTP status codes
	 */
	@RequestMapping(value = "/textAnalysis", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponseDto> textAnalysis(@RequestParam Integer linkId) {
		Map<String, Object> analysisMap = analysisService.getContentAnalysis(linkId); 
		return new ResponseEntity<>(new RestResponseDto(analysisMap), HttpStatus.OK);
	}
	
	/**
	 * Method used for doing analysis of links added by other users. 
	 * @param linkId
	 * @return esponseEntity containing the map with suggested tags along with HTTP status codes
	 */
	@RequestMapping(value = "/analysis", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponseDto> linkAnalysis(@RequestParam Integer linkId) {
		Map<String, Object> analysisMap = analysisService.getLinkAnalysis(linkId); 
		return new ResponseEntity<>(new RestResponseDto(analysisMap), HttpStatus.OK);
	}

}
