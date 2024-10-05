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
import CY.cymake.Exception.FileUploadFailedException;
import CY.cymake.Exception.UserNotFoundException;
import CY.cymake.OpenSearch.DataExtractor;
import CY.cymake.OpenSearch.OpenSearchService;
import CY.cymake.Repository.CompanyRepository;
import CY.cymake.Repository.CrwlTotalRepository;
import CY.cymake.Repository.FileRepository;
import CY.cymake.Repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
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
    private final CompanyRepository companyRepository;
    //private final double basic_usage = 3;
    //private final double premium_usage = 6;
    private final double basic_usage = 1536;
    private final double standard_usage = 3072;
    private final double premium_usage = 4068;
    /*
     * 파일 업로드
     * 1. 파일 작성자가 실제 유저인지 확인
     * 2. 파일 이름 변경 -> 랜덤으로
     * 추가 로직: 용량 확인
     * 3. s3에 업로드
     * 4. db에 저장
     * 5. opensearch 인덱스에 저장
     */
    @Transactional
    public String uploadFile(CustomUserInfoDto user, MultipartFile file, String postTitle) throws IOException, Exception {
        CompanyEntity companyCode = user.getCompanyCode(); //db에 저장할 해당 유저의 회사 코드
        double usage;
        if(companyCode.getPlan().equals("basic")){
            usage = basic_usage;
        } else if (companyCode.getPlan().equals("standard")){
            usage = standard_usage;
        }else {
            usage = premium_usage;
        }
        //1. 파일 작성자가 실제 유저인지 확인
        Optional<UsersEntity> siteUser= usersRepository.findById(user.getId());

        if(siteUser.isEmpty()) {
            //유저가 존재하지 않는다면
            throw new UserNotFoundException("파일 업로드에 실패했습니다.");
        }
        //2. 파일 이름 변경 -> 랜덤으로
        double size = calSize(file.getSize());
        System.out.println(size);

        //추가 로직: 용량 확인
        if(companyCode.getCurrent_usage() + size > usage){
            throw new FileUploadFailedException("용량이 부족합니다.");
        }

        //3. s3에 업로드
        String s3Fn = createRandomFilename(Objects.requireNonNull(file.getOriginalFilename()));
        String path = "files/" + companyCode.getCode() + "/" + s3Fn; //s3 버킷 파일 저장 경로
        String fileUrl = s3Service.uploadFile(file, path);

        //4.db에 저장
        //1) 파일 db에 업로드
        FileEntity fileEntity = FileEntity.builder()
                .companyCode(companyCode)
                .postTitle(postTitle)
                .s3Fn(s3Fn)
                .originalFn(file.getOriginalFilename())
                .fileUrl(fileUrl)
                .uploader(siteUser.get())
                .uploadDate(Timestamp.valueOf(LocalDateTime.now()))
                .lastEditDate(Timestamp.valueOf(LocalDateTime.now()))
                .type(getExtension(Objects.requireNonNull(file.getOriginalFilename())))
                .size(s3Service.getFileSize(path))
                .build();
        FileEntity savedFile = fileRepository.save(fileEntity);

        //2) 해당 회사의 용량에 파일 용량 추가
        CompanyEntity companyEntity = user.getCompanyCode();
        companyEntity.uddUsage(fileEntity.getSize());
        companyRepository.save(companyEntity);

        //5. opensearch에 업로드
        openSearchService.addAndUpdateFileData(convertFileData(savedFile.getId(), savedFile.getS3Fn(), savedFile.getOriginalFn(), savedFile.getFileUrl(), savedFile.getPostTitle(), savedFile.getType(), savedFile.getUploadDate(), savedFile.getCompanyCode().getCode(), savedFile.getUploader().getId(), savedFile.getSize()), "file_id");

        return fileUrl;
    }

    /*
     * 파일 삭제
     * 1. 파일 아이디로 해당 파일 찾음
     * 2. 글 작성자와 해당 유저가 일치하는지 확인
     * 3. opensearch 인덱스에서 삭제
     * 4. s3에서 삭제
     * 5. db에서 삭제
     */
    @Transactional
    public void deleteFile(CustomUserInfoDto user, Long fileId) throws IOException {
        //input 1) CustomUserInfoDto user:로그인 되어 있는 유저 정보 2) String filename: 삭제할 파일 이름 ex. example_text.txt
        //1. 파일 아이디로 해당 파일 찾음
        FileEntity fileEntity = fileRepository.findByCompanyCodeAndId(user.getCompanyCode(), fileId).orElseThrow(() -> new FileDeleteFailedException("파일이 존재하지 않습니다."));


        //2. 작성자와 일치하는지 확인
        if(!user.getId().equals(fileEntity.getUploader().getId())){
            //일치하지 않을 경우
            throw new FileDeleteFailedException("파일 삭제에 실패하였습니다.");
        }
        //3. opensearch 인덱스에서 삭제
        openSearchService.deleteFileData(fileEntity.getId());

        //3. s3에서 삭제 로직 수행
        String path = "files/" + user.getCompanyCode().getCode() + "/" + fileEntity.getS3Fn();
        s3Service.deleteFile(path);

        //4. db에서 삭제 로직 수행
        fileRepository.delete(fileEntity);

        CompanyEntity companyEntity = user.getCompanyCode();
        companyEntity.deleteUsage(fileEntity.getSize());
        companyRepository.save(companyEntity);
    }

    /*
     * 파일 수정
     * 1. 작성자 일치 여부 확인
     * 2. postTitle만 수정한 경우 처리
     * 3. 다른 파일을 올렸을 경우 처리
     * 추가 로직: 용량 확인
     * 3-1. s3에서 파일 수정(기존 파일 삭제, 새로운 파일 업로드)
     * 3-2. db에서 데이터 수정
     * 3-3. opensearch에서 데이터 수정
     */
    @Transactional
    public void updateFile(CustomUserInfoDto user, MultipartFile newFile, String postTitle, Long fileId) throws IOException {
        CompanyEntity companyEntity = user.getCompanyCode();
        double usage;
        if(companyEntity.getPlan().equals("basic")){
            usage = basic_usage;
        } else if (companyEntity.getPlan().equals("standard")){
            usage = standard_usage;
        }else {
            usage = premium_usage;
        }
        FileEntity fileEntity = fileRepository.findByCompanyCodeAndId(user.getCompanyCode(), fileId).orElseThrow(() -> new FileUpdateFailedException("파일이 존재하지 않습니다."));

        //1. 작성자 일치 여부 확인
        if(!user.getId().equals(fileEntity.getUploader().getId())) {
            throw new FileUpdateFailedException("작성자가 일치하지 않습니다.");
        }
        //2. postTitle만 수정한 경우 처리
        if(newFile == null) {
            fileEntity.updatePostTitle(postTitle);
            fileRepository.save(fileEntity);
            Map<String, Object> data = convertFileData(fileEntity.getId(), fileEntity.getS3Fn(), fileEntity.getOriginalFn(),
                    fileEntity.getFileUrl(), postTitle,
                    fileEntity.getType(), fileEntity.getUploadDate(), fileEntity.getCompanyCode().getCode(),
                    fileEntity.getUploader().getId(), fileEntity.getSize());
            openSearchService.addAndUpdateFileData(data, "file_id");
            return;
        }
        // 3. 다른 파일을 올렸을 경우 처리

        //추가 로직: 용량 확인
        double size = calSize(newFile.getSize());

        if(companyEntity.getCurrent_usage() - fileEntity.getSize() + size > usage){
            throw new FileUpdateFailedException("용량이 부족합니다.");
        }

        //3-1. s3에서 파일 수정(기존 파일 삭제, 새로운 파일 업로드)
        String newS3Fn = createRandomFilename(Objects.requireNonNull(newFile.getOriginalFilename()));
        String path = "files/" + user.getCompanyCode().getCode() + "/" + fileEntity.getS3Fn();
        String newPath = "files/" + user.getCompanyCode().getCode() + "/" + newS3Fn;
        String newFileUrl = s3Service.updateFile(newFile, path, newPath);

        //3-2. db에서 데이터 수정
        companyEntity.updateUsage(fileEntity.getSize(), size);
        companyRepository.save(companyEntity);

        fileEntity.updatePost(postTitle, newFile.getOriginalFilename(), newS3Fn, newFileUrl, getExtension(Objects.requireNonNull(newFile.getOriginalFilename())), size);
        fileRepository.save(fileEntity);



        //3-3. opensearch에서 데이터 수정
        Map<String, Object> data = convertFileData(fileEntity.getId(), fileEntity.getS3Fn(), fileEntity.getOriginalFn()
                , newFileUrl, postTitle, getExtension(Objects.requireNonNull(newFile.getOriginalFilename())),
                fileEntity.getUploadDate(), fileEntity.getCompanyCode().getCode(),
                fileEntity.getUploader().getId(), fileEntity.getSize());
        openSearchService.addAndUpdateFileData(data, "file_id");
    }

    /*
     * 중복되는 이름의 파일 충돌 방지 코드 -> 적용 여부 후에 논의
     */
    @Transactional
    public String createRandomFilename(String originalFilename) {
        int idx = originalFilename.lastIndexOf(".");
        String extension = originalFilename.substring(idx + 1);
        return UUID.randomUUID() + "." + extension;
    }
    /*
     * 확장자 추출
     */
    @Transactional
    public String getExtension(String originalFilename) {
        int idx = originalFilename.lastIndexOf(".");
        return originalFilename.substring(idx + 1);
    }

    /*
     * post 리스트 전송
     */
    @Transactional
    public List<PostListResDto> getPostList(CustomUserInfoDto user) throws Exception {
        //openSearchService.deleteFileIndex(); //test 용. 실제 코드에서는 삭제
        //openSearchService.createFileTb(); //test 용.
        String directory = "files/" + user.getCompanyCode().getCode() + "/";

        List<FileEntity> files = fileRepository.findAllByCompanyCode(user.getCompanyCode());
        List<PostListResDto> posts = new ArrayList<>();
        for(FileEntity file: files) {
            PostListResDto post = PostListResDto.builder()
                    .fileId(file.getId())
                    .fileName(file.getOriginalFn())
                    .postTitle(file.getPostTitle())
                    .uploader(file.getUploader().getId())
                    .username(file.getUploader().getUsername())
                    .fileUrl(file.getFileUrl())
                    .uploadDate(file.getUploadDate())
                    .size(file.getSize())
                    .build();
            posts.add(post);
        }
        return posts;
    }
    /*
     * post 검색
     */
    @Transactional
    public List<PostListResDto> searchPost(CustomUserInfoDto user, String searchBody) throws Exception {
        return changeToPostList(user, openSearchService.searchFileTb(user, "tb_file", searchBody.toLowerCase()));

    }
    /*
     * PostListDto -> PostSearchResultDto
     */
    @Transactional
    public List<PostListResDto> changeToPostList(CustomUserInfoDto user, List<PostSearchResultDto> list) throws IOException {
        String directory = "files/" + user.getCompanyCode().getCode() + "/";
        List<PostListResDto> result = new ArrayList<>();
        for(PostSearchResultDto post: list) {
            String username = usersRepository.findById(post.getUploader()).orElseThrow(() -> new UserNotFoundException("파일 업로더가 존재하지 않습니다.")).getUsername();
            PostListResDto postListResDto = new PostListResDto(post.getFile_id(), post.getOriginal_fn(), post.getPost_title(), post.getFile_url(), post.getUploader(), username, post.getUpload_date(), post.getSize());
            result.add(postListResDto);
        }
        return result;
    }

    /*
     * opensearch에 파일 업로드 또는 수정할 때 필요한 코드
     */
    @Transactional
    public Map<String, Object> convertFileData(long fileId, String s3Fn, String originalFn, String fileUrl, String postTitle, String type, Timestamp uploadDate, String companyCode, String uploader, Double size) {
        Map<String, Object> row = new HashMap<>();
        row.put("file_id", fileId);
        row.put("s3_fn", s3Fn);
        row.put("original_fn", originalFn);
        row.put("file_url", fileUrl);
        row.put("last_edit_date",  Timestamp.valueOf(LocalDateTime.now()));
        row.put("post_title", postTitle);
        row.put("type", type);
        row.put("upload_date", uploadDate);
        row.put("company_code", companyCode);
        row.put("uploader", uploader);
        row.put("size", size);
        return row;
    }

    public double calSize(double size){
        size = size / (1024.0 * 1024.0);
        DecimalFormat df = new DecimalFormat("#.###");
        return Double.parseDouble(df.format(size));
    }
}
