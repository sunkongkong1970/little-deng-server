package org.deng.littledengserver.service;

import java.util.Map;

public interface DictService {
    String getDict(String type,String key);

    Map<String,String> getDictList(String type);
}
