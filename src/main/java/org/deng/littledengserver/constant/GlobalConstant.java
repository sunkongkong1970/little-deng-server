package org.deng.littledengserver.constant;

import java.util.HashMap;
import java.util.Map;

public class GlobalConstant {
    public static final Map<String,String> roleMap = new HashMap<String,String>();
    static {
        roleMap.put("baba","爸爸");
        roleMap.put("mama","妈妈");
        roleMap.put("yeye","爷爷");
        roleMap.put("nainai","奶奶");
        roleMap.put("laolao","姥姥");
        roleMap.put("laoye","姥爷");
    }
}
