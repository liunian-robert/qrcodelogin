package com.robert.qrcodelogin.common;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 二维码工具类，生成二维码需要依赖zxing包
 * @author zhangyapo
 *
 */
public class QRCodeUtil {

	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;
	private static final String QRCODE_DEFAULT_CHARSET = "UTF-8";
    /**
     * 生成二维码
     * @param matrix
     * @return
     */
    public static BufferedImage toBufferedImage(BitMatrix matrix){
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}
	
    /**
     * 将生成的二维码图片转化为base64编码，直接传给浏览器显示
     * 好处是可以传图片的同时将参数也一并传递过去
     * @param content
     * @param width
     * @param height
     * @return
     */
	public static String toBase64(String content,int width,int height){
		String qrCodeBASE64 = null;
    	try{
	    	//BitMatrix bitMatrix = new MultiFormatWriter().encode(content,BarcodeFormat.QR_CODE , width, height);
			Map hint = new HashMap();
			hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			hint.put(EncodeHintType.CHARACTER_SET, QRCODE_DEFAULT_CHARSET);
			BitMatrix bitMatrix = new MultiFormatWriter().encode(new String(content.getBytes(QRCODE_DEFAULT_CHARSET), QRCODE_DEFAULT_CHARSET), BarcodeFormat.QR_CODE, width, height, hint);
	    	BufferedImage bufferedImage = QRCodeUtil.toBufferedImage(bitMatrix);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage,"png",byteArrayOutputStream);
			byte[] data = byteArrayOutputStream.toByteArray();
			byteArrayOutputStream.flush();
			byteArrayOutputStream.close();
			BASE64Encoder base64 = new BASE64Encoder();
			qrCodeBASE64 = base64.encode(data);
			//解决base64 \r\n问题
			Pattern CRLF = Pattern.compile("(\r\n|\r|\n)");
			Matcher m = CRLF.matcher(qrCodeBASE64);
			if (m.find()) {
				qrCodeBASE64 = m.replaceAll("");
			}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return qrCodeBASE64;
	}
	/**
	 * 将生成的二维码(带logo)图片转化为base64编码，直接传给浏览器显示
	 * 好处是可以传图片的同时将参数也一并传递过去
	 * @param content
	 * @param width
	 * @param height
	 * @return
	 */
	public static String toBase64WithLogo(String content,int width,int height){
		String qrCodeBASE64 = null;
		try{
			//BitMatrix bitMatrix = new MultiFormatWriter().encode(content,BarcodeFormat.QR_CODE , width, height);
			Map hint = new HashMap();
			hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			hint.put(EncodeHintType.CHARACTER_SET, QRCODE_DEFAULT_CHARSET);
			BitMatrix bitMatrix = new MultiFormatWriter().encode(new String(content.getBytes(QRCODE_DEFAULT_CHARSET), QRCODE_DEFAULT_CHARSET), BarcodeFormat.QR_CODE, width, height, hint);
			BufferedImage bufferedImage = QRCodeUtil.toBufferedImage(bitMatrix);
			//添加logo
			InputStream logoFile = QRCodeUtil.class.getClassLoader().getResourceAsStream("logo.png");
			BufferedImage logo = ImageIO.read(logoFile);
			int deltaHeight = height - logo.getHeight();
			int deltaWidth = width - logo.getWidth();

			BufferedImage combined = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D) combined.getGraphics();
			g.drawImage(bufferedImage, 0, 0, null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			g.drawImage(logo, (int) Math.round(deltaWidth / 2), (int) Math.round(deltaHeight / 2), null);
			//添加logo结束
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.write(combined,"png",byteArrayOutputStream);
			byte[] data = byteArrayOutputStream.toByteArray();
			byteArrayOutputStream.flush();
			byteArrayOutputStream.close();
			BASE64Encoder base64 = new BASE64Encoder();
			qrCodeBASE64 = base64.encode(data);
			//解决base64 \r\n问题
			Pattern CRLF = Pattern.compile("(\r\n|\r|\n)");
			Matcher m = CRLF.matcher(qrCodeBASE64);
			if (m.find()) {
				qrCodeBASE64 = m.replaceAll("");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return qrCodeBASE64;
	}

	private static BufferedImage LogoMatrix(BufferedImage matrixImage) throws IOException{
		Graphics2D g2 = matrixImage.createGraphics();
		int matrixWidth = matrixImage.getWidth();
		int matrixHeigh = matrixImage.getHeight();
		/**
		 36
		 * 读取Logo图片
		 37
		 */
		InputStream logoFile = QRCodeUtil.class.getClassLoader().getResourceAsStream("logo.png");
		BufferedImage logo = ImageIO.read(logoFile);
		//开始绘制图片
		g2.drawImage(logo,matrixWidth/5*2,matrixHeigh/5*2, matrixWidth/5, matrixHeigh/5, null);//绘制
		BasicStroke stroke = new BasicStroke(5,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
		g2.setStroke(stroke);// 设置笔画对象
		//指定弧度的圆角矩形
		RoundRectangle2D.Float round = new RoundRectangle2D.Float(matrixWidth/5*2, matrixHeigh/5*2, matrixWidth/5, matrixHeigh/5,20,20);
		g2.setColor(Color.white);
		g2.draw(round);// 绘制圆弧矩形
		//设置logo 有一道灰色边框
		BasicStroke stroke2 = new BasicStroke(1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
		g2.setStroke(stroke2);// 设置笔画对象
		RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(matrixWidth/5*2+2, matrixHeigh/5*2+2, matrixWidth/5-4, matrixHeigh/5-4,20,20);
		g2.setColor(new Color(128,128,128));
		g2.draw(round2);// 绘制圆弧矩形
		g2.dispose();
		matrixImage.flush() ;
		return matrixImage ;
	}
	/**
	 * 生成二维码以流的形式存在
	 * @param matrix
	 * @param format
	 * @param out
	 * @throws IOException
	 */
	public static void writeToFile(BitMatrix matrix, String format, OutputStream out) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        ImageIO.write(image, format, out);
    }
	
	/**
	 * 生成的二维码以文件的形式存在
	 * @param matrix
	 * @param format
	 * @param file
	 * @throws IOException
	 */
	public static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        ImageIO.write(image, format, file);
	}
	
	public static void main(String args[]){
		System.out.println( QRCodeUtil.toBase64("123", 100, 100) );
	}

}
