/**
 * 
 * I declare that this code was written by me, 21012014. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: LAI YUEYIN SHYANN
 * Student ID: 21012014
 * Class: E63C
 * Date created: 2022-Nov-27 9:01:07 pm 
 * 
 */

package e63c.Lai.GA;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * @author 21012014
 *
 */
@Controller
public class CategoryController {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@GetMapping("/categories")
	public String viewCategory(Model model) {
		List<Category> categories = categoryRepository.findAll();
		model.addAttribute("listCategory", categories);
		return "view_categories";
	}
	
	@GetMapping("/categories/add")
	public String addCategory(Model model) {
		model.addAttribute("category", new Category());
		return "add_category";
	}
	
	@PostMapping("/categories/save")
	public String saveCategory(@Valid Category category, BindingResult bindingResult, RedirectAttributes redirectAttribute) {
		if (bindingResult.hasErrors()) {
//			System.out.println(bindingResult.getFieldErrorCount());
			return "add_category";
		}
		categoryRepository.save(category);
		redirectAttribute.addFlashAttribute("success","Category added!");
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/edit/{id}")
	public String editCategory(@PathVariable("id") Integer id, Model model) {
		Category category = categoryRepository.getById(id);
		/*  getById ==> Select * from category where id = id   */
		model.addAttribute("category", category);
		return "edit_category";
	}
	
	@PostMapping("/categories/edit/{id}")
	public String saveUpdatedCaetgory(@PathVariable("id") Integer id, Category category, RedirectAttributes redirectAttribute) {
		categoryRepository.save(category);
//		it helps to pass the "category" data to edit_category.html
		redirectAttribute.addFlashAttribute("success","Category edited!");
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/delete/{id}")
	public String editCategory(@PathVariable("id") Integer id, RedirectAttributes redirectAttribute) {
		categoryRepository.deleteById(id);
		redirectAttribute.addFlashAttribute("success","Category deleted!");
		return "redirect:/categories";
	}
}
