package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
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
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
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

    /**
     * method to create new ad and save it to DB
     * @return AdDto with information
     */
    public AdDto addAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userRepository.findByUsername(authentication.getName());
        String fileName = UUID.randomUUID().toString() + "." + getExtension(image.getOriginalFilename());
        Path filePath = Path.of(imageDir, fileName);
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        Files.write(filePath,image.getBytes());

        AdEntity adEntity = adRepository.save(mapper.map(createOrUpdateAd, userEntity, fileName));
        log.debug("Ad was successfully added: {}", createOrUpdateAd.getTitle());

        return mapper.map(adEntity);
    }

    /**
     * method to get all existing ads currently stored in DB
     * @return Ads, a collective object that holds information about all ads
     */
    public Ads getAllAds() {
        List<AdEntity> entities = adRepository.findAll();
        log.debug("Number of ads sent: {}", entities.size());
        return mapper.map(entities);
    }

    /**
     * method to get info about an ad by its id
     * @return AdInfoDto with information
     */

    public AdInfoDto getAdInfo(Integer id) {
        AdEntity adEntity = adRepository.getReferenceById(id);
        if (adEntity == null) {
            return null;
        }
            return mapper.map(adEntity,true);
    }

    /**
     * method to delete an ad by its id number
     * @return AdEntity
     */

    public AdEntity deleteAd(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userRepository.findByUsername(authentication.getName());

        Optional<AdEntity> ad = adRepository.findById(id);
        if (ad.isEmpty()) {
            return null;
        }

        AdEntity adEntity = ad.get();
        String path = imageDir + "\\" + adEntity.getImage();
        if (adEntity.getImage() != null) {
            File file = new File(path);
            file.delete();
        }

        if (userEntity.getRole() == Role.ADMIN || userEntity.getId() == adEntity.getAuthor().getId()) {
            adRepository.deleteById(id);
            return adEntity;
        } else {
            throw new UserAccessDeniedException();
        }
    }

    /**
     * method for updating an image for a particular ad
     * @return AdDto with information
     */

    public AdDto updateImage(Integer id, MultipartFile image) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userRepository.findByUsername(authentication.getName());

        AdEntity adEntity = adRepository.getReferenceById(id);
        if (adEntity == null) {
            return null;
        }

        if (userEntity.getRole() == Role.ADMIN || userEntity.getId() == adEntity.getAuthor().getId()) {
            String fileName = UUID.randomUUID().toString() + "." + getExtension(image.getOriginalFilename());
            Path filePath = Path.of(imageDir, fileName);
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);

            Files.write(filePath,image.getBytes());

            String path = imageDir + "\\" + adEntity.getImage();
            if (path != null) {
                File file = new File(path);
                file.delete();
            }

            adEntity.setImage(fileName);
            adRepository.save(adEntity);
            log.debug("Image updated successfully: {}", adEntity.getImage());

            return mapper.map(adEntity);
        } else {
            throw new UserAccessDeniedException();
        }

    }

    /**
     * method to update a particular ad
     * @return AdDto with information
     */
    public AdDto patchAd(Integer id, AdUpdateDto adUpdateDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userRepository.findByUsername(authentication.getName());

        AdEntity adEntity = adRepository.getReferenceById(id);
        if (adEntity == null) {
            return null;
        }

        if (userEntity.getRole() == Role.ADMIN || userEntity.getId() == adEntity.getAuthor().getId()) {
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

    /**
     * method to get all ads of a currently authorized user
     * @return AdsDto with information
     */
    public Ads getAdsMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userRepository.findByUsername(authentication.getName());
        List<AdEntity> entities = adRepository.findAllByAuthor(userEntity);
        log.debug("Number of ads sent: {}", entities.size());
        return mapper.map(entities);

    }

    /**
     * method to get an image by its id
     * @return byte[], an actual image
     */

    public byte[] getImage(String id) throws IOException {
        Path path = Path.of(imageDir, id);
        return Files.readAllBytes(path);
    }

    /**
     * utility method to get an extension of a file to be used in file id creation
     * @return String
     */

    private String getExtension(String fileName) {
        log.debug("Method getExtension is called, argument(s) passed: {}", fileName);
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}
