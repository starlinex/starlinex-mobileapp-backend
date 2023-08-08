package com.starlinex.service;

import com.starlinex.entity.CountryData;
import com.starlinex.exception.StarLinexException;

public interface DropDownService {
    public CountryData getDropDownById() throws StarLinexException;
}
