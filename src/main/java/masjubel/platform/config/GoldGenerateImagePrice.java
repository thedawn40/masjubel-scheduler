package masjubel.platform.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import masjubel.platform.entity.Harga;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;

import java.awt.image.BufferedImage;
public class GoldGenerateImagePrice {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	public void generate(Harga harga) {
		try {
			Date date = new Date();	
			String jasperReport = getResourcePath();
			
			Map<String,Object> parameters = new HashMap<>();
			parameters.put("tanggal", harga.getTanggal());
			parameters.put("harga_satu", harga.getHargaSatu());
			parameters.put("harga_dua", harga.getHargaDua());
			parameters.put("harga_tiga", harga.getHargaTiga());
			parameters.put("harga_empat", harga.getHargaEmpat());
			parameters.put("harga_lima", harga.getHargaLima());
			parameters.put("harga_enam", harga.getHargaEnam());
			
			JasperReport jr = JasperCompileManager.compileReport(jasperReport);
			JasperPrint print = JasperFillManager.fillReport(jr, parameters, new JREmptyDataSource());
			  
	        DefaultJasperReportsContext.getInstance();   
	        JasperPrintManager printManager = JasperPrintManager.getInstance(DefaultJasperReportsContext.getInstance());      
	 
	        BufferedImage rendered_image = null;      
	        rendered_image = (BufferedImage)printManager.printToImage(print, 0, 1);	

			EmailSender.sendEmail2(rendered_image, generateFilename(date)+".png", harga.getTanggal());

		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void exportImage(BufferedImage rendered_image){
		try {				
			Date date = new Date();	
			String path = getResourceSavePath(date);

			File file = new File(path);  
			OutputStream ouputStream= null;  
			ouputStream= new FileOutputStream(file);   
			ImageIO.write(rendered_image, "png", ouputStream);  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getResourcePath() {
        String resourcePath = Paths.get("src/main/resources/jasper/masjubel_harga.jrxml").toAbsolutePath().toString();
        System.out.println("Resource path: " + resourcePath);
		return resourcePath;
    }

	public String getResourceSavePath(Date date) {
        String resourcePath = Paths.get("src/main/resources/generated/"+generateFilename(date)+".png").toAbsolutePath().toString();
        System.out.println("Resource path: " + resourcePath);
		return resourcePath;
    }

	public String generateFilename(Date date){
		String result = "new-"+sdf.format(date);;
		return result;
	}

	public static void main(String[] args) {
		GoldGenerateImagePrice imagePrice = new GoldGenerateImagePrice();
		imagePrice.generate(GoldPriceFetcher.antamPriceFetch());
	}
    
}
