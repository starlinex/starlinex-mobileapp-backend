package com.starlinex.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starlinex.model.AirWay;
import com.starlinex.model.ServiceResponse;
import com.starlinex.service.impl.AirWayBillServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/airWay")
@RequiredArgsConstructor
public class AirWayBillController {

    private final AirWayBillServiceImpl airWayBillService;

    @PostMapping("/storeAirWayBill")
    public ResponseEntity<ServiceResponse> saveAirWayBill(@RequestPart("airWay") String airWay, @RequestPart("doc") MultipartFile[] file) throws Exception {
        ServiceResponse serviceResponse = new ServiceResponse();
        AirWay airWayModel = new AirWay();
        try {
            if (file != null && airWay != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                airWayModel = objectMapper.readValue(airWay, AirWay.class);
                airWayModel.setShipperKycDoc(file);
                serviceResponse.setResponseCode(200);
                serviceResponse.setResponse(airWayBillService.storeAirWayBillInfo(airWayModel));
                serviceResponse.setMessage("Success");
            }
        } catch (Exception e) {
            serviceResponse.setResponseCode(404);
            serviceResponse.setMessage("Failed to store data");
            throw new Exception(e.getMessage());
        }
        return ResponseEntity.ok(serviceResponse);

    }

    @GetMapping("/getAllData/{id}")
    public ResponseEntity<ServiceResponse> getAllData(@PathVariable Integer id) throws Exception {
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setResponse(airWayBillService.getDataById(id));
        serviceResponse.setResponseCode(200);
        serviceResponse.setMessage("Success");
        return ResponseEntity.ok(serviceResponse);
    }

//    @GetMapping("/getAllData1/{id}")
//    public ResponseEntity<ServiceResponse> getAllData1(@PathVariable Integer id) throws Exception {
//        ServiceResponse serviceResponse = new ServiceResponse();
//        List<byte[]> img = new ArrayList<>();
//        airWayBillService.getDataById(id).forEach(airWayBill -> {
//            img.add(DocUtils.decompressImage(airWayBill.getShipperKycDoc()));
//        });
//        serviceResponse.setResponse(img);
//        serviceResponse.setResponseCode(200);
//        serviceResponse.setMessage("Success");
//        return ResponseEntity.ok(serviceResponse);
//    }

}
