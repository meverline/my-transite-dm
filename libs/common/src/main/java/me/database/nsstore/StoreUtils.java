package me.database.nsstore;

import java.util.List;

public class StoreUtils {

    /**
     *
     * @param fieldList
     * @return
     */
    public static String toDocField(List<String> fieldList) {
        StringBuilder queryField = new StringBuilder();

        for (String fld : fieldList) {
            if (queryField.length() > 0) {
                queryField.append(".");
            }
            queryField.append(fld);
        }
        return queryField.toString();
    }

}
