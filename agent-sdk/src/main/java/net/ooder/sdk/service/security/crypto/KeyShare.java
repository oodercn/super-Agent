
package net.ooder.sdk.service.security.crypto;

import java.util.Arrays;

public class KeyShare {
    
    private final int shareIndex;
    private final byte[] shareData;
    private final int totalShares;
    private final int threshold;
    
    public KeyShare(int shareIndex, byte[] shareData, int totalShares, int threshold) {
        this.shareIndex = shareIndex;
        this.shareData = shareData.clone();
        this.totalShares = totalShares;
        this.threshold = threshold;
    }
    
    public int getShareIndex() { return shareIndex; }
    public byte[] getShareData() { return shareData.clone(); }
    public int getTotalShares() { return totalShares; }
    public int getThreshold() { return threshold; }
    
    public boolean isValid() {
        return shareData != null && shareData.length > 0 
            && threshold > 0 && totalShares >= threshold;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        KeyShare other = (KeyShare) obj;
        return shareIndex == other.shareIndex && Arrays.equals(shareData, other.shareData);
    }
    
    @Override
    public int hashCode() {
        return 31 * shareIndex + Arrays.hashCode(shareData);
    }
}
