package com.eazybyts.boot.dto;

import lombok.Data;

@Data
public class ImageDto {

	private Long imageId;
	private byte[] content;
	private String mimeType;
	private String assignedFileName;
	private String originalFileName;
}
