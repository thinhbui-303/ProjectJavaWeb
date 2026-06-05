package com.orderseat.controller;

import com.orderseat.dto.CartItem;
import com.orderseat.entity.Product;
import com.orderseat.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final ProductService productService;

    @SuppressWarnings("unchecked")
    private Map<Long, CartItem> getCart(HttpSession session) {
        Map<Long, CartItem> cart = (Map<Long, CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        Map<Long, CartItem> cart = getCart(session);
        double total = cart.values().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        model.addAttribute("cartItems", cart.values());
        model.addAttribute("totalPrice", total);
        return "cart";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addToCart(@RequestParam("productId") Long productId,
                                       @RequestParam(value = "quantity", defaultValue = "1") Integer quantity,
                                       HttpSession session) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.badRequest().body("Sản phẩm không tồn tại");
        }

        Map<Long, CartItem> cart = getCart(session);
        if (cart.containsKey(productId)) {
            CartItem item = cart.get(productId);
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            cart.put(productId, new CartItem(productId, product.getName(), product.getPrice(), quantity));
        }

        return ResponseEntity.ok().body("Đã thêm vào giỏ hàng");
    }

    @PostMapping("/update")
    public String updateCart(@RequestParam("productId") Long productId,
                             @RequestParam("quantity") Integer quantity,
                             HttpSession session) {
        Map<Long, CartItem> cart = getCart(session);
        if (cart.containsKey(productId)) {
            if (quantity > 0) {
                cart.get(productId).setQuantity(quantity);
            } else {
                cart.remove(productId);
            }
        }
        return "redirect:/cart";
    }

    @GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable("productId") Long productId, HttpSession session) {
        Map<Long, CartItem> cart = getCart(session);
        cart.remove(productId);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout() {
        // Nêu chưa đăng nhập, Spring Security sẽ tự động chuyển hướng về /login
        // Vì /cart/checkout đã được yêu cầu quyền CUSTOMER trong SecurityConfig!
        // Sau khi đăng nhập xong, có thể điều hướng về trang đặt bàn để tiếp tục
        return "redirect:/customer/booking";
    }
}
