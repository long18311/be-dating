package com.example.datingbe.rest;

import com.cloudinary.Cloudinary;
import com.example.datingbe.entity.Product;
import com.example.datingbe.repository.ImageProductRepository;
import com.example.datingbe.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProductResource {
    @Autowired
    ProductService productService;
    @Autowired
    ImageProductRepository imgProductRepository;

    private Cloudinary cloudinary;
    public ProductResource(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @GetMapping("/get-products")
    public ResponseEntity<List<Product>> getAllProduct () throws URISyntaxException{
        List<Product> products = productService.getAllProducts();
        System.out.println(products);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/get-productpages")
    public ResponseEntity<Page<Product>> getAllProductPages (@PageableDefault Pageable pageable) throws URISyntaxException{
        Page<Product> productpages = productService.getAllProductPages(pageable);
        return ResponseEntity.ok(productpages);
    }


    @PostMapping("/admin/create-product")
    public ResponseEntity<Product> save (
            @RequestParam("name") String name,
            @RequestParam("status") String status,
            @RequestParam("avatar") List<MultipartFile> avatars

    ) throws URISyntaxException {
        try{
//            if (name == null || name.isEmpty() || avatars.isEmpty()) {
//                return ResponseEntity.badRequest().build();
//            } else{
//                List<Images_product> imageUrls = new ArrayList<>();
//
//                for (MultipartFile avatar : avatars) {
//                    Map uploadResult = cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.emptyMap());
//                    String imageUrl = (String) uploadResult.get("url");
//                    imageUrls.add(new Images_product(imageUrl));
//                }
//                Product product = new Product();
//                product.setName(name);
//                product.setActive(Integer.parseInt(status));
//                product.setPrice(100.1);
//                product.setImagesProducts(imageUrls);
//                productService.Save(product);
//               return ResponseEntity.ok(product);
//            }
//            Images_product img1 = new Images_product();
//            img1.setUrl("testUrl1");
//            Images_product img2 = new Images_product();
//            img2.setUrl("testUrl2");
//            Images_product img3 = new Images_product();
//            img3.setUrl("testUrl3");
//            List<Images_product> imgs = new ArrayList<>();
//            imgs.add(img1);
//            imgs.add(img2);
//            imgs.add(img3);
//            Product pr = new Product();
//            pr.setImagesProducts(imgs);
//            img1.setProduct(pr);
//            img2.setProduct(pr);
//            img3.setProduct(pr);
//            pr.setName("Test Success");
//            productService.Save(pr);

            System.out.println(name);
            System.out.println(status);
            System.out.println(avatars.toString());

            return null;
        }catch (
        Exception e
        ){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}
