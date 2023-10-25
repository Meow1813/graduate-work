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
import ru.skypro.homework.service.AdsService;
import io.swagger.v3.oas.annotations.tags.Tag;


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
                    @Schema(implementation = Ad.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addNewAd(@RequestPart(value = "properties") CreateOrUpdateAd createOrUpdateAd, @RequestPart(value = "image") MultipartFile image) {
        logger.info("Ads Controller image {}, createOrUpdateAd {}", image.getContentType(), createOrUpdateAd.getTitle());
        Ad addedAd = adsService.addNewAd(createOrUpdateAd);
        if (addedAd == null) {
            logger.info("Unable to save the ad");
            return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        logger.info("New ad is added: {}", addedAd.getTitle());
        return new ResponseEntity<Ad>(addedAd, HttpStatus.CREATED);
    }

    @Operation(summary = "Получение всех объявлений")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema =
            @Schema(implementation = Ads.class))})
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        Ads receivedAds = adsService.getAllAds();
        if (receivedAds == null){
            logger.info("Unable to get ads");
            return ResponseEntity.notFound().build();
        }
        logger.info("Ads sent");
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
        return ResponseEntity.ok(new AdInfoDto());
    }

    @Operation(summary = "Удаление объявления")
    @ApiResponse(responseCode = "204", description = "No content")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    @DeleteMapping("/{id}")
    public void deleteAd(@PathVariable("id") Integer id) {
    }

    @Operation(summary = "Обновление информации об объявлении")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema =
            @Schema(implementation = Ad.class))})
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> patchAd(@PathVariable("id") Integer id,
                                               @RequestBody AdUpdateDto adUpdateDto) {
        return ResponseEntity.ok(new Ad());
    }
}
