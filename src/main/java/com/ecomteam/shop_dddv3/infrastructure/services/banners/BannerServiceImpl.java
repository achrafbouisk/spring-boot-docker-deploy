package com.ecomteam.shop_dddv3.infrastructure.services.banners;

import com.ecomteam.shop_dddv3.domain.models.Banner;
import com.ecomteam.shop_dddv3.domain.repositories.BannerRepository;
import com.ecomteam.shop_dddv3.infrastructure.errors.ErrorMessage;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {
    
    private final BannerRepository bannerRepository;
    private final ErrorMessage errorMessage;


    @Override
    public ResponseEntity<?> getAllBanners() {
        List<Banner> banner = bannerRepository.findAll();
        if(!banner.isEmpty())
            return new ResponseEntity<>(banner, HttpStatus.OK);
        else {
            errorMessage.setMessage("Banner Not Found");
            return new ResponseEntity<>(errorMessage.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> createBanner(Banner banner) {
        Banner banner1 = bannerRepository.save(banner);
        if(banner1 != null)
            return new ResponseEntity<>("Banner has been saved successfully",HttpStatus.CREATED);
        else {
            errorMessage.setMessage("Error while saving banner");
            return new ResponseEntity<>(errorMessage.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> updateBanner(String id, Banner banner) {
        Optional<Banner> exist = bannerRepository.findById(id);
        if(exist.isPresent()){
            Banner req = exist.get();
            req.setActive(banner.isActive());
            req.setTitle(banner.getTitle());
            req.setLink(banner.getLink());
            req.setImages(banner.getImages());
            bannerRepository.save(req);
            return new ResponseEntity<>("Banner has been updated successfully",HttpStatus.ACCEPTED);
        }
        else {
            errorMessage.setMessage("Error while updating banner");
            return new ResponseEntity<>(errorMessage.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
