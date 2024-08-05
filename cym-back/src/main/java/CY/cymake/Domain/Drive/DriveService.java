package CY.cymake.Domain.Drive;

import CY.cymake.AWS.S3Service;
import CY.cymake.Domain.Auth.Dto.CustomUserInfoDto;
import CY.cymake.Domain.Drive.Dto.CrwlResDto;
import CY.cymake.Domain.Drive.Dto.CrwlTotalDto;
import CY.cymake.Domain.Drive.Dto.PostListResDto;
import CY.cymake.Domain.Drive.Dto.PostSearchResultDto;
import CY.cymake.Entity.CompanyEntity;
import CY.cymake.Entity.CrwlTotalEntity;
import CY.cymake.Entity.FileEntity;
import CY.cymake.Entity.UsersEntity;
import CY.cymake.Exception.FileDeleteFailedException;
import CY.cymake.Exception.FileUpdateFailedException;
import CY.cymake.Exception.UserNotFoundException;
import CY.cymake.OpenSearch.DataExtractor;
import CY.cymake.OpenSearch.OpenSearchService;
import CY.cymake.Repository.CrwlTotalRepository;
import CY.cymake.Repository.FileRepository;
import CY.cymake.Repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriveService {
    private final S3Service s3Service;
    private final UsersRepository usersRepository;
    private final FileRepository fileRepository;
    private final OpenSearchService openSearchService;
    private final DataExtractor dataExtractor;
    private final CrwlTotalRepository crwlTotalRepository;

    public String uploadFile(CustomUserInfoDto user, MultipartFile multipartFile, String postTitle) throws IOException, Exception {
        Optional<UsersEntity> siteUser= usersRepository.findById(user.getId());
        if(siteUser.isEmpty()) {
            throw new UserNotFoundException("파일 업로드에 실패했습니다.");
        }
        CompanyEntity companyCode = user.getCompanyCode();
        String path = "files/" + companyCode.getCode() + "/" + multipartFile.getOriginalFilename();
        String fileUrl = s3Service.uploadFile(multipartFile, path);

        FileEntity file = FileEntity.builder()
                .companyCode(companyCode)
                .postTitle(postTitle)
                .file(multipartFile.getOriginalFilename())
                .fileUrl(fileUrl)
                .uploader(siteUser.get())
                .uploadDate(Timestamp.valueOf(LocalDateTime.now()))
                .lastEditDate(Timestamp.valueOf(LocalDateTime.now()))
                .type(getExtension(Objects.requireNonNull(multipartFile.getOriginalFilename())))
                .build();
        fileRepository.save(file);
        openSearchService.bulkUploadData(dataExtractor.extractFileData(), "tb_file", "file_id");
        return fileUrl;
    }
    /*
     * 파일 다운로드
     */
    /*
    public ResponseEntity<byte[]> download(CustomUserInfoDto user, String filename) throws IOException {
        String directory = "files/" + user.getCompanyCode().getCode() + "/";
        return s3Service.download(directory, filename);
    }

     */
    /*
     * 파일 삭제
     */
    public void deleteFile(CustomUserInfoDto user, String filename) throws IOException {
        //input 1) CustomUserInfoDto user:로그인 되어 있는 유저 정보 2) String filename: 삭제할 파일 이름 ex. example_text.txt
        FileEntity file = fileRepository.findByCompanyCodeAndFile(user.getCompanyCode(), filename).orElseThrow(() -> new FileDeleteFailedException("파일이 존재하지 않습니다."));
        //1. 버킷의 특정 디렉터리로 매핑
        String directory = "files/" + user.getCompanyCode().getCode() + "/";

        //2. 작성자와 일치하는지 확인
        if(!user.getId().equals(file.getUploader().getId())){
            //일치하지 않을 경우
            throw new FileDeleteFailedException("파일 삭제에 실패하였습니다.");
        }
        //opensearch 인덱스에서 삭제
        openSearchService.deleteFileData(file.getId());

        //3. s3에서 삭제 로직 수행
        s3Service.deleteFile(directory, filename);

        //4. db에서 삭제 로직 수행
        fileRepository.delete(file);
    }
    /*
     * 파일 수정
     */
    public void updateFile(CustomUserInfoDto user, MultipartFile newFile, String originalFilename, String postTitle) throws IOException {
        FileEntity file = fileRepository.findByCompanyCodeAndFile(user.getCompanyCode(), originalFilename).orElseThrow(() -> new FileUpdateFailedException("파일이 존재하지 않습니다."));

        //1. 작성자 일치 여부 확인
        if(!user.getId().equals(file.getUploader().getId())) {
            throw new FileUpdateFailedException("기존 post가 존재하지 않습니다.");
        }
        //2. 파일 수정 여부 확인
        if(newFile == null) {
            file.updatePostTitle(postTitle);
            fileRepository.save(file);
            Map<String, Object> data = convertFileData(file.getId(), file.getFile(), file.getFileUrl(), postTitle,
                    file.getType(), file.getUploadDate(), file.getCompanyCode().getCode(),
                    file.getUploader().getId());
            openSearchService.addAndUpdateFileData(data, "file_id");
            return;
        }
        //2. s3에서 파일 수정(기존거 삭제, 새로운거 올림)
        String directory = "files/" +  user.getCompanyCode().getCode() + "/";
        String fileUrl = s3Service.updateFile(newFile, directory, originalFilename);
        //3. db수정
        file.updatePost(postTitle, newFile.getOriginalFilename(), fileUrl, getExtension(Objects.requireNonNull(newFile.getOriginalFilename())));
        fileRepository.save(file);
        Map<String, Object> data = convertFileData(file.getId(), newFile.getOriginalFilename(), fileUrl, postTitle,
                getExtension(newFile.getOriginalFilename()), file.getUploadDate(), file.getCompanyCode().getCode(),
                file.getUploader().getId());
        openSearchService.addAndUpdateFileData(data, "file_id");
    }

    /*
     * 중복되는 이름의 파일 충돌 방지 코드 -> 적용 여부 후에 논의
     */
    public String createRandomFilename(String originalFilename) {
        int idx = originalFilename.lastIndexOf(".");
        String extension = originalFilename.substring(idx + 1);
        return UUID.randomUUID() + "." + extension;
    }
    /*
     * 확장자 추출
     */
    public String getExtension(String originalFilename) {
        int idx = originalFilename.lastIndexOf(".");
        return originalFilename.substring(idx + 1);
    }

    /*
     * post 리스트 전송
     */
    public List<PostListResDto> getPostList(CustomUserInfoDto user) throws Exception {
        //openSearchService.deleteFileIndex(); //test 용. 실제 코드에서는 삭제
        openSearchService.deleteFileIndex();
        openSearchService.deleteNewsIndex();
        openSearchService.createCrwlNewsTb();
        openSearchService.createFileTb();
        openSearchService.bulkUploadData(dataExtractor.extractCrwlNewsData(), "tb_crwl_news", "news_id");
        openSearchService.bulkUploadData(dataExtractor.extractFileData(), "tb_file", "file_id");
        String directory = "files/" + user.getCompanyCode().getCode() + "/";
        System.out.println(directory);

        List<FileEntity> files = fileRepository.findAllByCompanyCode(user.getCompanyCode());
        List<PostListResDto> posts = new ArrayList<>();
        for(FileEntity file: files) {
            PostListResDto post = PostListResDto.builder()
                    .fileName(file.getFile())
                    .postTitle(file.getPostTitle())
                    .id(file.getUploader().getId())
                    .username(file.getUploader().getUsername())
                    .fileUrl(file.getFileUrl())
                    .uploadDate(file.getUploadDate())
                    .size(s3Service.getFileSize(directory, file.getFile()))
                    .build();
            posts.add(post);
        }
        return posts;
    }
    /*
     * post 검색
     */
    public List<PostListResDto> searchPost(CustomUserInfoDto user, String searchBody) throws Exception {
        return changeToPostList(user, openSearchService.searchFileTb(user, "tb_file", searchBody));

    }
    /*
     * PostListDto -> PostSearchResultDto
     */
    public List<PostListResDto> changeToPostList(CustomUserInfoDto user, List<PostSearchResultDto> list) throws IOException {
        String directory = "files/" + user.getCompanyCode().getCode() + "/";
        List<PostListResDto> result = new ArrayList<>();
        for(PostSearchResultDto post: list) {
            String username = usersRepository.findById(post.getUploader()).orElseThrow(() -> new UserNotFoundException("파일 업로더가 존재하지 않습니다.")).getUsername();
            PostListResDto postListResDto = new PostListResDto(post.getFile_name(), post.getPost_title(), post.getFile_url(), post.getUploader(), username, post.getUpload_date(), s3Service.getFileSize(directory,post.getFile_name()));
            result.add(postListResDto);
        }
        return result;
    }

    /*
     *
     */
    public Map<String, Object> convertFileData(long fileId, String filename, String fileUrl, String postTitle, String type, Timestamp uploadDate, String companyCode, String uploader) {
        Map<String, Object> row = new HashMap<>();
        row.put("file_id", fileId);
        row.put("file_name", filename);
        row.put("file_url", fileUrl);
        row.put("last_edit_date",  Timestamp.valueOf(LocalDateTime.now()));
        row.put("post_title", postTitle);
        row.put("type", type);
        row.put("upload_date", uploadDate);
        row.put("company_code", companyCode);
        row.put("uploader", uploader);
        return row;
    }

    /*
     * 크롤링 총 수
     */
    public CrwlResDto getCrwlTotal() {
        List<CrwlTotalEntity> carEntity = crwlTotalRepository.findBySubjectOrderByDate("car");
        List<CrwlTotalEntity> beautyEntity = crwlTotalRepository.findBySubjectOrderByDate("beauty");
        List<CrwlTotalDto> car = convertToDtoList(carEntity);
        List<CrwlTotalDto> beauty = convertToDtoList(beautyEntity);
        return CrwlResDto.builder()
                .carCrwlData(car)
                .beautyCrwlData(beauty)
                .build();
    }

    public CrwlTotalDto convertToDto(CrwlTotalEntity entity) {
        return CrwlTotalDto.builder()
                .date(String.valueOf(entity.getDate()))
                .total(entity.getTotal())
                .build();
    }

    public List<CrwlTotalDto> convertToDtoList(List<CrwlTotalEntity> entities) {
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
