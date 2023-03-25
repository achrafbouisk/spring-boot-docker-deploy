package com.ecomteam.shop_dddv3.controllers;

import com.ecomteam.shop_dddv3.domain.models.Banner;
import com.ecomteam.shop_dddv3.infrastructure.services.banners.BannerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/banners")
public class BannerController {
    private final BannerService bannerService;

    public BannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    @GetMapping
    public ResponseEntity<?> getAllBanners() {
        return bannerService.getAllBanners();
    }

    @PostMapping
    public ResponseEntity<String> createBanner(@RequestBody Banner banner) {
        return bannerService.createBanner(banner);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBanner(@PathVariable String id, @RequestBody Banner banner) {
        return bannerService.updateBanner(id, banner);
    }

}





