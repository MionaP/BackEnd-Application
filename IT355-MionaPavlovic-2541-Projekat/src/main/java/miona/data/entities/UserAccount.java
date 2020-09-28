package miona.data.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.userdetails.UserDetails;


@Entity
@Table(name = "USER_ACCOUNT")
public class UserAccount implements UserDetails{

	private static final long serialVersionUID = -7729558353351529823L;
	
	private Integer id;
	private String username;
	private String password;
	private Roles roles;
	private String email;
	private boolean enabled = false;
	private String mailHash;
	private Long mailHashTimestamp;
	@Transient
	private boolean nonLocked = false;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "USERNAME", nullable = false)
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(name = "PASSWORD", nullable = false)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@ManyToOne
	@JoinColumn(name = "ROLE", nullable = false)
	public Roles getRoles() {
		return roles;
	}
	
	public void setRoles(Roles roles) {
		this.roles = roles;
	}
	
	@Column(name = "EMAIL", nullable = false)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Transient
	public boolean isNonLocked() {
		return nonLocked;
	}
	@Transient
	public void setNonLocked(boolean nonLocked) {
		this.nonLocked = nonLocked;
	}
	
	@Transient
	@Override
	public List<Roles> getAuthorities() {
		List<Roles> listRoles = new ArrayList<Roles>();
		listRoles.add(this.roles);
		return listRoles;
	}
	
	@Transient
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Transient
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Transient
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Column(name = "ENABLED", nullable = false)
	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	@Column(name = "MAIL_HASH", nullable = true)
	public String getMailHash() {
		return mailHash;
	}
	public void setMailHash(String mailHash) {
		this.mailHash = mailHash;
	}
	
	@Column(name = "MAIL_HASH_TIMESTAMP", nullable = true)
	public Long getMailHashTimestamp() {
		return mailHashTimestamp;
	}
	public void setMailHashTimestamp(Long mailHashTimestamp) {
		this.mailHashTimestamp = mailHashTimestamp;
	}
	
}
