package net.ooder.nexus.skillcenter.dto.network;

import net.ooder.nexus.skillcenter.dto.common.OperationResultDTO;

public class LinkOperationResultDTO extends OperationResultDTO {

    private String linkId;
    private String operation;

    public LinkOperationResultDTO() {}

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public static LinkOperationResultDTO success(String message, String linkId, String operation) {
        LinkOperationResultDTO result = new LinkOperationResultDTO();
        result.setSuccess(true);
        result.setMessage(message);
        result.setLinkId(linkId);
        result.setOperation(operation);
        return result;
    }

    public static LinkOperationResultDTO failure(String message, String linkId, String operation) {
        LinkOperationResultDTO result = new LinkOperationResultDTO();
        result.setSuccess(false);
        result.setMessage(message);
        result.setLinkId(linkId);
        result.setOperation(operation);
        return result;
    }
}
