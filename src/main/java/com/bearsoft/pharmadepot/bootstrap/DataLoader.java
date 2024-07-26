package com.bearsoft.pharmadepot.bootstrap;

import com.bearsoft.pharmadepot.models.domain.entities.Category;
import com.bearsoft.pharmadepot.models.domain.entities.Product;
import com.bearsoft.pharmadepot.models.domain.enums.MedType;
import com.bearsoft.pharmadepot.repositories.CategoryRepository;
import com.bearsoft.pharmadepot.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Profile("data-loader")
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public DataLoader(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Optional<Category> analgesicOpt = categoryRepository.findByMedType(MedType.ANALGESIC);
        Optional<Category> antibioticOpt = categoryRepository.findByMedType(MedType.ANTIBIOTIC);
        Optional<Category> vitaminOpt = categoryRepository.findByMedType(MedType.VITAMIN);

        if (!analgesicOpt.isPresent() || !antibioticOpt.isPresent() || !vitaminOpt.isPresent()) {
            throw new IllegalStateException("Required categories are missing from the database");
        }

        Category analgesic = analgesicOpt.get();
        Category antibiotic = antibioticOpt.get();
        Category vitamin = vitaminOpt.get();

        List<Product> products = Arrays.asList(

                Product.builder().name("Aspirin").price(new BigDecimal("5.00")).category(analgesic).build(),
                Product.builder().name("Ibuprofen").price(new BigDecimal("10.00")).category(analgesic).build(),
                Product.builder().name("Paracetamol").price(new BigDecimal("7.50")).category(analgesic).build(),
                Product.builder().name("Naproxen").price(new BigDecimal("12.00")).category(analgesic).build(),
                Product.builder().name("Diclofenac").price(new BigDecimal("8.50")).category(analgesic).build(),
                Product.builder().name("Ketoprofen").price(new BigDecimal("14.00")).category(analgesic).build(),
                Product.builder().name("Codeine").price(new BigDecimal("20.00")).category(analgesic).build(),
                Product.builder().name("Tramadol").price(new BigDecimal("22.00")).category(analgesic).build(),
                Product.builder().name("Morphine").price(new BigDecimal("50.00")).category(analgesic).build(),
                Product.builder().name("Acetaminophen").price(new BigDecimal("5.50")).category(analgesic).build(),

                Product.builder().name("Amoxicillin").price(new BigDecimal("15.00")).category(antibiotic).build(),
                Product.builder().name("Ciprofloxacin").price(new BigDecimal("20.00")).category(antibiotic).build(),
                Product.builder().name("Azithromycin").price(new BigDecimal("25.00")).category(antibiotic).build(),
                Product.builder().name("Doxycycline").price(new BigDecimal("18.00")).category(antibiotic).build(),
                Product.builder().name("Cephalexin").price(new BigDecimal("17.50")).category(antibiotic).build(),
                Product.builder().name("Clindamycin").price(new BigDecimal("23.00")).category(antibiotic).build(),
                Product.builder().name("Metronidazole").price(new BigDecimal("16.00")).category(antibiotic).build(),
                Product.builder().name("Erythromycin").price(new BigDecimal("22.00")).category(antibiotic).build(),
                Product.builder().name("Levofloxacin").price(new BigDecimal("19.00")).category(antibiotic).build(),
                Product.builder().name("Penicillin").price(new BigDecimal("10.00")).category(antibiotic).build(),

                Product.builder().name("Vitamin A").price(new BigDecimal("8.00")).category(vitamin).build(),
                Product.builder().name("Vitamin B").price(new BigDecimal("9.00")).category(vitamin).build(),
                Product.builder().name("Vitamin C").price(new BigDecimal("12.00")).category(vitamin).build(),
                Product.builder().name("Vitamin D").price(new BigDecimal("10.00")).category(vitamin).build(),
                Product.builder().name("Vitamin E").price(new BigDecimal("11.00")).category(vitamin).build(),
                Product.builder().name("Vitamin K").price(new BigDecimal("14.00")).category(vitamin).build(),
                Product.builder().name("Vitamin B12").price(new BigDecimal("13.00")).category(vitamin).build(),
                Product.builder().name("Folic Acid").price(new BigDecimal("9.50")).category(vitamin).build(),
                Product.builder().name("Biotin").price(new BigDecimal("15.00")).category(vitamin).build(),
                Product.builder().name("Niacin").price(new BigDecimal("12.50")).category(vitamin).build()
        );

        productRepository.saveAll(products);
    }
}
