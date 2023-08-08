package com.starlinex.controller;


import com.starlinex.exception.StarLinexException;
import com.starlinex.model.ServiceResponse;
import com.starlinex.service.HsnCodeListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hsnDetails")
@RequiredArgsConstructor
public class HsnDetailsController {

    private final HsnCodeListService hsnCodeListService;

    @GetMapping("/search")
    public ResponseEntity<ServiceResponse> getHsnDetails(@RequestParam String keyWord) throws StarLinexException {
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setResponse(hsnCodeListService.getHsnList(keyWord));
        serviceResponse.setResponseCode(200);
        serviceResponse.setMessage("Success");
        return ResponseEntity.ok(serviceResponse);
    }
}
