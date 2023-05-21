package com.java.util;

import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class StringUtil {
    public static final String EMPTY = "";
    public static final String SPACE = " ";
    public static final String DOT = ".";
    public static final String COMMA = ",";
    public static final String COLON = ":";
    public static final String SEMICOLON = ";";
    public static final String UNDERSCORE = "_";

    public static final String paramCharacter = "\\{\\}";

    public StringUtil() {
    }

    public static String format(String format, List<String> params) {
        String res = format;
        for (String param: params) {
            res = res.replaceFirst(paramCharacter, param);
        }
        return res;
    }

    public static boolean isEmpty(String... args) {
        String[] var1 = args;
        int var2 = args.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            String ele = var1[var3];
            if (ele == null || ele.trim().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static String concatenate(List<String> listOfItems, String separator) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> iterator = listOfItems.iterator();

        while(iterator.hasNext()) {
            sb.append((String)iterator.next());
            if (iterator.hasNext()) {
                sb.append(separator);
            }
        }

        return sb.toString();
    }

    public static String toStringFromList(List<String> list, String separator) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < list.size(); ++i) {
            sb.append((String)list.get(i));
            if (i < list.size() - 1) {
                sb.append(separator);
            }
        }

        return sb.toString();
    }

    public static String convertDateToString(Date date, String pattern) {
        String dateStr = null;
        if (date != null) {
            DateFormat dateFormat = new SimpleDateFormat(pattern);

            try {
                dateStr = dateFormat.format(date);
            } catch (Exception var5) {
                return null;
            }
        }

        return dateStr;
    }

    public static String escapePath(String path) {
        if (path == null || EMPTY.equals(path)) {
            return path;
        }

        String step1 = path.replace("\\", "\\\\");
        step1 = step1.replace("%", "\\%");
        return step1.replace("_", "\\_");
    }

    public static String validateKeywordSearch(String value) {
        if ((value != null && !value.isEmpty())) {
            return escapePath(value).toLowerCase();
        }
        return "";
    }

    public static String trim(String value) {
        if ((value != null && !value.isEmpty())) {
            return value.trim();
        }
        return value;
    }

    public static String normalize(String input) {
        return input == null ? null : Normalizer.normalize(input, Normalizer.Form.NFKD);
    }

    public static String removeAccents(String input) {
        input = normalize(input);
        if (input != null && !input.isEmpty()) {
            input = input.replaceAll("\\p{M}", "");
            return input.replace("Đ", "D").replace("đ", "d");
        }
        return input;
    }

}
