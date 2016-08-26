package com.gettec.fsnip.fsn.test.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring-context.xml"})
public abstract class AbstractServiceTest {

	private final static Logger logger = Logger.getLogger(AbstractServiceTest.class);
	
	//NESCAFE
	protected final Long BUSINESS_UNIT_ID = 10l;
	//NESCAFE
	protected final Long BUSINESS_BRAND_ID = 10l;
	//咖啡
	protected final Long PRODUCT_ID_1 = 1l;
	//雀巢咖啡
	protected final Long PRODUCT_ID_2 = 2l;
	
	protected final Long FDA_ID = 1l;
	
	protected final Long TEST_LAB_ID = 1l;
	

	protected List<Properties> parseDSFile(String dsFile, String columnCVS){
		List<Properties> result = null;
		Properties row = null;
		String[] columns = StringUtils.commaDelimitedListToStringArray(columnCVS);
		try {
			List<String> lines = FileUtils.readLines(new File(this.getClass().getClassLoader().getResource(dsFile).getFile()), "utf-8");
			if(lines != null && lines.size() > 0){
				result = new ArrayList<Properties>();
				for(String line :  lines){
					logger.info("Parsing line[" + line + "]");
					String[] colVals = StringUtils.commaDelimitedListToStringArray(line);
					if(colVals != null && colVals.length == columns.length){
						row = new Properties();
						for(int i = 0; i < columns.length; i++){
							row.put(columns[i].trim(), colVals[i]);
						}
					}
					result.add(row);
					logger.info("Parsed line[" + row + "]");
				}
			}
		} catch (Exception e) {
			logger.error("Error occurs on parsing ds file:" + dsFile, e);
		}
		
		return result;
	}
}
