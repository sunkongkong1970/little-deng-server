package org.deng.littledengserver.util;

import org.deng.littledengserver.constant.DictConstant;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DictUtil {

    public static String getDict(String type, String key) {
        Map<String, String> dictmap = DictConstant.dictMap.get(type);
        if (dictmap == null) {
            return null;
        }

        return dictmap.get(key);
    }
}
