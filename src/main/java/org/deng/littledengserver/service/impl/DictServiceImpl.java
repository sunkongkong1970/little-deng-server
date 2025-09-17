package org.deng.littledengserver.service.impl;

import org.deng.littledengserver.constant.DictConstant;
import org.deng.littledengserver.service.DictService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DictServiceImpl implements DictService {
    @Override
    public String getDict(String type,String key) {
        Map<String,String> dictmap = DictConstant.dictMap.get(type);
        if(dictmap==null){
            return null;
        }

        return dictmap.get(key);
    }

    @Override
    public Map<String, String> getDictList(String type) {
        return DictConstant.dictMap.get(type);
    }
}
