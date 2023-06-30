package com.starlinex.service.impl;

import com.starlinex.entity.AirWayBill;
import com.starlinex.entity.User;
import com.starlinex.model.AirWay;
import com.starlinex.repository.AirWayBillRepository;
import com.starlinex.utils.DocUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AirWayBillService {
    private static final Logger LOGGER = Logger.getLogger(AirWayBill.class.getName());
    private final AirWayBillRepository airWayBillRepository;

    public String storeAirWayBillInfo(AirWay airWay) throws Exception{
        try{
            var airWayBill = AirWayBill.builder()
                    .userId(airWay.getUserId())
            .awbNbr(airWay.getAwbNbr())
            .destination(airWay.getDestination())
            .product(airWay.getProduct())
            .bookingDate(airWay.getBookingDate())
            .insurance(airWay.getInsurance())
            .service(airWay.getService())
            .shipmentValue(airWay.getShipmentValue())
            .invoiceDate(airWay.getInvoiceDate())
            .invoiceNbr(airWay.getInvoiceNbr())
            .content(airWay.getContent())

            .shipperPersonName(airWay.getShipperPersonName())
            .shipperAddress1(airWay.getShipperAddress1())
            .shipperAddress2(airWay.getShipperAddress2())
            .shipperAddress3(airWay.getShipperAddress3())
            .shipperZipCode(airWay.getShipperZipCode())
            .shipperCity(airWay.getShipperCity())
            .shipperState(airWay.getShipperState())
            .shipperPhoneNbr(airWay.getShipperPhoneNbr())
            .shipperEmailAddress(airWay.getShipperEmailAddress())
            .shipperKycType(airWay.getShipperKycType())
            .shipperKycNbr(airWay.getShipperKycNbr())
            .shipperKycDoc(airWay.getShipperKycDoc())

            .receiverAddressBook(airWay.getReceiverAddressBook())
            .receiverCompany(airWay.getReceiverCompany())
            .receiverPersonName(airWay.getReceiverPersonName())
            .receiverAddress1(airWay.getReceiverAddress1())
            .receiverAddress2(airWay.getReceiverAddress2())
            .receiverAddress3(airWay.getReceiverAddress3())
            .receiverZipCode(airWay.getReceiverZipCode())
            .receiverCity(airWay.getReceiverCity())
            .receiverCountry(airWay.getReceiverCountry())
            .receiverState(airWay.getReceiverState())
            .receiverPhoneNbr(airWay.getReceiverPhoneNbr())
            .receiverPhoneNbr2(airWay.getReceiverPhoneNbr2())
            .receiverEmailAddress(airWay.getReceiverEmailAddress())

            .pcs(airWay.getPcs())
            .actualWeight(airWay.getActualWeight())
            .volumetricWeight(airWay.getVolumetricWeight())
            .consignerWeight(airWay.getConsignerWeight())
            .chargeableWeight(airWay.getChargeableWeight())
            .parcelNo(airWay.getParcelNo())
            .boxNo(airWay.getBoxNo())
            .actualWt(airWay.getActualWt())
            .lcm(airWay.getLcm())
            .bcm(airWay.getBcm())
            .hcm(airWay.getHcm())
            .volumetricWt(airWay.getVolumetricWt())
            .chargeableEt(airWay.getChargeableEt())

            .specialServiceName(airWay.getSpecialServiceName())
            .specialService(airWay.getSpecialService())

            .invoiceType(airWay.getInvoiceType())
            .incoterms(airWay.getIncoterms())
            .note(airWay.getNote())
            .descNote(airWay.getDescNote())

            .shipmentBoxNo(airWay.getShipmentBoxNo())
            .shipmentDescription(airWay.getShipmentDescription())
            .shipmentHsCode(airWay.getShipmentHsCode())
            .shipmentUnityType(airWay.getShipmentUnityType())
            .shipmentQuantity(airWay.getShipmentQuantity())
            .shipmentUnitWeight(airWay.getShipmentUnitWeight())
            .shipmentIgst(airWay.getShipmentIgst())
            .shipmentUnitRates(airWay.getShipmentUnitRates())
            .shipmentAmount(airWay.getShipmentAmount())
                    .build();
            airWayBillRepository.save(airWayBill);
        }catch (Exception e){
            LOGGER.logrb(Level.SEVERE, ResourceBundle.getBundle(e.toString()), String.valueOf(e));
        }
        return "Data added successfully";
    }
    public List<AirWayBill> getDataById(Integer id) throws Exception{
        List<AirWayBill> airWays = new ArrayList<>();
        try{
            airWays = airWayBillRepository.findAllByUserId(id);
        }catch (Exception e){
            LOGGER.logrb(Level.SEVERE, ResourceBundle.getBundle(e.toString()), String.valueOf(e));
            throw new Exception(e.getMessage());
        }
        return airWays;
    }
}
