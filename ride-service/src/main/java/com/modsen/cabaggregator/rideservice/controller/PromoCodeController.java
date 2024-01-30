package com.modsen.cabaggregator.rideservice.controller;

import com.modsen.cabaggregator.rideservice.dto.AllPromoCodesResponse;
import com.modsen.cabaggregator.rideservice.dto.CreatePromoCodeRequest;
import com.modsen.cabaggregator.rideservice.dto.PromoCodeResponse;
import com.modsen.cabaggregator.rideservice.dto.UpdatePromoCodeRequest;
import com.modsen.cabaggregator.rideservice.service.PromoCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/promos")
@Tag(name = "Promo codes")
public class PromoCodeController {

    public static final String DEFAULT_PAGE = "0";
    public static final String DEFAULT_SIZE = "5";

    private final PromoCodeService promoService;

    @Operation(description = "Find all promo codes")
    @GetMapping
    public ResponseEntity<AllPromoCodesResponse> getAllPromoCodes(@RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
                                                                  @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return ResponseEntity.ok(
                promoService.findAll(page, size)
        );
    }

    @Operation(description = "Create promo code")
    @PostMapping
    public ResponseEntity<PromoCodeResponse> createPromo(@RequestBody @Valid CreatePromoCodeRequest request) {
        return new ResponseEntity<>(
                promoService.create(request),
                HttpStatus.CREATED
        );
    }

    @Operation(description = "Update promo code")
    @PutMapping("/{name}")
    public ResponseEntity<PromoCodeResponse> updatePromo(@PathVariable String name,
                                                         @RequestBody @Valid UpdatePromoCodeRequest request) {
        return ResponseEntity.ok(
                promoService.update(name, request)
        );
    }

    @Operation(description = "Delete promotional code")
    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePromo(@PathVariable String name) {
        promoService.delete(name);
    }

}
