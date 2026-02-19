
package net.ooder.sdk.core.skill.installer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DownloadStage implements InstallStage {
    
    private static final Logger log = LoggerFactory.getLogger(DownloadStage.class);
    
    private static final int BUFFER_SIZE = 8192;
    private static final int CONNECT_TIMEOUT = 30000;
    private static final int READ_TIMEOUT = 60000;
    
    private String downloadDir;
    private int connectTimeout = CONNECT_TIMEOUT;
    private int readTimeout = READ_TIMEOUT;
    
    public DownloadStage() {
        this.downloadDir = System.getProperty("java.io.tmpdir") + File.separator + "ooder-downloads";
    }
    
    public DownloadStage(String downloadDir) {
        this.downloadDir = downloadDir;
    }
    
    @Override
    public String getName() {
        return "download";
    }
    
    @Override
    public void execute(InstallContext context) throws Exception {
        log.debug("Downloading skill package for: {}", context.getSkillId());
        
        context.setStatus(InstallStatus.DOWNLOADING);
        
        String sourceUrl = context.getRequest().getSource();
        if (sourceUrl == null || sourceUrl.isEmpty()) {
            throw new InstallException("Source URL is required for download");
        }
        
        File downloadDir = ensureDownloadDirectory();
        String fileName = extractFileName(sourceUrl, context.getSkillId());
        File targetFile = new File(downloadDir, fileName);
        
        context.setRollbackData("downloadFile", targetFile.getAbsolutePath());
        
        long startTime = System.currentTimeMillis();
        long bytesDownloaded = downloadFile(sourceUrl, targetFile, context);
        long duration = System.currentTimeMillis() - startTime;
        
        context.setProperty("downloaded", true);
        context.setProperty("downloadTime", System.currentTimeMillis());
        context.setProperty("downloadFile", targetFile.getAbsolutePath());
        context.setProperty("downloadSize", bytesDownloaded);
        context.setProperty("downloadDuration", duration);
        context.addInstalledFile(targetFile.getAbsolutePath());
        
        log.info("Download completed for {}: {} bytes in {}ms", 
            context.getSkillId(), bytesDownloaded, duration);
    }
    
    private File ensureDownloadDirectory() throws IOException {
        File dir = new File(downloadDir);
        if (!dir.exists()) {
            Files.createDirectories(dir.toPath());
        }
        return dir;
    }
    
    private String extractFileName(String url, String skillId) {
        int lastSlash = url.lastIndexOf('/');
        if (lastSlash >= 0 && lastSlash < url.length() - 1) {
            String name = url.substring(lastSlash + 1);
            if (name.contains(".")) {
                return skillId + "-" + name;
            }
        }
        return skillId + "-" + System.currentTimeMillis() + ".zip";
    }
    
    private long downloadFile(String sourceUrl, File targetFile, InstallContext context) 
            throws InstallException {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        
        try {
            URL url = new URL(sourceUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(connectTimeout);
            connection.setReadTimeout(readTimeout);
            connection.setRequestProperty("User-Agent", "OoderSDK/0.7.0");
            
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new InstallException("Download failed with HTTP code: " + responseCode);
            }
            
            long contentLength = connection.getContentLengthLong();
            context.setProperty("contentLength", contentLength);
            
            inputStream = new BufferedInputStream(connection.getInputStream());
            outputStream = new FileOutputStream(targetFile);
            
            byte[] buffer = new byte[BUFFER_SIZE];
            long totalRead = 0;
            int bytesRead;
            
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalRead += bytesRead;
                
                if (contentLength > 0) {
                    int progress = (int) ((totalRead * 100) / contentLength);
                    context.setProperty("downloadProgress", progress);
                }
            }
            
            outputStream.flush();
            return totalRead;
            
        } catch (IOException e) {
            if (targetFile.exists()) {
                targetFile.delete();
            }
            throw new InstallException("Download failed: " + e.getMessage(), e);
        } finally {
            if (outputStream != null) {
                try { outputStream.close(); } catch (IOException e) { }
            }
            if (inputStream != null) {
                try { inputStream.close(); } catch (IOException e) { }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    
    public void setDownloadDir(String downloadDir) {
        this.downloadDir = downloadDir;
    }
    
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
    
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
}
