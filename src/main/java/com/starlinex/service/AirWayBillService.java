package com.starlinex.service;

import com.starlinex.entity.AirWayBill;
import com.starlinex.model.AirWay;

import java.util.List;

public interface AirWayBillService {
    String storeAirWayBillInfo(AirWay airWay) throws Exception;

    List<AirWayBill> getDataById(Integer id) throws Exception;
}
