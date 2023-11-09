package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.service.AdsService;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;


@Slf4j
@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("ads")
@RequiredArgsConstructor
@Tag(name="Объявления")
public class AdsController {

    private final AdsService adsService;
    private static Logger logger = LoggerFactory.getLogger(AdsController.class);

    @Operation(summary = "Добавление объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = AdDto.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addAd(@RequestPart(value = "properties") CreateOrUpdateAd createOrUpdateAd, @RequestPart(value = "image") MultipartFile image) throws IOException {
        logger.info("Ads Controller image {}, createOrUpdateAd {}", image.getContentType(), createOrUpdateAd.getTitle());
        AdDto addedAd = adsService.addAd(createOrUpdateAd, image);
        if (addedAd == null) {
            logger.info("Unable to save the ad");
            return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        logger.info("New ad is added: {}", addedAd.getTitle());
        return new ResponseEntity<AdDto>(addedAd, HttpStatus.CREATED);
    }

    @Operation(summary = "Получение всех объявлений")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema =
            @Schema(implementation = Ads.class))})
    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        Ads receivedAds = adsService.getAllAds();
        return ResponseEntity.ok(receivedAds);
    }

    @Operation(summary = "Получение информации об объявлении")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema =
            @Schema(implementation = AdInfoDto.class))})
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("/{id}")
    public ResponseEntity<AdInfoDto> getAdInfo(@PathVariable("id") Integer id) {
        AdInfoDto adInfoDto = adsService.getAdInfo(id);
        if (adInfoDto == null) {
            logger.info("Not Found. Ad id {}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adInfoDto);
    }

    @Operation(summary = "Удаление объявления")
    @ApiResponse(responseCode = "204", description = "No content")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<AdEntity> deleteAd(@PathVariable("id") Integer id) {
        AdEntity adEntity = adsService.deleteAd(id);
        if (adEntity == null) {
            logger.info("Not Found. Ad id {}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adEntity);
    }

    @Operation(summary = "Обновление информации об объявлении")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema =
            @Schema(implementation = AdDto.class))})
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PatchMapping("/{id}")
    public ResponseEntity<AdDto> patchAd(@PathVariable("id") Integer id,
                                         @RequestBody AdUpdateDto adUpdateDto) {
        AdDto adDto = adsService.patchAd(id, adUpdateDto);
        if (adDto == null) {
            logger.info("Not Found. Ad id {}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adDto);
    }

    @Operation(summary = "Получение объявлений авторизованного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = Ads.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe() {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновление картинки объявления")
    @ApiResponse(responseCode = "200", description = "Ok", content = {
            @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)})
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PatchMapping("/{id}/image")
    public ResponseEntity<AdDto> updateImage(@PathVariable("id") Integer id, @RequestParam MultipartFile image) throws IOException {

        AdDto adDto = adsService.updateImage(id, image);
        if (adDto == null) {
            logger.info("Not Found. Ad id {}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adDto);
    }
}
