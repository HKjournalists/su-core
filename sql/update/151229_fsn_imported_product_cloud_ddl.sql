  
  ALTER TABLE imported_product ADD province_id VARCHAR(6) AFTER country_id ;
    ALTER TABLE imported_product ADD city_id VARCHAR(6) AFTER province_id;
      ALTER TABLE imported_product ADD area_id VARCHAR(50) AFTER city_id;