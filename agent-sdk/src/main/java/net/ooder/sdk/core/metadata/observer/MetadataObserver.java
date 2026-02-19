
package net.ooder.sdk.core.metadata.observer;

import net.ooder.sdk.api.metadata.ChangeRecord;
import net.ooder.sdk.api.metadata.FourDimensionMetadata;

public interface MetadataObserver {
    
    void onMetadataCreated(String resourceId, FourDimensionMetadata metadata);
    
    void onMetadataUpdated(String resourceId, FourDimensionMetadata oldMetadata, FourDimensionMetadata newMetadata);
    
    void onMetadataDeleted(String resourceId);
    
    void onChangeRecorded(ChangeRecord changeRecord);
}
