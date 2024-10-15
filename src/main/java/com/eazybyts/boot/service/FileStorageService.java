package com.eazybyts.boot.service;

import org.springframework.web.multipart.MultipartFile;

import com.eazybyts.boot.entity.CarImage;

public interface FileStorageService {
	public CarImage saveFile(MultipartFile file,String filePath);
	public byte[] retreiveFile(String path);
}
