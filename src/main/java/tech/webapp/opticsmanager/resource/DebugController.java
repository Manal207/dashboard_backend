package tech.webapp.opticsmanager.resource; // adjust package name

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DebugController {

    @Value("${spring.datasource.url:NOT_SET}")
    private String datasourceUrl;

    @Value("${spring.datasource.username:NOT_SET}")
    private String datasourceUsername;

    @Value("${DATABASE_URL:NOT_SET}")
    private String databaseUrl;

    @Value("${MYSQL_USER:NOT_SET}")
    private String mysqlUser;

    @GetMapping("/debug/config")
    public Map<String, String> debugConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("datasource.url", datasourceUrl);
        config.put("datasource.username", datasourceUsername);
        config.put("DATABASE_URL", databaseUrl);
        config.put("MYSQL_USER", mysqlUser);
        config.put("java.version", System.getProperty("java.version"));
        return config;
    }
}