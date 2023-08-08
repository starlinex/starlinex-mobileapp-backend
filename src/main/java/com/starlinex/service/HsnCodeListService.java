package com.starlinex.service;

import com.starlinex.entity.HsnCodeList;
import com.starlinex.exception.StarLinexException;

import java.util.List;

public interface HsnCodeListService {
    List<HsnCodeList> getHsnList(String keyWord) throws StarLinexException;
}
