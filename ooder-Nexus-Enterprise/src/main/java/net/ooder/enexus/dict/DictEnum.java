package net.ooder.enexus.dict;

public interface DictEnum {
    String getCode();
    String getName();
    int getSort();
    default String getValue() { return getCode(); }
    default String getDescription() { return getName(); }
    default boolean isEnabled() { return true; }
}
