package com.einstens3.ironchef.Utilities;

import java.util.List;

/**
 * Created by hideki on 4/9/17.
 */

public class StringUtils {
    public static String fromList(List<String> list) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            if (i != 0)
                sb.append(", ");
            sb.append(list.get(i));
        }
        return sb.toString();
    }
}