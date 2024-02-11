package com.modsen.cabaggregator.rideservice.service.impl;

import com.modsen.cabaggregator.common.util.PageRequestValidator;
import com.modsen.cabaggregator.rideservice.dto.AllPromoCodesResponse;
import com.modsen.cabaggregator.rideservice.dto.CreatePromoCodeRequest;
import com.modsen.cabaggregator.rideservice.dto.PromoCodeResponse;
import com.modsen.cabaggregator.rideservice.dto.UpdatePromoCodeRequest;
import com.modsen.cabaggregator.rideservice.exception.PromoCodeNotFoundException;
import com.modsen.cabaggregator.rideservice.mapper.PromoCodeMapper;
import com.modsen.cabaggregator.rideservice.model.PromoCode;
import com.modsen.cabaggregator.rideservice.repository.PromoCodeRepository;
import com.modsen.cabaggregator.rideservice.service.PromoCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class PromoCodeServiceImpl implements PromoCodeService {

    private final PromoCodeRepository promoCodeRepository;
    private final PromoCodeMapper promoCodeMapper;

    @Override
    public AllPromoCodesResponse findAll(Integer page, Integer size) {
        PageRequestValidator.validatePageRequestParameters(page, size);
        Page<PromoCode> promos = promoCodeRepository.findAll(PageRequest.of(page, size));
        return new AllPromoCodesResponse(
                promos.getContent()
                        .stream()
                        .map(promoCodeMapper::toPromoCodeResponse)
                        .toList(),
                page,
                promos.getTotalPages(),
                promos.getTotalElements()
        );
    }

    @Override
    public PromoCodeResponse create(CreatePromoCodeRequest createPromoCodeRequest) {
        final String name = createPromoCodeRequest.getName();
        log.info("Create promo code. Name: {}", name);
        return promoCodeMapper.toPromoCodeResponse(
                promoCodeRepository.save(
                        PromoCode.builder()
                                .name(name)
                                .value(createPromoCodeRequest.getValue())
                                .build()
                )
        );
    }

    @Override
    public PromoCodeResponse update(String name, UpdatePromoCodeRequest request) {
        PromoCode promoCode = findByName(name);

        promoCode.setValue(request.getValue());

        log.info("Update promo code. Name: {}", name);
        return promoCodeMapper.toPromoCodeResponse(
                promoCodeRepository.save(promoCode)
        );
    }

    @Override
    public void delete(String name) {
        promoCodeRepository.deleteById(name);
        log.info("Delete promo code. Name: {}", name);
    }

    @Override
    public BigDecimal getPromoCodeValue(String name) {
        return findByName(name).getValue();
    }

    private PromoCode findByName(String name) {
        return promoCodeRepository.findById(name)
                .orElseThrow(() -> new PromoCodeNotFoundException(name));
    }

}
