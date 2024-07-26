package CY.cymake.OpenSearch;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DataExtractor {
    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    public List<Map<String, Object>> extractFileData() throws Exception {
        List<Map<String, Object>> dataList = new ArrayList<>();

        Connection conn = DriverManager.getConnection(url, username, password);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM tb_file");

        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            row.put("file_id", rs.getLong("file_id"));
            row.put("file_name", rs.getString("file_name"));
            row.put("file_url", rs.getString("file_url"));
            row.put("last_edit_date",  rs.getTimestamp("last_edit_date").toInstant().toString());
            row.put("post_title", rs.getString("post_title"));
            row.put("type", rs.getString("type"));
            row.put("upload_date", rs.getTimestamp("upload_date").toInstant().toString());
            row.put("company_code", rs.getString("company_code"));
            row.put("uploader", rs.getString("uploader"));
            // 필요한 다른 필드 추가
            dataList.add(row);
        }

        rs.close();
        stmt.close();
        conn.close();

        return dataList;
    }

    public List<Map<String, Object>> extractCrwlNewsData() throws Exception {
        List<Map<String, Object>> dataList = new ArrayList<>();

        Connection conn = DriverManager.getConnection(url, username, password);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM tb_crwl_news");

        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            row.put("news_id", rs.getLong("news_id"));
            row.put("img_url", rs.getString("img_url"));
            row.put("news_link", rs.getString("news_link"));
            row.put("subject", rs.getString("subject"));
            row.put("title", rs.getString("title"));
            row.put("upload_date", rs.getTimestamp("upload_date").toInstant().toString());
            row.put("url", rs.getString("url"));
            // 필요한 다른 필드 추가
            dataList.add(row);
        }

        rs.close();
        stmt.close();
        conn.close();

        return dataList;

    }
}
