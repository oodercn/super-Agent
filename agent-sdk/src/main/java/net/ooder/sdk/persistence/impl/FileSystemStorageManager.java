package net.ooder.sdk.persistence.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import net.ooder.sdk.persistence.StorageManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FileSystemStorageManager implements StorageManager {
    private final String storagePath;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
    private final Map<String, Object> memoryCache = new HashMap<>();
    private boolean initialized = false;
    private boolean inTransaction = false;
    private final List<Runnable> transactionActions = new ArrayList<>();
    
    public FileSystemStorageManager(String storagePath) {
        this.storagePath = storagePath;
    }
    
    @Override
    public CompletableFuture<Boolean> initialize() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Path path = Paths.get(storagePath);
                if (!Files.exists(path)) {
                    Files.createDirectories(path);
                }
                initialized = true;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> shutdown() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                executorService.shutdown();
                executorService.awaitTermination(10, TimeUnit.SECONDS);
                initialized = false;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }
    
    @Override
    public <T> CompletableFuture<Boolean> save(String key, T value) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (inTransaction) {
                    transactionActions.add(() -> saveSync(key, value));
                    return true;
                } else {
                    return saveSync(key, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }
    
    @Override
    public <T> CompletableFuture<T> load(String key, Class<T> clazz) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // 先从内存缓存中读取
                if (memoryCache.containsKey(key)) {
                    Object value = memoryCache.get(key);
                    if (clazz.isInstance(value)) {
                        return clazz.cast(value);
                    }
                }
                
                // 从文件中读取
                Path filePath = getFilePath(key);
                if (!Files.exists(filePath)) {
                    return null;
                }
                
                byte[] contentBytes = Files.readAllBytes(filePath);
                String content = new String(contentBytes, StandardCharsets.UTF_8);
                T value = JSON.parseObject(content, clazz);
                
                // 放入内存缓存
                memoryCache.put(key, value);
                
                return value;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> delete(String key) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (inTransaction) {
                    transactionActions.add(() -> deleteSync(key));
                    return true;
                } else {
                    return deleteSync(key);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> exists(String key) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // 先从内存缓存中检查
                if (memoryCache.containsKey(key)) {
                    return true;
                }
                
                // 从文件中检查
                Path filePath = getFilePath(key);
                return Files.exists(filePath);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }
    
    @Override
    public CompletableFuture<Long> size() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Path path = Paths.get(storagePath);
                return Files.walk(path)
                    .filter(Files::isRegularFile)
                    .count();
            } catch (Exception e) {
                e.printStackTrace();
                return 0L;
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> clear() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Path path = Paths.get(storagePath);
                Files.walk(path)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            Files.delete(file);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                
                // 清空内存缓存
                memoryCache.clear();
                
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }
    
    @Override
    public <T> CompletableFuture<Boolean> saveAll(Map<String, T> entries) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                for (Map.Entry<String, T> entry : entries.entrySet()) {
                    saveSync(entry.getKey(), entry.getValue());
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }
    
    @Override
    public <T> CompletableFuture<Map<String, T>> loadAll(Class<T> clazz) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Map<String, T> result = new HashMap<>();
                Path path = Paths.get(storagePath);
                
                Files.walk(path)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            String key = file.getFileName().toString();
                            byte[] contentBytes = Files.readAllBytes(file);
                            String content = new String(contentBytes, StandardCharsets.UTF_8);
                            T value = JSON.parseObject(content, clazz);
                            result.put(key, value);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return Collections.emptyMap();
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> deleteAll(Iterable<String> keys) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                for (String key : keys) {
                    deleteSync(key);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> backup(String backupPath) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Path source = Paths.get(storagePath);
                Path target = Paths.get(backupPath);
                
                if (!Files.exists(target)) {
                    Files.createDirectories(target);
                }
                
                // 复制所有文件
                Files.walk(source)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            Path relativePath = source.relativize(file);
                            Path targetFile = target.resolve(relativePath);
                            Files.createDirectories(targetFile.getParent());
                            Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> restore(String backupPath) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Path source = Paths.get(backupPath);
                Path target = Paths.get(storagePath);
                
                if (!Files.exists(source)) {
                    return false;
                }
                
                // 清空目标目录
                Files.walk(target)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            Files.delete(file);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                
                // 复制所有文件
                Files.walk(source)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            Path relativePath = source.relativize(file);
                            Path targetFile = target.resolve(relativePath);
                            Files.createDirectories(targetFile.getParent());
                            Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                
                // 清空内存缓存
                memoryCache.clear();
                
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> scheduleBackup(long period, TimeUnit unit) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String backupDir = storagePath + File.separator + "backups";
                Path backupPath = Paths.get(backupDir);
                if (!Files.exists(backupPath)) {
                    Files.createDirectories(backupPath);
                }
                
                executorService.scheduleAtFixedRate(() -> {
                    try {
                        String timestamp = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                        String backupFilePath = backupDir + File.separator + "backup-" + timestamp;
                        backup(backupFilePath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, 0, period, unit);
                
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> cancelBackupSchedule() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                executorService.shutdown();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }
    
    @Override
    public CompletableFuture<Map<String, Object>> getStorageInfo() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Map<String, Object> info = new HashMap<>();
                Path path = Paths.get(storagePath);
                
                long fileCount = Files.walk(path)
                    .filter(Files::isRegularFile)
                    .count();
                
                long totalSize = Files.walk(path)
                    .filter(Files::isRegularFile)
                    .mapToLong(file -> {
                        try {
                            return Files.size(file);
                        } catch (Exception e) {
                            return 0;
                        }
                    })
                    .sum();
                
                info.put("storagePath", storagePath);
                info.put("fileCount", fileCount);
                info.put("totalSize", totalSize);
                info.put("initialized", initialized);
                info.put("inTransaction", inTransaction);
                info.put("cacheSize", memoryCache.size());
                
                return info;
            } catch (Exception e) {
                e.printStackTrace();
                return Collections.emptyMap();
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> compact() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // 这里可以添加压缩逻辑，例如清理过期数据等
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> validate() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // 这里可以添加验证逻辑，例如检查文件完整性等
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> repair() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // 这里可以添加修复逻辑，例如修复损坏的文件等
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> beginTransaction() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                inTransaction = true;
                transactionActions.clear();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> commitTransaction() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                for (Runnable action : transactionActions) {
                    action.run();
                }
                inTransaction = false;
                transactionActions.clear();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> rollbackTransaction() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                inTransaction = false;
                transactionActions.clear();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }
    
    @Override
    public boolean isInTransaction() {
        return inTransaction;
    }
    
    // 内部同步方法
    private <T> boolean saveSync(String key, T value) {
        try {
            Path filePath = getFilePath(key);
            Files.createDirectories(filePath.getParent());
            
            String content = JSON.toJSONString(value);
            Files.write(filePath, content.getBytes(StandardCharsets.UTF_8));
            
            // 更新内存缓存
            memoryCache.put(key, value);
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean deleteSync(String key) {
        try {
            Path filePath = getFilePath(key);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
            
            // 从内存缓存中删除
            memoryCache.remove(key);
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private Path getFilePath(String key) {
        // 替换非法字符
        String safeKey = key.replaceAll("[<>:\\\"\\|?*]", "_");
        return Paths.get(storagePath, safeKey);
    }
}
