/**
 * 
 * I declare that this code was written by me, 21012014. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: LAI YUEYIN SHYANN
 * Student ID: 21012014
 * Class: E63C
 * Date created: 2023-Jan-12 7:13:41 pm 
 * 
 */

package e63c.Lai.GA;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * @author 21012014
 *
 */
public class MemberDetails implements UserDetails {
	
	private Member member;
	
	public Member getMember() {
		return member;
	}
	
	public MemberDetails(Member member) {
		this.member = member;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities(){
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(member.getRole());
		return Arrays.asList(authority);
	}
	@Override
	public String getPassword() {
		return member.getPassword();
	}
	@Override
	public String getUsername() {
		return member.getUsername();
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
}