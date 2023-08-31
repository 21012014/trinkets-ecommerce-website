package e63c.Lai.GA;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CartItemController {

	@Autowired
	private CartItemRepository cartItemRepo;
	
	@Autowired
	private OrderItemRepository orderRepo;

	@Autowired
	private AccessoryRepository accessoryRepo;

	@Autowired
	private MemberRepository memberRepo;
	
	@Autowired
	private JavaMailSender javaMailSender;

	@GetMapping("/cart")
	public String showCart(Model model, Principal principal) {
		// get currently logged in user
		MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int loggedInMemberId = loggedInMember.getMember().getId();
		
		// get shopping cart items added by this user
		List<CartItem> cartItemList = cartItemRepo.findByMemberId(loggedInMemberId);
		
		// Add the shopping cart items to the model
		model.addAttribute("cartItemList" ,cartItemList);
		
		// Calculate the total cost of all items in the shopping cart
		double cartTotal = 0.0;
		for (int i=0; i<cartItemList.size(); i++) {
			
			CartItem currentCartItem = cartItemList.get(i);
			int itemQuantityInCart = currentCartItem.getQuantity();
			model.addAttribute("memberId", loggedInMemberId);
			
				Accessory item = currentCartItem.getAccessory();
				double itemPrice = item.getPrice();
				
				currentCartItem.setSubTotal((double)Math.round(itemPrice * itemQuantityInCart * 100)/100);
				cartTotal += itemPrice * itemQuantityInCart;
		}
		cartTotal = ((double)Math.round(cartTotal * 100)/100);
		
		// Add the shopping cart total to the model
		model.addAttribute("cartTotal", cartTotal);
		
		return "cart";
	}

	@PostMapping("/cart/add/{accessoryId}")
	public String addToCart(@PathVariable("accessoryId") int accessoryId, @RequestParam("quantity") int quantity,
			Principal principal, RedirectAttributes redirectAttribute) {

		// Get currently logged in user
		MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication().getPrincipal();
		int loggedInMemberId = loggedInMember.getMember().getId();
		
		// Check in the cartItemRepo if item was previously added by user.
		// *Hint: we will need to write a new method in the CartItemRepository
		CartItem cartItem = cartItemRepo.findByMemberIdAndAccessoryId(loggedInMemberId, accessoryId);
		
		// if the item was previously added, then we get the quantity that was
		// previously added and increase that
		// Save the CartItem object back to the repository
		if (cartItem != null) {
			int currentQuantity = cartItem.getQuantity();
			cartItem.setQuantity(quantity + currentQuantity);
			cartItemRepo.save(cartItem);
		} else {
			
		// if the item was NOT previously added,
		// then prepare the item and member objects
			Accessory item = accessoryRepo.getById(accessoryId);
			Member member = memberRepo.getById(loggedInMemberId);
		
		// Create a new CartItem object
			CartItem newCartItem = new CartItem();
		
		// Set the item and member as well as the new quantity in the new CartItem object
			newCartItem.setAccessory(item);
			newCartItem.setMember(member);
			newCartItem.setQuantity(quantity);
		
		// Save the new CartItem object to the repository
			cartItemRepo.save(newCartItem);
		}
		redirectAttribute.addFlashAttribute("success","Accessory added!");
		return "redirect:/";
	}

	@PostMapping("/cart/update/{id}")
	public String updateCart(@PathVariable("id") int cartItemId, @RequestParam("quantity") int quantity, RedirectAttributes redirectAttribute) {

		// Get cartItem object by cartItem's id
		CartItem cartItem = cartItemRepo.getById(cartItemId);

		if (quantity <= 0) {
//			If quantity is 0 or less, remove the cart item from the repository
			cartItemRepo.deleteById(cartItemId);
			return "redirect:/cart";
		      
		} else {
			// Set the quantity of the carItem object retrieved
			cartItem.setQuantity(quantity);
			
			// Save the cartItem back to the cartItemRepo
			cartItemRepo.save(cartItem);
			redirectAttribute.addFlashAttribute("success","Quantity changed!");
			return "redirect:/cart";			
		}
	}

	@GetMapping("/cart/remove/{id}")
	public String removeFromCart(@PathVariable("id") int cartItemId, RedirectAttributes redirectAttribute) {

		//Remove item from cartItemRepo 
		cartItemRepo.deleteById(cartItemId);
		redirectAttribute.addFlashAttribute("success","Accessory deleted!");
		return "redirect:/cart";
	}
	
	@PostMapping("/cart/process_order")
	public String processOrder(Model model, @RequestParam("cartTotal") double cartTotal,
		@RequestParam("memberId") int memberId, @RequestParam("orderId") String orderId,
		@RequestParam("transactionId") String transactionId) {
	    
		// Retrieve cart items purchased	
		List<CartItem> cartItemList = cartItemRepo.findByMemberId(memberId);
	    
		// Get member object	
		Member member = memberRepo.getById(memberId);
	    
		// Making String variable to enter invoice details	
		String details = "";
				
		// Loop to iterate through all cart items
		for (int i = 0; i < cartItemList.size(); i++) {

			// Retrieve details about current cart item	
			CartItem currentCartItem = cartItemList.get(i);
			Accessory itemToUpdate = currentCartItem.getAccessory();
			int quantityOfItemPurchased = currentCartItem.getQuantity();
			int itemToUpdateId = itemToUpdate.getId();
			currentCartItem.setSubTotal(quantityOfItemPurchased * currentCartItem.getAccessory().getPrice());
			
			System.out.println("Accessory: " + itemToUpdate.getDescription());
			
			// Update item table
			Accessory inventoryItem = accessoryRepo.getById(itemToUpdateId);
			int currentInventoryQuantity = inventoryItem.getQuantity();
			System.out.println("Current inventory quantity: " + inventoryItem.getQuantity());
			inventoryItem.setQuantity(currentInventoryQuantity - quantityOfItemPurchased);
			inventoryItem.setSold(inventoryItem.getSold() + quantityOfItemPurchased);
			accessoryRepo.save(inventoryItem);
			
			// Add item to order table
			OrderItem orderItem = new OrderItem();
			orderItem.setOrderId(orderId);
			orderItem.setTransactionId(transactionId);
			orderItem.setAccessory(itemToUpdate);
			orderItem.setMember(member);
			orderItem.setQuantity(quantityOfItemPurchased);
			orderRepo.save(orderItem);
			
			// clear cart items belonging to member
			cartItemRepo.deleteById(currentCartItem.getId());

			// Build String variable
			details += String.format("  %-75s %-20s $%-20s \n", currentCartItem.getAccessory().getName(), currentCartItem.getQuantity(), currentCartItem.getSubTotal());
			
		}
		cartTotal = ((double)Math.round(cartTotal * 100)/100);
		
		// Pass info to view to display success page
		model.addAttribute("cartTotal", cartTotal);
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("member", member);
		model.addAttribute("orderId", orderId);
		model.addAttribute("transactionId", transactionId);
		
		// Send email
		String subject = "Order for radiant accessories is confirmed!";
		String body = String.format("\nINVOICE SUMMARY \n\nThank you for shopping with us at RADIANCE. \n"
				+ "____________________________________________________________________________________\n Customer's Name: %-35s %40s \n"
				+ " Customer's Username: %-33s %-40s \n %101s \n %100s \n____________________________________________________________________________________"
				+ "\n  %-75s %-20s $%-20s  \n"
				+ "____________________________________________________________________________________\n", member.getName(), "Transaction ID: " + transactionId, 
				member.getUsername(), "Order ID: " + orderId, "Payment Term: Paypal", "Total Amount: $" + cartTotal, "Product", "Quantity", "Amount");
		
		String to = member.getEmail();
		sendEmail(to, subject, body+details);

		return "success";
	}
	public void sendEmail(String to, String subject, String body) {
	   SimpleMailMessage msg = new SimpleMailMessage();
	   msg.setTo(to);
	   msg.setSubject(subject);
	   msg.setText(body);
	   System.out.println("Sending");
	   javaMailSender.send(msg);
	   System.out.println("Sent");
	}

	@GetMapping("/purchase_history")
	public String viewHistory(Model model) {
		MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int loggedInMemberId = loggedInMember.getMember().getId();
		List<OrderItem> itemPurchase = orderRepo.findByMemberId(loggedInMemberId);
		model.addAttribute("itemPurchased", itemPurchase);
		
		for (int i=0; i<itemPurchase.size(); i++) {
			
			OrderItem currentItem = itemPurchase.get(i);
			int itemQuantityInCart = currentItem.getQuantity();
			Accessory item = currentItem.getAccessory();
			double itemPrice = item.getPrice();
			currentItem.setSubTotal((double)Math.round(itemPrice * itemQuantityInCart * 100)/100);
		}
		return "purchase_history";
	}
	
	@GetMapping("/member/{id}/purchase_history")
	public String viewUserHistory(@PathVariable("id") Integer id, Model model) {
		Member member = memberRepo.getById(id);
		List<OrderItem> itemPurchase = orderRepo.findByMemberId(id);
		model.addAttribute("itemPurchased", itemPurchase);
		
		for (int i=0; i<itemPurchase.size(); i++) {
			
			OrderItem currentItem = itemPurchase.get(i);
			int itemQuantityInCart = currentItem.getQuantity();
			Accessory item = currentItem.getAccessory();
			double itemPrice = item.getPrice();
			currentItem.setSubTotal((double)Math.round(itemPrice * itemQuantityInCart * 100)/100);
		}
		model.addAttribute("member", member);
		
		return "user_purchase_history";
	}

}
