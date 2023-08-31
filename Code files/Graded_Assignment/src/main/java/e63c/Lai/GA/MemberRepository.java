/**
 * 
 * I declare that this code was written by me, 21012014. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: LAI YUEYIN SHYANN
 * Student ID: 21012014
 * Class: E63C
 * Date created: 2023-Jan-12 3:29:26 pm 
 * 
 */

package e63c.Lai.GA;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 21012014
 *
 */
public interface MemberRepository extends JpaRepository<Member, Integer> {
	
	public Member findByUsername(String username);
	
}