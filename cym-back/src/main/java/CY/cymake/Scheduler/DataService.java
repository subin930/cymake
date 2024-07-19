package CY.cymake.Scheduler;

import CY.cymake.AWS.S3Service;
import CY.cymake.Entity.CrwlNewsEntity;
import CY.cymake.Entity.CrwlTotalEntity;
import CY.cymake.Repository.CrwlTotalRepository;
import CY.cymake.Repository.CrwlNewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/*
 * S3버킷에 있는 자료를 db에 업데이트하기 위한 Service
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DataService {
    private final S3Service s3Service;
    private final CrwlNewsRepository crwlNewsRepository;
    private final CrwlTotalRepository crwlTotalRepository;
    /*
     * S3에서 크롤링한 데이터를 읽고 DB에 업데이트.
     * 중복 존재 시 예외 처리(뉴스 제목으로 중복 확인)
     */

    public void updateNewsDB() throws IOException {
        /*
         * 특정 디렉토리의 뉴스 제목 목록 들고 오기 -> 0번째 인덱스 요소가 디렉토리 경로라 제외해주어야 함.
         */

        //오늘 날짜 설정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");  //원하는 형식 지정
        LocalDateTime now = LocalDateTime.now();  //오늘 날짜와 시간 가져옴
        String date = now.format(formatter); //변형

        //주제 목록 설정
        List <String> subjectNames = List.of("car", "beauty");

        for(String subject: subjectNames) {
            Long total = 0L;
            //해당 디렉토리에 존재하는 파일들의 이름 모두 불러오기
            //List<String> fileNames = s3Service.getFileNames("testcrawl/" + subject + "/article/" + date + "/"); //todo: 이전 시점부터 지금 시점까지 모두 가져오도록 해야함
            List<String> fileNames = s3Service.getFileNames("testcrawl/" + subject + "/article/" + "20240717" + "/");
            System.out.println("fileNames: ");
            System.out.println(fileNames);
            /*
             * 관련 정보 DB에 넣기(title/url/newsLink/imgUrl/uploadDate/subject) -> 구조: url(\n)뉴스제목(\n)본문
             * 경로:ex) https://skku-cymake-crawl.s3.ap-northeast-1.amazonaws.com/testcrawl/beauty/article/20240716/202407160GWZJHRT8N.txt
             */
            //해당 날짜, 주제에 맞게 크롤링된 뉴스 기사 목록 가져옴
            for (String filename : fileNames) {
                List<String> titleAndLink = s3Service.getTitleAndLink(filename); //파일 제목 & 링크 추출
                String newsURL = s3Service.getUrl(filename); //뉴스 링크 추출
                String imgURL = s3Service.getUrl(changeFileExtension(filename, "jpg")); //이미지 url 추출
                if (imgURL == null) {
                    imgURL = "null";
                }
                CrwlNewsEntity news = CrwlNewsEntity.builder()
                        .title(titleAndLink.get(1))
                        .url(newsURL)
                        .newsLink(titleAndLink.get(0))
                        .imgUrl(imgURL)
                        .uploadDate(Timestamp.valueOf(LocalDateTime.now())) //예시 날짜
                        .subject(subject)
                        .build();
                crwlNewsRepository.save(news);
                total++;
            }
            //해당 날짜, 주제에 해당하는 tb_crwl_total의 엔티티 만들기
            CrwlTotalEntity crwlTotalEntity = CrwlTotalEntity.builder()
                    .subject(subject)
                    .date(date)
                    .total(total)
                    .build();
            crwlTotalRepository.save(crwlTotalEntity);
        }
    }
    /*
     * 뉴스 txt의 url을 통해 사진의 url 얻음 -> 파일 확장자 변경 -> 해당 뉴스 기사의 이미지 파일 불러올 때 사용
     */
    public static String changeFileExtension(String filePath, String newExtension) {
        filePath = filePath.replace("/article/", "/image/");
        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex == -1) {
            // 파일 경로에 점(.)이 없는 경우, 확장자를 추가
            return filePath + "." + newExtension;
        } else {
            // 기존 확장자를 새로운 확장자로 변경
            return filePath.substring(0, lastDotIndex + 1) + newExtension;
        }
    }
}
