package com.Lin;

public class Qrcode {
	private String content;    	//��ά������    
	private String filePath;   	//�ļ����·��
	private String fileName;   	//�ļ�����
	private int width;         	//ͼ����  
	private int height;  	   	//ͼ��߶�  
	private String format;     	//ͼ������,eg:png
	private int onColor = 0xFF000000;  //Ĭ��Ϊ��
	private int offColor = 0xFFFFFFFF; //������ɫ,Ĭ��Ϊ��
	
	//�޲εĹ��캯��
	public Qrcode(){}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}

	public int getOnColor() {
		return onColor;
	}

	public void setOnColor(int onColor) {
		this.onColor = onColor;
	}

	public int getOffColor() {
		return offColor;
	}

	public void setOffColor(int offColor) {
		this.offColor = offColor;
	}
	
	

}
