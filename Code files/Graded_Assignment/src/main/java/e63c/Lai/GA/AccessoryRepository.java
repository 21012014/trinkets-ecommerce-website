/**
 * 
 * I declare that this code was written by me, 21012014. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: LAI YUEYIN SHYANN
 * Student ID: 21012014
 * Class: E63C
 * Date created: 2022-Nov-10 5:37:35 pm 
 * 
 */

package e63c.Lai.GA;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author 21012014
 *
 */
public interface AccessoryRepository extends JpaRepository<Accessory, Integer>{

	@Query("SELECT a.name, a.sold FROM Accessory a")
    List<Object[]> findAccessorySoldCounts();
}
