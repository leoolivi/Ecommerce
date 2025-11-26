package com.ecommerce.main.services;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
public class ProductServiceTest {

    /* @Autowired
    private ProductService productService;
    
    @Test
    public void shouldAddProductWork() throws Exception {
        var mockRepo = Mockito.mock(ProductRepository.class);
        var mockService = new ProductService(mockRepo);

        var mockProduct = Product.builder()
                                .setName("Prova")
                                .setDescription("descrizione")
                                .setPrice((float) 10.99)
                                .setStockQuantity(10)
                                .setCategory("cat1")
                                .build();
        mockService.addProduct(mockProduct)
    } */
}
