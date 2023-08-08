package com.starlinex.service;

import com.starlinex.model.AirWay;

import java.util.List;

public interface AirWayBillService {
    String storeAirWayBillInfo(AirWay airWay) throws Exception;

    List<AirWay> getDataById(Integer id) throws Exception;
}
