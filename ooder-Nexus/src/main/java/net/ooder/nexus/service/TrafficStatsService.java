package net.ooder.nexus.service;

import net.ooder.nexus.domain.network.model.TrafficStats;

import java.util.List;

/**
 * 流量统计服务接口
 */
public interface TrafficStatsService {
    
    /**
     * 获取所有设备流量统计
     */
    List<TrafficStats> getAllTrafficStats();
    
    /**
     * 获取总带宽统计
     */
    BandwidthSummary getBandwidthSummary();
    
    /**
     * 获取带宽趋势数据
     */
    List<BandwidthTrend> getBandwidthTrend(String period);
    
    /**
     * 更新流量统计
     */
    TrafficStats updateTrafficStats(TrafficStats stats);
    
    /**
     * 初始化默认数据
     */
    void initDefaultData();
    
    /**
     * 带宽汇总信息
     */
    class BandwidthSummary {
        private double todayUsage;
        private double monthUsage;
        private double maxUpload;
        private double maxDownload;
        
        public double getTodayUsage() {
            return todayUsage;
        }
        
        public void setTodayUsage(double todayUsage) {
            this.todayUsage = todayUsage;
        }
        
        public double getMonthUsage() {
            return monthUsage;
        }
        
        public void setMonthUsage(double monthUsage) {
            this.monthUsage = monthUsage;
        }
        
        public double getMaxUpload() {
            return maxUpload;
        }
        
        public void setMaxUpload(double maxUpload) {
            this.maxUpload = maxUpload;
        }
        
        public double getMaxDownload() {
            return maxDownload;
        }
        
        public void setMaxDownload(double maxDownload) {
            this.maxDownload = maxDownload;
        }
    }
    
    /**
     * 带宽趋势数据点
     */
    class BandwidthTrend {
        private String time;
        private double upload;
        private double download;
        
        public BandwidthTrend(String time, double upload, double download) {
            this.time = time;
            this.upload = upload;
            this.download = download;
        }
        
        public String getTime() {
            return time;
        }
        
        public void setTime(String time) {
            this.time = time;
        }
        
        public double getUpload() {
            return upload;
        }
        
        public void setUpload(double upload) {
            this.upload = upload;
        }
        
        public double getDownload() {
            return download;
        }
        
        public void setDownload(double download) {
            this.download = download;
        }
    }
}
