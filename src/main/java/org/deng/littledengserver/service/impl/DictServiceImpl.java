package org.deng.littledengserver.service.impl;

import org.deng.littledengserver.constant.DictConstant;
import org.deng.littledengserver.service.DictService;
import org.deng.littledengserver.util.DictUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DictServiceImpl implements DictService {
    @Override
    public String getDict(String type,String key) {
        return DictUtil.getDict(type, key);
    }

    @Override
    public Map<String, String> getDictList(String type) {
        return DictConstant.dictMap.get(type);
    }
}
