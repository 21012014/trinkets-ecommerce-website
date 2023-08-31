/**
 * 
 * I declare that this code was written by me, 21012014. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: LAI YUEYIN SHYANN
 * Student ID: 21012014
 * Class: E63C
 * Date created: 2022-Nov-03 2:07:41 pm 
 * 
 */

package e63c.Lai.GA;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author 21012014
 *
 */
@Controller
public class AccessoryController {

	@Autowired
	private AccessoryRepository AccessoryRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@GetMapping("/")
	public String view(Model model) {
		List<Accessory> accessory = AccessoryRepository.findAll();
		model.addAttribute("listAccessory", accessory);
		return "home";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/403")
	public String error403() {
		return "403";
	}
	
	@GetMapping("/accessories")
	public String ga(Model model) {
		List<Accessory> accessory = AccessoryRepository.findAll();
		model.addAttribute("listAccessory", accessory);
		return "view_accessory";
	}
	
	@GetMapping("/accessories/add")
	public String addAccessory(Model model) {
		model.addAttribute("accessory", new Accessory());
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categoryList", categoryList);
		return "add_accessory";
	}
	
	@PostMapping("/accessories/save")
	public String saveAccessory(Accessory accessory, @RequestParam("itemImage") MultipartFile imgFile, RedirectAttributes redirectAttribute){
		// obtain the image name from the file uploaded
		String imageName = imgFile.getOriginalFilename();
		accessory.setImgName(imageName);
		Accessory savedItem = AccessoryRepository.save(accessory);
		
		// upload the file to the server
		try {
			//create a directory with the item id
			String uploadDir = "uploads/items/" + savedItem.getId();
			
			// tries to get the directory path
			Path uploadPath = Paths.get(uploadDir);
			System.out.println("Directory Path:" + uploadPath);
			
			// if the path does not exist
			if (!Files.exists(uploadPath)) {
				// creates the directory
				Files.createDirectories(uploadPath);
			}
			// prepare the path for file
			Path fileToCreatePath= uploadPath.resolve(imageName);
			System.out.println("File path: " + fileToCreatePath);
			
			// copy the file to the upload location.
			Files.copy(imgFile.getInputStream(), fileToCreatePath, StandardCopyOption.REPLACE_EXISTING);
			
		} catch (IOException io) {
			io.printStackTrace();
		}
		redirectAttribute.addFlashAttribute("success","Item added!");
		return "redirect:/accessories";
	}
	
	@GetMapping("/accessories/{id}")
	public String viewSingleItem(@PathVariable("id") Integer id, Model model) {
		Accessory accessory = AccessoryRepository.getById(id);
//		select * from item where item.id = id
		model.addAttribute("accessory", accessory);
		return "view_single_accessory";
	}
	
	@GetMapping("/accessories/edit/{id}")
	public String editItem(@PathVariable("id") Integer id, Model model) {
		Accessory accessory = AccessoryRepository.getById(id);
//		select * from item where item.id = id
		model.addAttribute("accessory", accessory);
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categoryList", categoryList);
		return "edit_accessory";
	}
	
	@PostMapping("/accessories/edit/{id}")
	public String saveUpdatedItems(@PathVariable("id") Integer id, Accessory accessory, @RequestParam("itemImage") MultipartFile imgFile, RedirectAttributes redirectAttribute) {
		String imageName = imgFile.getOriginalFilename();

		accessory.setImgName(imageName);

		Accessory savedItem = AccessoryRepository.save(accessory);

		try {
			String uploadDir = "uploads/items/" + savedItem.getId();
			Path uploadPath = Paths.get(uploadDir);
			System.out.println("Directory path: " + uploadPath);

			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			Path fileToCreatePath = uploadPath.resolve(imageName);
			System.out.println("File path: " + fileToCreatePath);

			Files.copy(imgFile.getInputStream(), fileToCreatePath, StandardCopyOption.REPLACE_EXISTING);

		} catch (Exception io) {
			io.printStackTrace();
		}
		redirectAttribute.addFlashAttribute("success","Item details changed!");
		return "redirect:/accessories";
	}
	
	@GetMapping("/accessories/delete/{id}")
	public String editCategory(@PathVariable("id") Integer id, RedirectAttributes redirectAttribute) {
		AccessoryRepository.deleteById(id);
		redirectAttribute.addFlashAttribute("success","Item deleted!");
		return "redirect:/accessories";
	}
}
