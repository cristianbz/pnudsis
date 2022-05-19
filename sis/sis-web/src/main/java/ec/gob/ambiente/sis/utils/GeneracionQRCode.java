/**
@autor proamazonia [Christian Báez]  17 may. 2022

**/
package ec.gob.ambiente.sis.utils;

import java.io.Serializable;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class GeneracionQRCode implements Serializable{

	private static final long serialVersionUID = 1L;

	public static byte[] getQRCodeImage(String text, int width, int height, String logoQR) throws WriterException, IOException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

		ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

		ByteArrayOutputStream os = new ByteArrayOutputStream();

		byte[] pngData = pngOutputStream.toByteArray();
		if (logoQR != null) {
			InputStream in = new ByteArrayInputStream(pngData);
			BufferedImage qrImage = ImageIO.read(in);
			BufferedImage overly = getOverly(logoQR);
			int deltaHeight = qrImage.getHeight() - overly.getHeight();
			int deltaWidth = qrImage.getWidth() - overly.getWidth();
			BufferedImage combined = new BufferedImage(qrImage.getHeight(), qrImage.getWidth(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D) combined.getGraphics();
			g.drawImage(qrImage, 0, 0, null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			g.drawImage(overly, (int) Math.round(deltaWidth / 2), (int) Math.round(deltaHeight / 2), null);
			ImageIO.write(combined, "png", os);
			return os.toByteArray();
		}
		return pngOutputStream.toByteArray();
	}

	private static BufferedImage getOverly(String LOGO) throws IOException {
		File file = new File(LOGO);
		return ImageIO.read(file);
	}
}

