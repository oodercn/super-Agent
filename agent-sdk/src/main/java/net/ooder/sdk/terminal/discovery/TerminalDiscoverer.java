package net.ooder.sdk.terminal.discovery;

import net.ooder.sdk.terminal.TerminalManager;
import net.ooder.sdk.terminal.model.TerminalDevice;

import java.util.List;

public interface TerminalDiscoverer {
    void startDiscovery();
    void stopDiscovery();
    boolean isDiscoveryRunning();
    List<TerminalDevice> discoverDevices();
    void setTerminalManager(TerminalManager terminalManager);
}
