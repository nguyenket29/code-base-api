package com.java.constant;

import java.util.*;

public class Constants {
    public static final String ANONYMOUS = "Anonymous";

    public static final String AES_SECRET = "MTIzcnVnaHRhbmtobW91dA==";

    public static class StatusCode {
        public static final String SUCCESS = "SUCCESS";
        public static final String FAILED = "FAILED";
    }

    public static class ErrorCode {
        public static final String ERR_403000 = "403000";
        public static final Integer ERR_400000 = 400000; // Hàng hóa không đủ tồn trong kho
    }

    public static final String ATTRIBUTE_DELIMITER = "#_@_@_#";
    public static final String FORMAT_DATE = "dd/MM/YYYY";
}
