package com.a2823kevin.pdfreader.backend.dto;

import com.a2823kevin.pdfreader.backend.model.UploadPdfStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadPDFResponseDTO {
    private UploadPdfStatus status;
    private String taskId;
    private String message;

    UploadPDFResponseDTO() {}
    UploadPDFResponseDTO(UploadPdfStatus status, String taskId, String message) {
        setStatus(status);
        setTaskId(taskId);
        setMessage(message);
    }

    public static UploadPDFResponseDTO rejectIO() {
        return new UploadPDFResponseDTO(UploadPdfStatus.REJECTED, null, "upload rejected due to io exception");
    }

    public static UploadPDFResponseDTO rejectExtension() {
        return new UploadPDFResponseDTO(UploadPdfStatus.REJECTED, null, "upload rejected due to invalid file extension");
    }
}