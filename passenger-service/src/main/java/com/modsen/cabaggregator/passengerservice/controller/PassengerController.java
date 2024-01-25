package com.modsen.cabaggregator.passengerservice.controller;

import com.modsen.cabaggregator.passengerservice.dto.PassengerDTO;
import com.modsen.cabaggregator.passengerservice.dto.PassengerSortCriteria;
import com.modsen.cabaggregator.passengerservice.dto.PassengerUpdateDTO;
import com.modsen.cabaggregator.passengerservice.dto.PassengerViewingDTO;
import com.modsen.cabaggregator.passengerservice.model.enumeration.PassengerSortField;
import com.modsen.cabaggregator.passengerservice.service.PassengerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/passengers")
@Tag(name = "Passengers")
public class PassengerController {

    public static final String DEFAULT_PAGE = "1";
    public static final String DEFAULT_SIZE = "10";

    private final PassengerService passengerService;

    @GetMapping
    public ResponseEntity<Page<PassengerViewingDTO>> findAll(@RequestParam(required = false, defaultValue = DEFAULT_PAGE) @Min(1) Integer page,
                                                             @RequestParam(required = false, defaultValue = DEFAULT_SIZE) @Min(1) Integer size,
                                                             @RequestParam(required = false) Sort.Direction sortOrder,
                                                             @RequestParam(required = false) PassengerSortField sortField) {
        final PassengerSortCriteria sort = PassengerSortCriteria.builder()
                .field(Optional.ofNullable(sortField).orElse(PassengerSortField.NAME))
                .order(Optional.ofNullable(sortOrder).orElse(Sort.Direction.ASC))
                .build();

        return ResponseEntity.ok(
                passengerService.findAll(page, size, sort)
        );
    }

    @PostMapping
    public ResponseEntity<PassengerViewingDTO> create(@RequestBody @Valid PassengerDTO passengerDTO) {
        return new ResponseEntity<>(
                passengerService.save(passengerDTO),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        passengerService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerViewingDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                passengerService.findById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<PassengerViewingDTO> updatePassenger(@PathVariable UUID id,
                                                               @RequestBody @Valid PassengerUpdateDTO passengerUpdateDTO) {
        return ResponseEntity.ok(
                passengerService.update(id, passengerUpdateDTO)
        );
    }

}
