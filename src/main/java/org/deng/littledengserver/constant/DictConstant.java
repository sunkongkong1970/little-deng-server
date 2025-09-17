package org.deng.littledengserver.constant;

import org.deng.littledengserver.config.dict.Dict;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DictConstant {
    public static final String ROLE = "role";

    public static final Map<String,Map<String, String>> dictMap = new ConcurrentHashMap<>();

    public static final Map<String,String> roleMap = new ConcurrentHashMap<>();
    static {
        roleMap.put("baba","爸爸");
        roleMap.put("mama","妈妈");
        roleMap.put("yeye","爷爷");
        roleMap.put("nainai","奶奶");
        roleMap.put("laolao","姥姥");
        roleMap.put("laoye","姥爷");

        dictMap.put(ROLE,roleMap);
    }

}
