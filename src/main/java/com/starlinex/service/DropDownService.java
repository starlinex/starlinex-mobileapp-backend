package com.starlinex.service;

import com.starlinex.entity.CountryData;
import com.starlinex.exception.StarLinexException;

import java.util.List;

public interface DropDownService {
    public List<CountryData> getDropDownById() throws StarLinexException;
}
