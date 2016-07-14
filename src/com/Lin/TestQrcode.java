package com.Lin;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
/**
 * ʹ��google��Դ��Zxing
 * ������logo�Ķ�ά�����ɼ�ʶ��
 * @author LIn
 *
 */
public class TestQrcode {
	  
	/**
	 * ���ɶ�ά��ͼ��
	 * @param q          ��ά��ģ��
	 * @param logoPath   ����logo�Ĵ洢·��,Ϊnullʱû����logo
	 * @throws WriterException
	 * @throws IOException
	 */
	public void encode(Qrcode q, String logoPath) throws WriterException, IOException {	
        BitMatrix bitMatrix;    

        Hashtable<EncodeHintType, Integer> hints = new Hashtable<>();  
        hints.put(EncodeHintType.MARGIN, 1); //���ö�ά��հױ߿�Ĵ�С 1-4��1����С 4��Ĭ�ϵĹ���

        //���ɾ���
        bitMatrix = new MultiFormatWriter().encode(new String(q.getContent().getBytes("UTF-8"),"iso-8859-1"),    
                BarcodeFormat.QR_CODE, q.getWidth(), q.getHeight(), hints);  

        //�洢·��
        Path path = FileSystems.getDefault().getPath(q.getFilePath(), q.getFileName()); 
        
        //������ɫ����
        MatrixToImageConfig config = new MatrixToImageConfig(q.getOnColor(), q.getOffColor());
        
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix, config);
        
        if(null == logoPath){   //����Ҫ����logo
        	ImageIO.write(image, q.getFormat(), new File(path.toString()));
        }else{
        	this.overlapImage(image, path.toString(), logoPath, q.getFormat());
        }

        System.out.println("success");
   
    }

	/**
	 * �����ɵĶ�ά���������ͼ��
	 * @param image
	 * @param imgSavePath
	 * @param logoPath
	 * @param format
	 */
	public void overlapImage(BufferedImage image, String imgSavePath, String logoPath, String format) {
		try {
			BufferedImage logo = scale(logoPath, image.getWidth() / 5,
					image.getHeight() / 5, true);
			Graphics2D g = image.createGraphics();
			//logo���
			int width = image.getWidth() / 5;
			int height = image.getHeight() / 5;
			//logo��ʼλ�ã���Ŀ����Ϊlogo������ʾ
			int x = (image.getWidth() - width) / 2;
			int y = (image.getHeight() - height) / 2;
			g.drawImage(logo, x, y, width, height, null);
			g.dispose();
			ImageIO.write(image, format, new File(imgSavePath));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �Ѵ����ԭʼͼ�񰴸߶ȺͿ�Ƚ������ţ����ɷ���Ҫ���ͼ��
	 * @param srcImageFile  Դ�ļ���ַ
	 * @param height        Ŀ��߶�
	 * @param width         Ŀ����
	 * @param hasFiller     ��������ʱ�Ƿ���Ҫ���ף�trueΪ����; falseΪ������;
	 * @throws IOException
	 */
	private static BufferedImage scale(String srcImageFile, int height,
			int width, boolean hasFiller) throws IOException {
		double ratio = 0.0; // ���ű���
		File file = new File(srcImageFile);
		BufferedImage srcImage = ImageIO.read(file);
		Image destImage = srcImage.getScaledInstance(width, height,
				BufferedImage.SCALE_SMOOTH);
		// �������
		if ((srcImage.getHeight() > height) || (srcImage.getWidth() > width)) {
			if (srcImage.getHeight() > srcImage.getWidth()) {
				ratio = (new Integer(height)).doubleValue()
						/ srcImage.getHeight();
			} else {
				ratio = (new Integer(width)).doubleValue()
						/ srcImage.getWidth();
			}
			AffineTransformOp op = new AffineTransformOp(
					AffineTransform.getScaleInstance(ratio, ratio), null);
			destImage = op.filter(srcImage, null);
		}
		if (hasFiller) {// ����
			BufferedImage image = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D graphic = image.createGraphics();
			graphic.setColor(Color.white);
			graphic.fillRect(0, 0, width, height);
			if (width == destImage.getWidth(null))
				graphic.drawImage(destImage, 0,
						(height - destImage.getHeight(null)) / 2,
						destImage.getWidth(null), destImage.getHeight(null),
						Color.white, null);
			else
				graphic.drawImage(destImage,
						(width - destImage.getWidth(null)) / 2, 0,
						destImage.getWidth(null), destImage.getHeight(null),
						Color.white, null);
			graphic.dispose();
			destImage = image;
		}
		return (BufferedImage) destImage;
	}
	 
	 /** 
	 * ����ͼ��
	 * @param  filePath  ��ά����·��
	 * @throws IOException 
	 * @throws NotFoundException 
	 */  
	 public void decode(String filePath) throws IOException, NotFoundException {
	     BufferedImage image;
	     
	     image = ImageIO.read(new File(filePath));  
         LuminanceSource source = new BufferedImageLuminanceSource(image);  
         Binarizer binarizer = new HybridBinarizer(source);  
         BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);  
         Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();  
         hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");  
         Result result = new MultiFormatReader().decode(binaryBitmap, hints);// ��ͼ����н���  

         System.out.println("ͼƬ�����ݣ�  ");  
         System.out.println(result.getText()); 
     }

  
}  
