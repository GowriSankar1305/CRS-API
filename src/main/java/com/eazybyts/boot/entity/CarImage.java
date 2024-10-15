package com.eazybyts.boot.entity;

import java.time.LocalDateTime;

import com.eazybyts.boot.constants.CrsConstants;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tbl_car_image",schema = CrsConstants.DB_SCHEMA)
public class CarImage {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "carImageIdGen")
	@SequenceGenerator(name = "carImageIdGen",allocationSize = 1,initialValue = 1000
	,schema = CrsConstants.DB_SCHEMA,sequenceName = "seq_car_image")
	private Long imageId;
	private String originalName;
	private String imageSize;
	private String imageName;
	private String mimeType;
	private String imagePath;
	private String extension;
	private LocalDateTime createdTime;
	private LocalDateTime modifiedTime;
}
