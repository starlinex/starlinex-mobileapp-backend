package com.starlinex.service.impl;

import com.starlinex.entity.HsnCodeList;
import com.starlinex.exception.StarLinexException;
import com.starlinex.repository.HsnCodeListRepository;
import com.starlinex.service.HsnCodeListService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HsnCodeListServiceImpl implements HsnCodeListService {

    private final static Logger LOGGER = LoggerFactory.getLogger(HsnCodeListServiceImpl.class);

    private final HsnCodeListRepository repository;
    @Override
    public List<HsnCodeList> getHsnList(String keyWord) throws StarLinexException {
        List<HsnCodeList> hsnCodeList = new ArrayList<>();
        try{
            if(!keyWord.isBlank()) {
                hsnCodeList = repository.searchByKeyword(keyWord);
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            throw new StarLinexException("Something went wrong");
        }
        return hsnCodeList;
    }
}
