package com.modsen.cabaggregator.rideservice.service.impl;

import com.modsen.cabaggregator.rideservice.dto.AllPromoCodesResponse;
import com.modsen.cabaggregator.rideservice.dto.CreatePromoCodeRequest;
import com.modsen.cabaggregator.rideservice.dto.PromoCodeResponse;
import com.modsen.cabaggregator.rideservice.dto.UpdatePromoCodeRequest;
import com.modsen.cabaggregator.rideservice.mapper.PromoCodeMapper;
import com.modsen.cabaggregator.rideservice.model.PromoCode;
import com.modsen.cabaggregator.rideservice.repository.PromoCodeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class PromoCodeServiceImplTest {

    public static final BigDecimal PROMO_CODE_VALUE = BigDecimal.valueOf(10);
    public static final String PROMO_CODE_NAME = "promo-1";

    @Mock
    private PromoCodeRepository promoCodeRepository;

    @Mock
    private PromoCodeMapper promoCodeMapper;

    @InjectMocks
    private PromoCodeServiceImpl promoCodeService;

    @Test
    void findAll_ShouldReturnAllPromoCodesResponse() {
        int page = 1;
        int size = 10;
        List<PromoCode> promoCodes = Arrays.asList(
                new PromoCode(PROMO_CODE_NAME, PROMO_CODE_VALUE),
                new PromoCode(PROMO_CODE_NAME, PROMO_CODE_VALUE)
        );
        Page<PromoCode> promoCodePage = new PageImpl<>(promoCodes);
        Mockito.when(promoCodeRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(promoCodePage);

        AllPromoCodesResponse response = promoCodeService.findAll(page, size);

        Mockito.verify(promoCodeRepository).findAll(PageRequest.of(page, size));
        Mockito.verify(promoCodeMapper, Mockito.times(2)).toPromoCodeResponse(Mockito.any(PromoCode.class));
        Assertions.assertThat(response.getContent()).hasSize(promoCodes.size());
    }

    @Test
    void update_ShouldUpdatePromoCodeEntity() {
        UpdatePromoCodeRequest request = new UpdatePromoCodeRequest(BigDecimal.valueOf(15));
        PromoCode promoCode = new PromoCode(PROMO_CODE_NAME, PROMO_CODE_VALUE);
        Mockito.when(promoCodeMapper.toPromoCodeResponse(Mockito.any(PromoCode.class))).thenReturn(
                PromoCodeResponse.builder()
                        .name(PROMO_CODE_NAME)
                        .value(BigDecimal.valueOf(15))
                        .build()
        );
        Mockito.when(promoCodeRepository.findById(PROMO_CODE_NAME)).thenReturn(Optional.of(promoCode));
        Mockito.when(promoCodeRepository.save(Mockito.any(PromoCode.class))).thenReturn(promoCode);

        PromoCodeResponse response = promoCodeService.update(PROMO_CODE_NAME, request);

        Mockito.verify(promoCodeRepository).findById(PROMO_CODE_NAME);
        Mockito.verify(promoCodeRepository).save(promoCode);
        Mockito.verify(promoCodeMapper).toPromoCodeResponse(promoCode);
        Assertions.assertThat(response.getName()).isEqualTo(promoCode.getName());
        Assertions.assertThat(response.getValue()).isEqualTo(request.getValue());
    }

    @Test
    void delete_ShouldDeletePromoCodeEntity() {
        promoCodeService.delete(PROMO_CODE_NAME);

        Mockito.verify(promoCodeRepository).deleteById(PROMO_CODE_NAME);
    }

    @Test
    void create_ShouldSaveCreatedEntity() {
        CreatePromoCodeRequest request = new CreatePromoCodeRequest(PROMO_CODE_NAME, PROMO_CODE_VALUE);
        PromoCode promoCode = new PromoCode(PROMO_CODE_NAME, PROMO_CODE_VALUE);
        Mockito.when(promoCodeRepository.save(Mockito.any(PromoCode.class))).thenReturn(promoCode);
        Mockito.when(promoCodeMapper.toPromoCodeResponse(Mockito.any(PromoCode.class))).thenReturn(
                PromoCodeResponse.builder()
                        .name(PROMO_CODE_NAME)
                        .value(BigDecimal.TEN)
                        .build()
        );

        PromoCodeResponse response = promoCodeService.create(request);

        Mockito.verify(promoCodeMapper).toPromoCodeResponse(promoCode);
        Assertions.assertThat(response.getName()).isEqualTo(promoCode.getName());
        Assertions.assertThat(response.getValue()).isEqualTo(promoCode.getValue());
    }

    @Test
    void getPromoCodeValue_ShouldReturnValueFromRepository() {
        PromoCode promoCode = new PromoCode(PROMO_CODE_NAME, PROMO_CODE_VALUE);
        Mockito.when(promoCodeRepository.findById(PROMO_CODE_NAME)).thenReturn(java.util.Optional.of(promoCode));

        BigDecimal result = promoCodeService.getPromoCodeValue(PROMO_CODE_NAME);

        Mockito.verify(promoCodeRepository).findById(PROMO_CODE_NAME);
        Assertions.assertThat(result).isEqualTo(PROMO_CODE_VALUE);
    }

}
