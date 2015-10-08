package com.lrc.baseand.entity;

/**
 * 图片项实体类
 * 
 * @author ganyu
 * 
 */
public class ImageItem {
	/** ID */
	private String id;
	/** 缩略图地址 */
	private String thumbnailPath;
	/** 图片地址 */
	private String imagePath;
	/** 是否选中标记（0代表未选中，1代表选中）*/
	private int isSelected;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(int isSelected) {
		this.isSelected = isSelected;
	}

}
