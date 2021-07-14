package com.duc.smallproject.modaldialog.util;

import com.duc.smallproject.modaldialog.model.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserCsvExporter extends AbstractExporter {
    public void export(List<User> listUsers,
                       HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "text/csv", ".csv");

        ICsvBeanWriter csvBean = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"UserId", "Email", "First Name", "Last-Name", "Roles", "Enabled"};
        String[] fieldMapping = {"id", "email", "firstName", "lastName", "roles", "enabled"};

        csvBean.writeHeader(csvHeader);
        listUsers.forEach(user -> {
            try {
                csvBean.write(user, fieldMapping);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

        csvBean.close();

    }
}
