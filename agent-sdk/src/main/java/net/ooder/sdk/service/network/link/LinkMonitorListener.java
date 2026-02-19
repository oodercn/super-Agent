
package net.ooder.sdk.service.network.link;

public interface LinkMonitorListener {
    void onLinkQualityChange(Link link, LinkQuality quality, LinkStats stats);
}
