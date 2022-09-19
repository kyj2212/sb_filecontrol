package yejin.site.filecontrol.base;

import org.springframework.beans.factory.annotation.Value;

public class AppConfig {
    public static String GET_FILE_DIR_PATH;

    @Value("${custom.genFileDirPath}")
    public void setFileDirPath(String genFileDirPath) {
        GET_FILE_DIR_PATH = genFileDirPath;
    };
}
