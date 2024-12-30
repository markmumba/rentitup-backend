package com.markian.rentitup.Config;

import com.markian.rentitup.Category.Category;
import com.markian.rentitup.Category.CategoryRepository;
import com.markian.rentitup.Category.PriceCalculationType;
import com.markian.rentitup.User.Role;
import com.markian.rentitup.User.User;
import com.markian.rentitup.User.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public DataSeeder(CategoryRepository categoryRepository, PasswordEncoder passwordEncoder, UserRepository userRepository, UserRepository userRepository1) {
        this.categoryRepository = categoryRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository1;
    }

    @Override
    public void run (String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            seedCategories();
        }
        if (userRepository.count() == 0 ) {
            seedUser();
        }
    }

    private void seedUser() {

        User admin = User.builder()
                .email("admin@gmail.com")
                .createdAt(LocalDateTime.now())
                .fullName("Admin User")
                .password(passwordEncoder.encode("qwerty1234"))
                .updatedAt(LocalDateTime.now())
                .phone("0789612925")
                .role(Role.ADMIN)
                .verified(true)
                .build();


             User owner = User.builder()
                .email("markmumba01@gmail.com")
                .createdAt(LocalDateTime.now())
                .fullName("Owner user")
                .password(passwordEncoder.encode("qwerty1234"))
                .updatedAt(LocalDateTime.now())
                .phone("0789612925")
                .role(Role.OWNER)
                .verified(true)
                .build();

        User customer = User.builder()
                .email("markian.mwangi@riarauniversity.ac.ke")
                .createdAt(LocalDateTime.now())
                .fullName("Owner user")
                .password(passwordEncoder.encode("qwerty1234"))
                .updatedAt(LocalDateTime.now())
                .phone("0789612925")
                .role(Role.OWNER)
                .verified(true)
                .build();


        userRepository.saveAll(Arrays.asList(owner,admin,customer));
        System.out.println("User created successfully");
    }


    private void seedCategories() {
        Category category1 = Category.builder()
                .name("Material Handling Equipment")
                .description("Machinery specifically designed to transport, load, and store materials, ideal for warehouses, construction sites, and manufacturing facilities. Examples include forklifts for lifting heavy loads, pallet jacks for moving pallets, and hand trucks for smaller items.")
                .priceCalculationType(PriceCalculationType.HOURLY)
                .build();

        Category category2 = Category.builder()
                .name("Earth Moving Equipment")
                .description("Heavy-duty machines used for excavating, grading, and compacting soil, often in construction and landscaping projects. Examples include excavators for digging, bulldozers for leveling terrain, and skid-steer loaders for versatile lifting and moving tasks.")
                .priceCalculationType(PriceCalculationType.DAILY)
                .build();

        Category category3 = Category.builder()
                .name("Lawn & Garden Equipment")
                .description("Small-scale machinery tailored for garden maintenance and landscaping, perfect for homeowners and gardeners. Examples include lawn mowers for trimming grass, leaf blowers for clearing leaves and debris, and hedge trimmers for pruning bushes and hedges.")
                .priceCalculationType(PriceCalculationType.HOURLY)
                .build();

        Category category4 = Category.builder()
                .name("Concrete & Masonry Equipment")
                .description("Tools and machines designed to work with concrete and masonry, essential for paving, construction, and home improvement projects. Examples include cement mixers for mixing concrete, concrete saws for cutting concrete slabs, and mortar mixers for combining mortar materials.")
                .priceCalculationType(PriceCalculationType.DAILY)
                .build();

        Category category5 = Category.builder()
                .name("Power Tools")
                .description("Handheld and portable tools that increase efficiency in a variety of tasks, including woodworking, metalworking, and construction. Examples include drills for making holes, angle grinders for cutting and grinding, and electric saws for precise cutting.")
                .priceCalculationType(PriceCalculationType.HOURLY)
                .build();

        Category category6 = Category.builder()
                .name("Lifting & Hoisting Equipment")
                .description("Machinery designed for lifting and moving heavy objects in construction, manufacturing, and warehousing. Examples include cranes for lifting and moving heavy loads, hoists for smaller items, and jacks for car repairs or minor lifting tasks.")
                .priceCalculationType(PriceCalculationType.DISTANCE_BASED)
                .build();

        Category category7 = Category.builder()
                .name("Compaction Equipment")
                .description("Equipment used to compress soil, gravel, or asphalt, making surfaces stable for construction projects like road building and foundations. Examples include plate compactors for smaller areas, rollers for large surface areas, and rammers for tight spaces.")
                .priceCalculationType(PriceCalculationType.DAILY)
                .build();

        Category category8 = Category.builder()
                .name("Road Construction Equipment")
                .description("Heavy machinery suited for building and maintaining roads, ideal for large construction projects. Examples include asphalt pavers for laying asphalt, road rollers for compacting surfaces, and concrete curb machines for creating road borders.")
                .priceCalculationType(PriceCalculationType.DISTANCE_BASED)
                .build();

        categoryRepository.saveAll(List.of(
                category1, category2, category3, category4,
                category5, category6, category7, category8
        ));
    }
}
