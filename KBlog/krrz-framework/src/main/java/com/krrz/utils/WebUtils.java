package com.krrz.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WebUtils {
    //设置excel响应的response格式的封装  在CategoryController
    public static void setDownLoadHeader(String filename, HttpServletResponse response) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fname= URLEncoder.encode(filename,"UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition","attachment; filename="+fname);
    }
}
