package ru.skypro.homework.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.utils.MyMapper;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class AdsService {
    private static Logger logger = LoggerFactory.getLogger(AdsService.class);
    private final MyMapper mapper;
    private final AdRepository adRepository;

    public AdDto addAd(CreateOrUpdateAd createOrUpdateAd) {
        logger.info("Ad was successfully added: {}", createOrUpdateAd.getTitle());
        return new AdDto(); // Temp
    }

    public Ads getAllAds() {
        List<AdDto> allAd = adRepository.findAll().stream().map(a -> mapper.map(a)).collect(Collectors.toList());
        Ads ads = new Ads();
        ads.setCount(allAd.size());
        ads.setResults(allAd);
        logger.info("Number of ads sent: {}", ads.getCount());
        return ads;
    }

}
