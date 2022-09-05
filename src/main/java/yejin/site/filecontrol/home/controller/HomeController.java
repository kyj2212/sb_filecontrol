package yejin.site.filecontrol.home.controller;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    @Value("${custom.genFileDirPath}")
    private String uploadDir;
    @RequestMapping("/")
    public String main(){
        return "home/main";
    }
    @RequestMapping("/test/upload")
    public String upload(){
        return "home/test/upload";
    }
    @PostMapping("/test/upload")
    public String save(@RequestParam("img1") MultipartFile img1, @RequestParam("img2") MultipartFile img2) {
        log.debug("file: "+img1.getName());
        log.info("file: "+img1.getName());
        if(!img1.isEmpty()){
            String filename = img1.getOriginalFilename();
            String fullPath = uploadDir + "/"+filename;
            log.debug("uploaddir : "+fullPath);
            log.info("uploaddir : "+fullPath);
            try {
                img1.transferTo(new File(fullPath));
            } catch (IOException e) {
                log.debug("failed file upload");
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return "/home/test/upload";
    }
}
