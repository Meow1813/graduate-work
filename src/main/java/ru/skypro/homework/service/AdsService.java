package ru.skypro.homework.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.utils.MyMapper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdsService {
    private static Logger logger = LoggerFactory.getLogger(AdsService.class);
    private final MyMapper mapper;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    @Value("${ads.image.dir.path}")
    private String imageDir;

    public AdDto addAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userRepository.findByUsername(authentication.getName());

        Path filePath = Path.of(imageDir, UUID.randomUUID().toString() + "." + getExtension(image.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        Files.write(filePath,image.getBytes());

        AdEntity adEntity = adRepository.save(mapper.map(createOrUpdateAd, userEntity, filePath.toString()));
        logger.info("Ad was successfully added: {}", createOrUpdateAd.getTitle());

        return mapper.map(adEntity);
    }

    public Ads getAllAds() {
        List<AdEntity> entities = adRepository.findAll();
        logger.info("Number of ads sent: {}", entities.size());
        return mapper.map(entities);
    }

    public AdInfoDto getAdInfo(Integer id) {
        AdEntity adEntity = adRepository.getReferenceById(id);
        if (adEntity == null) {
            return null;
        }
        return mapper.map(adEntity,true);
    }

    public AdEntity deleteAd(Integer id) {
        AdEntity adEntity = adRepository.getReferenceById(id);
        if (adEntity == null) {
            return null;
        }
        String path = adEntity.getImage();
        if (path != null) {
            File file = new File(path);
            file.delete();
        }
        adRepository.deleteById(id);
        return adEntity;
    }

    public AdDto updateImage(Integer id, MultipartFile image) throws IOException {
        AdEntity adEntity = adRepository.getReferenceById(id);
        if (adEntity == null) {
            return null;
        }

        Path filePath = Path.of(imageDir, UUID.randomUUID().toString() + "." + getExtension(image.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        Files.write(filePath,image.getBytes());

        String path = adEntity.getImage();
        if (path != null) {
            File file = new File(path);
            file.delete();
        }

        adEntity.setImage(filePath.toString());
        adRepository.save(adEntity);
        logger.info("Image updated successfully: {}", adEntity.getImage());

        return mapper.map(adEntity);
    }

    public AdDto patchAd(Integer id, AdUpdateDto adUpdateDto) {
        AdEntity adEntity = adRepository.getReferenceById(id);
        if (adEntity == null) {
            return null;
        }
        adEntity.setTitle(adUpdateDto.getTitle());
        adEntity.setPrice(adUpdateDto.getPrice());
        adEntity.setDescription(adUpdateDto.getDescription());
        adRepository.save(adEntity);
        logger.info("Ad updated successfully: {}", adEntity.getTitle());
        return mapper.map(adEntity);
    }

    public Ads getAdsMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userRepository.findByUsername(authentication.getName());
        List<AdEntity> entities = adRepository.findAllByAuthor(userEntity.getId());
        logger.info("Number of ads sent: {}", entities.size());
        return mapper.map(entities);

    }

    private String getExtension(String fileName) {
        logger.debug("Method getExtension is called, argument(s) passed: {}", fileName);
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}
