
package net.ooder.sdk.infra.lifecycle;

@FunctionalInterface
public interface ShutdownHook {
    
    void execute() throws Exception;
}
