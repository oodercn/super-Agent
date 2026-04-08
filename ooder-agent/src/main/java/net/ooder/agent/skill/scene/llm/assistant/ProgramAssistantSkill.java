package net.ooder.mvp.skill.scene.llm.assistant;

import java.util.Map;
import java.util.Set;

public interface ProgramAssistantSkill {

    ScriptGenerationResult generateScript(ScriptGenerationRequest request);

    CodeCompletionResult completeCode(CodeCompletionRequest request);

    CodeExplanationResult explainCode(CodeExplanationRequest request);

    DebugSuggestionResult suggestDebug(DebugSuggestionRequest request);

    interface ScriptGenerationRequest {
        String getLanguage();
        String getModule();
        String getIntent();
        Map<String, Object> getContext();
        Set<String> getAllowedApis();
    }

    interface CodeCompletionRequest {
        String getLanguage();
        String getCode();
        int getCursorPosition();
        Map<String, Object> getContext();
    }

    interface CodeExplanationRequest {
        String getLanguage();
        String getCode();
        String getExplanationType();
    }

    interface DebugSuggestionRequest {
        String getLanguage();
        String getCode();
        String getErrorMessage();
        Map<String, Object> getContext();
    }

    interface ScriptGenerationResult {
        boolean isSuccess();
        String getScript();
        String getScriptType();
        String getModule();
        String getExplanation();
        String getError();
    }

    interface CodeCompletionResult {
        boolean isSuccess();
        String getCompletion();
        String getExplanation();
        String getError();
    }

    interface CodeExplanationResult {
        boolean isSuccess();
        String getExplanation();
        String getError();
    }

    interface DebugSuggestionResult {
        boolean isSuccess();
        String getSuggestion();
        String getFixedCode();
        String getError();
    }
}
