package com.starlinex.controller;


import com.starlinex.exception.StarLinexException;
import com.starlinex.model.ServiceResponse;
import com.starlinex.service.DropDownService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dropDown")
@RequiredArgsConstructor
public class DropDownController {

    private final DropDownService dropDownService;

    @GetMapping("/getCountryData")
    public ResponseEntity<ServiceResponse> getCountryData() throws StarLinexException {
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setResponse(dropDownService.getDropDownById());
        serviceResponse.setResponseCode(200);
        serviceResponse.setMessage("Success");
        return ResponseEntity.ok(serviceResponse);
    }
}
