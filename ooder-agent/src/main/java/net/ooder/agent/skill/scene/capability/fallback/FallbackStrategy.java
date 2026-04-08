package net.ooder.mvp.skill.scene.capability.fallback;

public enum FallbackStrategy {
    RETRY,
    FALLBACK_PROVIDER,
    CACHE,
    DEFAULT_VALUE,
    SKIP,
    FAIL_FAST
}
