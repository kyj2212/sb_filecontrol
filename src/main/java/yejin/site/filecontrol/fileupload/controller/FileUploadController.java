package yejin.site.filecontrol.fileupload.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/upload")
@Slf4j
public class FileUploadController {

        @Value("${custom.genFileDirPath}")
        private String uploadDir;

        @PostMapping("")
        public String save(@RequestParam("img1") MultipartFile img1, @RequestParam("img2") MultipartFile img2, @RequestParam("file") MultipartFile file1) {
            log.debug("file: " + img1.getName()+", "+img2.getName());
            if (!img1.isEmpty() && !img2.isEmpty()) {
                try {
                    img1.transferTo(new File(uploadDir + "\\" +img1.getOriginalFilename()));
                    img2.transferTo(new File(uploadDir + "\\" +img2.getOriginalFilename()));
                    file1.transferTo(new File(uploadDir + "\\" + file1.getOriginalFilename()));
                } catch (IOException e) {
                    log.debug("failed file upload");
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
            return "/home/test/upload";
        }
}