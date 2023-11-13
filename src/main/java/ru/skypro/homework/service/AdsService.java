package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exceptions.UserAccessDeniedException;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.utils.MyMapper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class AdsService {
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
        log.debug("Ad was successfully added: {}", createOrUpdateAd.getTitle());

        return mapper.map(adEntity);
    }

    public Ads getAllAds() {
        List<AdEntity> entities = adRepository.findAll();
        log.debug("Number of ads sent: {}", entities.size());
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userRepository.findByUsername(authentication.getName());

        AdEntity adEntity = adRepository.getReferenceById(id);
        if (adEntity == null) {
            return null;
        }


        String path = adEntity.getImage();
        if (path != null) {
            File file = new File(path);
            file.delete();
        }

        if (userEntity.getRole() == Role.ROLE_ADMIN || userEntity.getId() == adEntity.getAuthor().getId()) {
            adRepository.deleteById(id);
            return adEntity;
        } else {
            throw new UserAccessDeniedException();
        }
    }

    public AdDto updateImage(Integer id, MultipartFile image) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userRepository.findByUsername(authentication.getName());

        AdEntity adEntity = adRepository.getReferenceById(id);
        if (adEntity == null) {
            return null;
        }

        if (userEntity.getRole() == Role.ROLE_ADMIN || userEntity.getId() == adEntity.getAuthor().getId()) {
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
            log.debug("Image updated successfully: {}", adEntity.getImage());

            return mapper.map(adEntity);
        } else {
            throw new UserAccessDeniedException();
        }

    }

    public AdDto patchAd(Integer id, AdUpdateDto adUpdateDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userRepository.findByUsername(authentication.getName());

        AdEntity adEntity = adRepository.getReferenceById(id);
        if (adEntity == null) {
            return null;
        }

        if (userEntity.getRole() == Role.ROLE_ADMIN || userEntity.getId() == adEntity.getAuthor().getId()) {
            adEntity.setTitle(adUpdateDto.getTitle());
            adEntity.setPrice(adUpdateDto.getPrice());
            adEntity.setDescription(adUpdateDto.getDescription());
            adRepository.save(adEntity);
            log.debug("Ad updated successfully: {}", adEntity.getTitle());
            return mapper.map(adEntity);
        } else {
            throw new UserAccessDeniedException();
        }
    }

    public Ads getAdsMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userRepository.findByUsername(authentication.getName());
        List<AdEntity> entities = adRepository.findAllByAuthor(userEntity);
        log.debug("Number of ads sent: {}", entities.size());
        return mapper.map(entities);

    }

    private String getExtension(String fileName) {
        log.debug("Method getExtension is called, argument(s) passed: {}", fileName);
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}
