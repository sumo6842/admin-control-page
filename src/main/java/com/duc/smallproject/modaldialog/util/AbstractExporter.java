package com.duc.smallproject.modaldialog.util;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AbstractExporter {

    public void setResponseHeader(HttpServletResponse response,
                                  String contentType,
                                  String extension) {
        DateFormat dateFormat =
                new SimpleDateFormat("yyyy-mm-dd_HH-mm-ss");
        String timestamp = dateFormat.format(new Date());
        String filename = "users_" + timestamp + extension;
        response.setContentType(contentType);
        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=" + filename;
        response.setHeader(headerKey, headerValue);
    }



}
