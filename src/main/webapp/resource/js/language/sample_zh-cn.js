(function($){
	var _ = $;
    var fsn = window.fsn = window.fsn || {};
        
    if(!fsn.zh){
        fsn.zh = {};
    }
    
    _.extend(true, fsn.zh, { 
    	    	"ID" : "ID",
		    	"NAME" : "NAME",
		    	"ABSTRACT SAMPLE ID" : "ABSTRACT SAMPLE ID",
		    	"SHEET ID" : "SHEET ID",
		    	"DATA COVERAGE ID" : "DATA COVERAGE ID",
		    	"PRODUCT ID" : "PRODUCT ID",
		    	"BACKUP STORAGE PLACE ID" : "BACKUP STORAGE PLACE ID",
		    	"DISTRICT ID" : "DISTRICT ID",
		    	"VALUE UNIT ID" : "VALUE UNIT ID",
		    	"PROVIDER ID" : "PROVIDER ID",
		    	"ICBUREAU PRODUCT ID" : "ICBUREAU PRODUCT ID",
		    	"VOLUME FOR TEST" : "VOLUME FOR TEST",
		    	"VOLUME FOR BACKUP" : "VOLUME FOR BACKUP",
		    	"CLAIMED BRAND" : "CLAIMED BRAND",
		    	"LEVEL" : "LEVEL",
		    	"TYPE" : "TYPE",
		    	"FORMAT" : "FORMAT",
		    	"REGULARITY" : "REGULARITY",
		    	"BATCH SERIAL NO" : "BATCH SERIAL NO",
		    	"SERIAL" : "SERIAL",
		    	"STATUS" : "STATUS",
		    	"PRICE" : "PRICE",
		    	"INVENTORY VOLUME" : "INVENTORY VOLUME",
		    	"PURCHASE VOLUME" : "PURCHASE VOLUME",
		    	"BARCODE" : "BARCODE",
		    	"NOTES" : "NOTES",
		    	"SAMPLE METHOD ID" : "SAMPLE METHOD ID",
		    	"PRODUCTION DATE" : "PRODUCTION DATE",
		    	"VERSION" : "VERSION",
		    	"DATA" : "DATA",
		    });
    
	$(document).ready(function() {
		fsn.updateLocale();
		console.log(fsn.l("Field Locale"));
	});
    
})(jQuery);