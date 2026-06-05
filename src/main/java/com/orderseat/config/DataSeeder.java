package com.orderseat.config;

import com.orderseat.entity.*;
import com.orderseat.entity.enums.TableStatus;
import com.orderseat.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CoffeeTableRepository coffeeTableRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Chỉ seed khi DB hoàn toàn mới (chưa có Role nào)
        if (roleRepository.count() == 0) {

            // 1. Tạo Roles
            Role adminRole = Role.builder().name("ROLE_ADMIN").build();
            Role customerRole = Role.builder().name("ROLE_CUSTOMER").build();
            roleRepository.saveAll(Set.of(adminRole, customerRole));

            // 2. Tạo Tài khoản Admin và Khách hàng mẫu
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .email("admin@orderseat.com")
                    .fullName("Quản trị viên")
                    .roles(Set.of(adminRole))
                    .build();

            User customer = User.builder()
                    .username("customer")
                    .password(passwordEncoder.encode("123456"))
                    .email("khachhang@gmail.com")
                    .fullName("Khách hàng VIP")
                    .phone("0901234567")
                    .roles(Set.of(customerRole))
                    .build();

            userRepository.saveAll(Set.of(admin, customer));

            // 3. Tạo Category phân cấp 2 tầng
            // --- Tầng 1: Danh mục cha ---
            Category parentDrinks = Category.builder()
                    .name("Đồ Uống")
                    .description("Các loại đồ uống thơm ngon tại OrderSeat Coffee")
                    .build();
            Category parentFood = Category.builder()
                    .name("Đồ Ăn Vặt")
                    .description("Bánh và các món ăn nhẹ ăn kèm")
                    .build();
            // Lưu parent trước để có ID
            categoryRepository.saveAll(List.of(parentDrinks, parentFood));

            // --- Tầng 2: Danh mục con (tham chiếu parent đã có ID) ---
            Category catCoffee = Category.builder()
                    .name("Cà phê")
                    .description("Cà phê pha máy & pha phin đặc trưng")
                    .parent(parentDrinks)
                    .build();
            Category catTea = Category.builder()
                    .name("Trà & Nước ép")
                    .description("Trà thanh mát, nước ép tươi giải nhiệt")
                    .parent(parentDrinks)
                    .build();
            Category catCake = Category.builder()
                    .name("Bánh ngọt")
                    .description("Bánh ngọt cao cấp ăn kèm đồ uống")
                    .parent(parentFood)
                    .build();
            // Lưu children sau khi parent đã có ID
            categoryRepository.saveAll(List.of(catCoffee, catTea, catCake));

            // 4. Tạo Sản phẩm – gán vào subcategory (tầng 2)
            Product p1 = Product.builder()
                    .name("Cà phê sữa đá")
                    .price(35000.0)
                    .category(catCoffee)
                    .imageUrl("/images/iced_coffee.png")
                    .description("Cà phê truyền thống pha phin cùng sữa đặc và đá lạnh.")
                    .build();
            Product p2 = Product.builder()
                    .name("Bạc xỉu")
                    .price(39000.0)
                    .category(catCoffee)
                    .imageUrl("/images/bac_xiu.png")
                    .description("Bạc xỉu đặc trưng – ít cà phê nhiều sữa, béo ngậy.")
                    .build();
            Product p3 = Product.builder()
                    .name("Trà đào cam sả")
                    .price(45000.0)
                    .category(catTea)
                    .imageUrl("/images/peach_tea.png")
                    .description("Trà đào thanh mát kết hợp cam và sả thơm nồng.")
                    .build();
            Product p4 = Product.builder()
                    .name("Trà vải Olong")
                    .price(45000.0)
                    .category(catTea)
                    .imageUrl("/images/lychee_tea.png")
                    .description("Trà Olong cao cấp kết hợp với vải thiều tươi ngọt.")
                    .build();
            Product p5 = Product.builder()
                    .name("Bánh Tiramisu")
                    .price(30000.0)
                    .category(catCake)
                    .imageUrl("/images/tiramisu.png")
                    .description("Bánh Tiramisu Ý thượng hạng với mascarpone và espresso.")
                    .build();
            productRepository.saveAll(List.of(p1, p2, p3, p4, p5));

            // 5. Tạo Sơ đồ Bàn
            CoffeeTable t1 = CoffeeTable.builder().name("Bàn 01 (Tầng 1)").capacity(2).status(TableStatus.AVAILABLE).build();
            CoffeeTable t2 = CoffeeTable.builder().name("Bàn 02 (Tầng 1)").capacity(4).status(TableStatus.AVAILABLE).build();
            CoffeeTable t3 = CoffeeTable.builder().name("Bàn 03 (Tầng 1)").capacity(4).status(TableStatus.AVAILABLE).build();
            CoffeeTable t4 = CoffeeTable.builder().name("Bàn 04 (Tầng 2)").capacity(6).status(TableStatus.AVAILABLE).build();
            CoffeeTable t5 = CoffeeTable.builder().name("Bàn 05 (VIP)").capacity(10).status(TableStatus.AVAILABLE).build();
            coffeeTableRepository.saveAll(List.of(t1, t2, t3, t4, t5));

            System.out.println("====== DỮ LIỆU MẪU ĐÃ ĐƯỢC TẠO THÀNH CÔNG! ======");
            System.out.println("Cấu trúc Category:");
            System.out.println("  [Đồ Uống]");
            System.out.println("    └── Cà phê (Cà phê sữa đá, Bạc xỉu)");
            System.out.println("    └── Trà & Nước ép (Trà đào cam sả, Trà vải Olong)");
            System.out.println("  [Đồ Ăn Vặt]");
            System.out.println("    └── Bánh ngọt (Bánh Tiramisu)");
            System.out.println("Tài khoản Quản trị : admin / admin123");
            System.out.println("Tài khoản Khách hàng: customer / 123456");
        }
    }
}
