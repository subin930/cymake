package CY.cymake.Domain.Drive;

import CY.cymake.AWS.S3Service;
import CY.cymake.Domain.Auth.Dto.CustomUserInfoDto;
import CY.cymake.Domain.Drive.Dto.PostListResDto;
import CY.cymake.Domain.Drive.Dto.PostSearchResultDto;
import CY.cymake.Entity.CompanyEntity;
import CY.cymake.Entity.FileEntity;
import CY.cymake.Entity.UsersEntity;
import CY.cymake.Exception.FileDeleteFailedException;
import CY.cymake.Exception.FileUpdateFailedException;
import CY.cymake.Exception.UserNotFoundException;
import CY.cymake.OpenSearch.DataExtractor;
import CY.cymake.OpenSearch.OpenSearchService;
import CY.cymake.Repository.FileRepository;
import CY.cymake.Repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DriveService {
    private final S3Service s3Service;
    private final UsersRepository usersRepository;
    private final FileRepository fileRepository;
    private final OpenSearchService openSearchService;
    private final DataExtractor dataExtractor;

    public String uploadFile(CustomUserInfoDto user, MultipartFile multipartFile, String postTitle) throws IOException {
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
        return fileUrl;
    }
    /*
     * 파일 다운로드
     */
    public ResponseEntity<byte[]> download(CustomUserInfoDto user, String filename) throws IOException {
        String directory = "files/" + user.getCompanyCode().getCode() + "/";
        return s3Service.download(directory, filename);
    }
    /*
     * 파일 삭제
     */
    public void deleteFile(CustomUserInfoDto user, String filename) throws IOException {
        //input 1) CustomUserInfoDto user:로그인 되어 있는 유저 정보 2) String filename: 삭제할 파일 이름 ex. example_text.txt
        FileEntity file = fileRepository.findByFile(filename).orElseThrow(() -> new FileDeleteFailedException("해당 파일이 존재하지 않습니다."));
        //1. 버킷의 특정 디렉터리로 매핑
        String directory = "files/" + user.getCompanyCode().getCode() + "/";

        //2. 작성자와 일치하는지 확인
        if(!user.getId().equals(file.getUploader().getId())){
            //일치하지 않을 경우
            throw new FileDeleteFailedException("파일 삭제에 실패하였습니다.");
        }

        //3. s3에서 삭제 로직 수행
        s3Service.deleteFile(directory, filename);

        //4. db에서 삭제 로직 수행
        fileRepository.delete(file);
    }
    /*
     * 파일 수정
     */
    public void updateFile(CustomUserInfoDto user, MultipartFile newFile, String originalFilename, String postTitle) throws IOException {
        FileEntity file = fileRepository.findByFile(originalFilename)
                .orElseThrow(() -> new FileUpdateFailedException("파일 수정에 실패했습니다."));

        //1. 작성자 일치 여부 확인
        if(!user.getId().equals(file.getUploader().getId())) {
            throw new FileUpdateFailedException("기존 post가 존재하지 않습니다.");
        }
        //2. 파일 수정 여부 확인
        if(newFile == null) {
            file.updatePostTitle(postTitle);
            fileRepository.save(file);
            return;
        }
        //2. s3에서 파일 수정(기존거 삭제, 새로운거 올림)
        String directory = "files/" +  user.getCompanyCode().getCode() + "/";
        String fileUrl = s3Service.updateFile(newFile, directory, originalFilename);

        //3. db수정
        file.updatePost(postTitle, newFile.getOriginalFilename(), fileUrl, getExtension(Objects.requireNonNull(newFile.getOriginalFilename())));
        fileRepository.save(file);
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
    public List<PostListResDto> getPostList(CustomUserInfoDto user) throws IOException, Exception {
        openSearchService.deleteFileIndex(); //test 용. 실제 코드에서는 삭제
        openSearchService.bulkUploadData(dataExtractor.extractFileData(), "tb_file", "file_id");
        String directory = "files/" + user.getCompanyCode().getCode() + "/";

        List<FileEntity> files = fileRepository.findAll();
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
    public List<PostSearchResultDto> searchPost(String searchBody) throws IOException {
        String[] col = {"file_name"};
        return openSearchService.searchFileTb("tb_file", searchBody);

    }
    /*
     * PostListDto -> PostSearchResultDto
     */
    public List<PostListResDto> changeToPostList(CustomUserInfoDto user, List<PostSearchResultDto> list) throws IOException {
        String directory = "files/" + user.getCompanyCode().getCode() + "/";
        List<PostListResDto> result = new ArrayList<>();
        for(PostSearchResultDto post: list) {
            PostListResDto postListResDto = new PostListResDto(post.getFile_name(), post.getPost_title(), post.getFile_url(), post.getFile_id(), post.getUploader(), post.getUpload_date(), s3Service.getFileSize(directory,post.getFile_name()));
            result.add(postListResDto);
        }
        return result;
    }
}
