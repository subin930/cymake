package CY.cymake.AWS;

import CY.cymake.Exception.EmptyFileException;
import CY.cymake.Exception.FileDeleteFailedException;
import CY.cymake.Exception.FileUploadFailedException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
    @Autowired
    private AmazonS3 amazonS3; //AWS S3 클라이언트 객체

    @Autowired
    private AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket; //S3버킷 이름

    /*
     * S3에서 텍스트 파일 읽기
     */
    public String getBody(String path) throws IOException {
        try {
            S3Object awsS3Object = amazonS3.getObject(bucket, path);
            S3ObjectInputStream s3is = awsS3Object.getObjectContent();
            return new String(s3is.readAllBytes(), StandardCharsets.UTF_8);
        } catch(AmazonS3Exception ae) {
                throw new IllegalArgumentException(ae.getMessage());
        }
    }
    /*
     * S3에서 텍스트 파일 읽은 후 제목 & 뉴스 링크 추출 (0: 뉴스 링크 / 1: 뉴스 제목)
     */
    public List<String> getTitleAndLink(String path) throws IOException {
        String[] bodyList = getBody(path).split("\n");
        List<String> titleAndLink = new ArrayList<>();
        titleAndLink.add(bodyList[0]); //뉴스 링크
        titleAndLink.add(bodyList[1]); // 뉴스 제목
        return titleAndLink;
    }

    /*
     * S3에서 특정 디렉토리의 파일 이름을 읽어서 리스트로 반환(1000개씩 반복해서 읽음)
     */
    public List<String> getFileNames(String prefix) {
        List<String> fileNames = new ArrayList<>();
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(); //특정 접두사로 시작하는 객체들의 이름들만 반환
        listObjectsRequest.setBucketName(bucket);
        if(!prefix.isEmpty()) {
            listObjectsRequest.setPrefix(prefix);
        }
        ObjectListing s3Objects;

        do {
            s3Objects = amazonS3.listObjects(listObjectsRequest);
            for(S3ObjectSummary s3ObjectSummary : s3Objects.getObjectSummaries()) {
                fileNames.add(s3ObjectSummary.getKey());
            }
            listObjectsRequest.setMarker((s3Objects.getNextMarker()));
        } while(s3Objects.isTruncated());
        fileNames.remove(0); //0번째 요소는 디렉토리 경로이므로 제외
        return fileNames;
    }

    /*
     * S3에서 해당 파일의 URL 가져와서 반환
     */
    public String getUrl(String path) {
        return amazonS3.getUrl(bucket, path).toString();
    }

    /*
     * 파일 업로드
     */
    public String uploadFile(MultipartFile multipartFile, String path) throws IOException {
        if(multipartFile.isEmpty()) {
            throw new EmptyFileException("파일이 비어있습니다.");
        }
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        //metadata.setContentType(multipartFile.getContentType());
        try(InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, path, inputStream, metadata));
                    //.withCannedAcl(CannedAccessControlList.PublicRead));
        } catch(IOException e) {
            throw new FileUploadFailedException("파일 업로드에 실패했습니다.", e);
        }
        return amazonS3Client.getUrl(bucket, path).toString();
    }

    /*
     * 파일 용량 가져오기
     */
    public double getFileSize(String directory, String filename) throws IOException {
        String path = directory + filename;
        S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucket, path));
        s3Object.close();
        double size = s3Object.getObjectMetadata().getContentLength() /(1.0 * 1024 * 1024);
        DecimalFormat df = new DecimalFormat("#.###");
        return Double.parseDouble(df.format(size));
    }

    public ResponseEntity<byte[]> download(String directory, String filename) throws IOException {
        String path = directory + filename;
        S3Object awsS3Object = amazonS3.getObject(new GetObjectRequest(bucket, path));
        S3ObjectInputStream s3is = awsS3Object.getObjectContent();
        byte[] bytes = s3is.readAllBytes();
        String downloadedFileName = URLEncoder.encode(filename, StandardCharsets.UTF_8).replace("+", "%20");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(awsS3Object.getObjectMetadata().getContentType()));
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", downloadedFileName);

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

    /*
     * 파일 삭제
     */
    public void deleteFile(String directory, String filename) throws IOException {
        String path = directory + filename;
        System.out.println(path);
        try{
            amazonS3Client.deleteObject(bucket, path);
        } catch (SdkClientException e) {
            throw new FileDeleteFailedException("파일 삭제에 실패하였습니다.", e);
        } //service에서 db에서도 삭제해해야함
    }

    /*
     * 게시글 수정할 때 파일을 수정하면: 기존 파일 삭제 후 수정
     */
    public String updateFile(MultipartFile multipartFile, String directory, String original_filename) throws IOException {
        if(multipartFile.isEmpty()) {
            throw new EmptyFileException("파일이 존재하지 않습니다.");
        }
        String path = directory + multipartFile.getOriginalFilename();
        deleteFile(directory, original_filename);
        return uploadFile(multipartFile, path);
    }

}

