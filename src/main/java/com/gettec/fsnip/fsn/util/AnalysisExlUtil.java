package com.gettec.fsnip.fsn.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.LicenseInfo;

public class AnalysisExlUtil {
	
	
	public static List<BusinessUnit> getBuListByExlByte(byte[] fByte){
		File file = null;  
        BufferedOutputStream stream = null;  
        try {  
        	file = new File("/b.xls");  
            FileOutputStream fstream = new FileOutputStream(file);  
            stream = new BufferedOutputStream(fstream);  
            stream.write(fByte);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (stream != null) {  
                try {  
                    stream.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        
        List<BusinessUnit> bus=new ArrayList<BusinessUnit>();
        try {
        	Workbook workbook = Workbook.getWorkbook(file);
			Sheet sheet = workbook.getSheet(0);
			Cell cell = null;
			int rowCount = sheet.getRows();
			int columnCount = sheet.getColumns();
			for(int i = 1; i < rowCount; i++){
				BusinessUnit bu=new BusinessUnit();
				bu.setLicense(new LicenseInfo());
				for(int j = 0; j < columnCount; j++){
					cell=sheet.getCell(j, i);
					if(j==0){
						bu.setName(cell.getContents());
					}
					if(j==1){
						bu.setAddress(cell.getContents());
					}
					if(j==2){
						bu.getLicense().setLicenseNo(cell.getContents());
					}
				}
				bus.add(bu);
			}
			
        }catch(Exception e){
			e.printStackTrace();
		}
		
		
		return bus;		
	}
}
