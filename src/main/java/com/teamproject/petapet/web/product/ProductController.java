package com.teamproject.petapet.web.product;

import com.teamproject.petapet.domain.member.Member;
import com.teamproject.petapet.domain.member.MemberRepository;
import com.teamproject.petapet.domain.product.Product;
import com.teamproject.petapet.domain.product.ProductType;
import com.teamproject.petapet.domain.product.Review;
import com.teamproject.petapet.domain.product.ReviewRepository;
import com.teamproject.petapet.web.product.productdtos.ProductDetailDTO;
import com.teamproject.petapet.web.product.productdtos.ProductListDTO;
import com.teamproject.petapet.web.product.productdtos.ReviewInsertDTO;
import com.teamproject.petapet.web.product.reviewdto.ReviewDTO;
import com.teamproject.petapet.web.product.service.ProductService;
import com.teamproject.petapet.web.product.fileupload.FileService;
import com.teamproject.petapet.web.product.fileupload.UploadFile;
import com.teamproject.petapet.web.product.productdtos.ProductInsertDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final FileService fileService;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    @GetMapping
    public String productMainPage() {
        return "/product/productMainPage";
    }

    @GetMapping("/{category}")
    public String productList(@PathVariable("category") String category, Model model) {
        category = category.toUpperCase();
        ProductType productType = ProductType.valueOf(category);
        List<Product> productList = productService.findAllByProductDiv(productType);
        List<ProductListDTO> productListDTOS = productList.stream().map(m -> ProductListDTO.builder().productName(m.getProductName())
                .productPrice(m.getProductPrice())
                .productImg(m.getProductImg())
                .productId(m.getProductId())
                .productDiv(m.getProductDiv())
                .productRating(m.getProductRating())
                .review(m.getReview()).build()).collect(Collectors.toList());
        model.addAttribute("productList", productListDTOS);
        model.addAttribute("productDiv", productType.getProductCategory());
        return "product/productList";
    }

    @GetMapping("/insert")
    public String productInsertForm(@ModelAttribute("ProductInsertDTO") ProductInsertDTO productInsertDTO) {
        return "/product/productInsertForm";
    }

    @PostMapping("/insert")
    public String productInsert(@Validated @ModelAttribute("ProductInsertDTO") ProductInsertDTO productInsertDTO, BindingResult bindingResult) throws IOException {

        if (productInsertDTO.getProductImg().get(0).isEmpty()) {
            bindingResult.addError(new FieldError("productInsertDTO", "productImg", "1장 이상의 사진을 올려주세요"));
        }

        if (bindingResult.hasErrors()) {
            return "/product/productInsertForm";
        }

        List<MultipartFile> productImg = productInsertDTO.getProductImg();
        List<UploadFile> uploadFiles = fileService.storeFiles(productImg);

        ProductType productDiv = ProductType.valueOf(productInsertDTO.getProductDiv());

        Product product = new Product(productInsertDTO.getProductName()
                , productInsertDTO.getProductPrice()
                , productInsertDTO.getProductStock()
                , uploadFiles
                , "판매중"
                , productDiv
                , productInsertDTO.getProductContent());
        productService.productSave(product);

        String redirectURL = "/product/" +
                product.getProductDiv().name().toLowerCase() + "/" +
                product.getProductId() + "/" + "details";

        return "redirect:" + redirectURL;
    }

//    @GetMapping(value = "/Users/oh/Desktop/test/file/{fileName}",produces = MediaType.IMAGE_PNG_VALUE)
//    @ResponseBody
//    public Resource downloadImage(@PathVariable String filename) throws
//            MalformedURLException {
//        return new UrlResource("file:" + fileService.getFullPath(filename));
//    }

    @GetMapping(value = "/images/{filename}")
    public ResponseEntity<Resource> downloadImageV2(@PathVariable String filename) throws IOException {
        String fullPath = fileService.getFullPath(filename);
        MediaType mediaType = MediaType.parseMediaType(Files.probeContentType(Paths.get(fullPath)));
        UrlResource resource = new UrlResource("file:" + fullPath);
        ResponseEntity<Resource> body = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, mediaType.toString())
                .body(resource);
        return body;
    }

    @GetMapping("/{productType}/{productId}/details")
    public String detailViewForm(@PathVariable("productType") String productType
            , @PathVariable("productId") Long productId, Model model) {
        Product findProduct = productService.findOne(productId);
        ProductDetailDTO productDetailDTO = findProduct.toProductDetailDTO(findProduct);
        Sort sort = Sort.by("reviewId").descending();
        Pageable pageable = PageRequest.of(0, 10, sort);
        Slice<Review> reviews = reviewRepository.requestMoreReview(productId, pageable);
        Long countReview = reviewRepository.countReviewByProduct(findProduct);
        model.addAttribute("countReview", countReview);
        model.addAttribute("findProduct", productDetailDTO);
        model.addAttribute("reviews", reviews);
        return "/product/productDetails";
    }


    @PostMapping("/{productId}/reviewInsert")
    public String reviewInsert(@ModelAttribute ReviewInsertDTO reviewInsertDTO,
                               @RequestParam String requestURI,
                               @PathVariable("productId") Long productId) throws IOException {
        List<MultipartFile> reviewImg = reviewInsertDTO.getReviewImg();
        List<UploadFile> uploadFiles = fileService.storeFiles(reviewImg);

        //테스트 유저
        Member member = memberRepository.findOneWithAuthoritiesByMemberId("memberId1").get();

        Review review = Review.builder().reviewTitle(reviewInsertDTO.getReviewTitle())
                .reviewRating(reviewInsertDTO.getReviewRating())
                .reviewContent(reviewInsertDTO.getReviewContent())
                .reviewImg(uploadFiles)
                .reviewDate(LocalDateTime.now())
                .member(member)
                .product(productService.findOne(productId)).build();

        reviewRepository.save(review);

        return "redirect:" + requestURI;
    }
}
