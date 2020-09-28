package miona.data.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
@Table(name = "TAGS")
public class Tags {

	private Integer id;
	private String tagName;
	private String tagValue;
	@JsonBackReference(value = "tagsLinks")
	private Links link;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "TAG_NAME", nullable = false)
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
	@Column(name = "TAG_VALUE", nullable = false)
	public String getTagValue() {
		return tagValue;
	}
	public void setTagValue(String tagValue) {
		this.tagValue = tagValue;
	}
	@ManyToOne
	@JoinColumn(name = "LINK", nullable = false)
	public Links getLink() {
		return link;
	}
	public void setLink(Links link) {
		this.link = link;
	}
	
	

}
