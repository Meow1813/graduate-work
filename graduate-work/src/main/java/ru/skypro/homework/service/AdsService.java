package ru.skypro.homework.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.skypro.homework.controller.AdsController;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class AdsService {
    private static Logger logger = LoggerFactory.getLogger(AdsService.class);

    private List<Ad> testAds = new ArrayList<>(); //Replace with Ads repo

    public Ad addNewAd(CreateOrUpdateAd createOrUpdateAd) {
        logger.info("Ad was successfully added: {}", createOrUpdateAd.getTitle());
        return new Ad();
    }

    public Ads getAllAds() {
        if (testAds.isEmpty()) {
            Ad ad = new Ad();
            ad.setTitle("Title 1");
            ad.setPrice(3333);
            ad.setTitle("Title 1");
            ad.setPk(555);
            ad.setAuthor(111);
            testAds.add(ad);
        }
        Ads ads = new Ads();//Replace with mapper
        ads.setCount(testAds.size());
        ads.setResults(testAds);
        logger.info("Number of ads sent: {}", ads.getCount());
        return ads;
    }

}
