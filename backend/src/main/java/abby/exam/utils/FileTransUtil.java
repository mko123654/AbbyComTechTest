package abby.exam.utils;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class FileTransUtil {

    public static String uploadFile(MultipartFile uploadfile, String dir) {
        log.info("Single file upload!");
        if (uploadfile.isEmpty()) {
            return "請上傳文件";
        }
        try {
            saveUploadedFiles(Arrays.asList(uploadfile), dir);
        } catch (IOException e) {
            e.printStackTrace();
            return "文件上傳失敗";
        }
        log.info("file upload successfully! " + dir);
        return "文件上傳成功";
    }


    public static String uploadFiles(MultipartFile[] uploadfiles, String dir) {
        log.debug("Multiple file upload!");
        String uploadedFileName = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename()).filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));
        if (StringUtils.isEmpty(uploadedFileName)) {
            return "請上傳文件";
        }
        try {
            FileTransUtil.saveUploadedFiles(Arrays.asList(uploadfiles), dir);
        } catch (IOException e) {
            return "文件上傳失敗";
        }
        log.info("file upload successfully! " + uploadedFileName);
        return "文件上傳成功";
    }


    public static void saveUploadedFiles(List<MultipartFile> files, String dir) throws IOException {
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            if (!FileUtil.exist(dir)) {
                FileUtil.mkdir(dir);
            }
            byte[] bytes = file.getBytes();
            String fileName = file.getOriginalFilename().replace("\\", "/");
            if (fileName.lastIndexOf('/')>0){
                String fileDir = dir + "/" + fileName.substring(0, fileName.lastIndexOf('/'));
                if (!FileUtil.exist(fileDir)) {
                    FileUtil.mkdir(fileDir);
                }
            }
            Path path = Paths.get(dir + "/" + fileName);
            Files.write(path, bytes);
        }
    }


    public static ResponseEntity<InputStreamResource> downloadFile(String filePath) throws IOException {
        log.info("downloading file : " + filePath);
        FileSystemResource file = new FileSystemResource(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", new String(file.getFilename().getBytes("gbk"), "iso-8859-1")));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        System.out.println(file.getFilename());
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(file.getInputStream()));
    }

}
