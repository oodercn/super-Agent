package net.ooder.sdk.api.msg;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface MsgClientProxy<V> {
    
    String getClientId();
    
    String getSceneId();
    
    String getGroupId();
    
    void initialize(MsgClientConfig config);
    
    void start();
    
    void stop();
    
    boolean isRunning();
    
    CompletableFuture<V> getMsgById(String msgId);
    
    CompletableFuture<List<V>> getAllSendMsg();
    
    CompletableFuture<List<V>> getAllReceiveMsg();
    
    CompletableFuture<List<V>> getMsgList(Map<String, Object> condition);
    
    CompletableFuture<V> createMsg();
    
    CompletableFuture<V> createMsg(String toPersonId);
    
    CompletableFuture<V> createMsg(String toPersonId, Map<String, Object> content);
    
    CompletableFuture<V> updateMsg(V msg);
    
    CompletableFuture<Void> sendMsg(V msg);
    
    CompletableFuture<Map<String, Boolean>> sendMassMsg(V msg, List<String> personIds);
    
    CompletableFuture<Void> deleteMsg(String msgId);
    
    void addMessageListener(MessageListener<V> listener);
    
    void removeMessageListener(MessageListener<V> listener);
    
    interface MessageListener<V> {
        void onMessageReceived(V msg);
        void onMessageSent(V msg);
        void onMessageDeleted(String msgId);
    }
}
