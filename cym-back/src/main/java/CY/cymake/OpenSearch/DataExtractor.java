package CY.cymake.OpenSearch;


import CY.cymake.Entity.CrwlNewsEntity;
import CY.cymake.Entity.FileEntity;
import CY.cymake.Repository.CrwlNewsRepository;
import CY.cymake.Repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataExtractor {
    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    private final CrwlNewsRepository crwlNewsRepository;
    private final FileRepository fileRepository;

    public List<Map<String, Object>> extractFileData() {
        List<FileEntity> fileEntities = fileRepository.findAll();

        return fileEntities.stream()
                .filter(Objects::nonNull) // Null 체크
                .map(file -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("file_id", file.getId());
                    map.put("s3_fn", file.getS3Fn());
                    map.put("original_fn", file.getOriginalFn());
                    map.put("file_url", file.getFileUrl());
                    map.put("last_edit_date", file.getLastEditDate().toInstant().toString());
                    map.put("post_title", file.getPostTitle());
                    map.put("type", file.getType());
                    map.put("upload_date", file.getUploadDate().toInstant().toString());
                    map.put("company_code", file.getCompanyCode().getCode());
                    map.put("uploader", file.getUploader().getId());
                    map.put("size", file.getSize());
                    return map;
                })
                .collect(Collectors.toList());
    }

    /*
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

     */

/*
    public List<Map<String, Object>> extractCrwlNewsData() throws Exception {
        List<Map<String, Object>> dataList = new ArrayList<>();

        Connection conn = DriverManager.getConnection(url, username, password);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM tb_crwl_news");

        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            row.put("news_id", rs.getLong("news_id"));
            row.put("subject", rs.getString("subject"));
            row.put("title", rs.getString("title"));
            // 필요한 다른 필드 추가
            dataList.add(row);
        }

        rs.close();
        stmt.close();
        conn.close();
        return dataList;

    }

 */

    public List<Map<String, Object>> extractCrwlNewsData() {
        List<CrwlNewsEntity> newsEntities = crwlNewsRepository.findAll();

        return newsEntities.stream()
                .filter(Objects::nonNull) // Null 체크
                .map(news -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("news_id", news.getId());
                    map.put("subject", news.getSubject());
                    map.put("title", news.getTitle());
                    return map;
                })
                .collect(Collectors.toList());
    }

}
