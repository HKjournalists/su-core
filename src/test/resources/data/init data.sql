--business_brands.
select distinct claimed_brand, claimed_brand, claimed_brand
from t_batch_sample 
where claimed_brand is not null 
and claimed_brand <> '' 
order by claimed_brand;

--business_units.txt
select distinct claimed_brand
from t_batch_sample 
where claimed_brand is not null 
and claimed_brand <> '' 
order by claimed_brand;

--products.txt
select distinct name, status, format, regularity, barcode, claimed_brand
from t_batch_sample 
where claimed_brand is not null 
and claimed_brand <> '' 
and barcode is not null
and barcode <> ''
order by claimed_brand;

--product_instances.txt
select distinct batch_serial_no, serial, production_date, barcode
from t_batch_sample 
where claimed_brand is not null 
and claimed_brand <> '' 
and barcode is not null
and barcode <> ''
and serial is not null
and serial <> ''
order by serial;