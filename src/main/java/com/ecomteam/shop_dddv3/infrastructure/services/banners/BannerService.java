package com.ecomteam.shop_dddv3.infrastructure.services.banners;

import com.ecomteam.shop_dddv3.domain.models.Banner;
import org.springframework.http.ResponseEntity;

public interface BannerService {
    ResponseEntity<?> getAllBanners();

    ResponseEntity<String> createBanner(Banner banner);

    ResponseEntity<String> updateBanner(String id, Banner banner);
}
