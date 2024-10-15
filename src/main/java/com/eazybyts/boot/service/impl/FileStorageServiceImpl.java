package com.eazybyts.boot.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eazybyts.boot.entity.CarImage;
import com.eazybyts.boot.exception.InvalidInputException;
import com.eazybyts.boot.service.FileStorageService;
import com.eazybyts.boot.util.CrsUtility;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {
	
	@Autowired
	private CrsUtility crsUtility;
	
	@Override
	public CarImage saveFile(MultipartFile file, String filePath) {	
		CarImage image = null;
		String imagePath = "";
		if(Objects.isNull(file) || file.getSize() == 0)	{
			throw new InvalidInputException(List.of("Invalid file. no data to process.."));
		}
		String fileId = UUID.randomUUID().toString();
		String originalFilename = file.getOriginalFilename();
		log.info("irignal name-------------------> {}",originalFilename);
		String extenSion = originalFilename.split("\\.")[1];
		imagePath = filePath + fileId + "." + extenSion;
		log.info("image path---------------> {}",imagePath);
		try(FileOutputStream fos = new FileOutputStream(new File(imagePath)))	{
			fos.write(file.getBytes());
			image = new CarImage();
			image.setCreatedTime(LocalDateTime.now());
			image.setModifiedTime(LocalDateTime.now());
			image.setExtension(extenSion);
			image.setImageName(fileId);
			image.setMimeType(crsUtility.findMimeType(extenSion));
			image.setOriginalName(file.getOriginalFilename());
			image.setImageSize(crsUtility.getReadableFileSize(file.getSize()));
			image.setImagePath(imagePath);
		}
		catch(Exception e)	{
			image = null;
			log.error("error while uploading: " ,e);
		}
		return image;
	}

	@Override
	public byte[] retreiveFile(String path) {
		byte[] content = null;
		try	{
			content = Files.readAllBytes(Paths.get(path));
		}
		catch(Exception e)	{
			content = null;
			log.error("error while downloading: {}",e);
		}
		return content;
	}

}
