package com.gettec.fsnip.fsn.model.test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.gettec.fsnip.fsn.model.common.Model;

@Entity(name="t_batch_sample")
public class TBatchSample extends Model {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue
	private Long id;
	@Column(name="name", columnDefinition="TEXT", nullable=false)
	private String name;
	@Column(name="abstract_sample_id", nullable=true)
	private Integer abstractSampleId;
	@Column(name="sheet_id", nullable=true)
	private Integer sheetId;
	@Column(name="data_coverage_id", nullable=true)
	private Integer dataCoverageId;
	@Column(name="product_id", nullable=true)
	private Integer productId;
	@Column(name="backup_storage_place_id", nullable=true)
	private Integer backupStoragePlaceId;
	@Column(name="district_id", nullable=true)
	private Integer districtId;
	@Column(name="value_unit_id", nullable=true)
	private Integer valueUnitId;
	@Column(name="provider_id", nullable=true)
	private Integer providerId;
	@Column(name="icbureau_product_id", nullable=true)
	private Integer icbureauOroductId;
	@Column(name="volume_for_test", columnDefinition="TEXT", nullable=false)
	private String volumeForTest;
	@Column(name="volume_for_backup", columnDefinition="TEXT", nullable=false)
	private String volumeForBackup;
	@Column(name="claimed_brand", columnDefinition="TEXT", nullable=false)
	private String claimedBrand;
	@Column(name="level", columnDefinition="TEXT", nullable=false)
	private String level;
	@Column(name="type", columnDefinition="TEXT", nullable=false)
	private String type;
	@Column(name="format", columnDefinition="TEXT", nullable=false)
	private String format;
	@Column(name="regularity", columnDefinition="TEXT", nullable=false)
	private String regularity;
	@Column(name="batch_serial_no", columnDefinition="TEXT", nullable=false)
	private String batchSerialNo;
	@Column(name="serial", columnDefinition="TEXT", nullable=false)
	private String serial;
	@Column(name="status", columnDefinition="TEXT", nullable=false)
	private String status;
	@Column(name="price", columnDefinition="TEXT", nullable=false)
	private String price;
	@Column(name="inventory_volume", columnDefinition="TEXT", nullable=false)
	private String inventoryVolume;
	@Column(name="purchase_volume", columnDefinition="TEXT", nullable=false)
	private String purchaseVolume;
	@Column(name="barcode", columnDefinition="TEXT", nullable=false)
	private String barcode;
	@Column(name="notes", columnDefinition="TEXT", nullable=false)
	private String notes;
	@Column(name="sample_method_id", nullable=true)
	private Integer sampleMethodId;
	@Column(name="production_date", columnDefinition="TEXT", nullable=false)
	private String productionDate;
	@Column(name="version", nullable=true)
	private Long version;
	@Column(name="data", columnDefinition="TEXT", nullable=false)
	private String data;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAbstractSampleId() {
		return abstractSampleId;
	}
	public void setAbstractSampleId(Integer abstractSampleId) {
		this.abstractSampleId = abstractSampleId;
	}
	public Integer getSheetId() {
		return sheetId;
	}
	public void setSheetId(Integer sheetId) {
		this.sheetId = sheetId;
	}
	public Integer getDataCoverageId() {
		return dataCoverageId;
	}
	public void setDataCoverageId(Integer dataCoverageId) {
		this.dataCoverageId = dataCoverageId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getBackupStoragePlaceId() {
		return backupStoragePlaceId;
	}
	public void setBackupStoragePlaceId(Integer backupStoragePlaceId) {
		this.backupStoragePlaceId = backupStoragePlaceId;
	}
	public Integer getDistrictId() {
		return districtId;
	}
	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}
	public Integer getValueUnitId() {
		return valueUnitId;
	}
	public void setValueUnitId(Integer valueUnitId) {
		this.valueUnitId = valueUnitId;
	}
	public Integer getProviderId() {
		return providerId;
	}
	public void setProviderId(Integer providerId) {
		this.providerId = providerId;
	}
	public Integer getIcbureauOroductId() {
		return icbureauOroductId;
	}
	public void setIcbureauOroductId(Integer icbureauOroductId) {
		this.icbureauOroductId = icbureauOroductId;
	}
	public String getVolumeForTest() {
		return volumeForTest;
	}
	public void setVolumeForTest(String volumeForTest) {
		this.volumeForTest = volumeForTest;
	}
	public String getVolumeForBackup() {
		return volumeForBackup;
	}
	public void setVolumeForBackup(String volumeForBackup) {
		this.volumeForBackup = volumeForBackup;
	}
	public String getClaimedBrand() {
		return claimedBrand;
	}
	public void setClaimedBrand(String claimedBrand) {
		this.claimedBrand = claimedBrand;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getRegularity() {
		return regularity;
	}
	public void setRegularity(String regularity) {
		this.regularity = regularity;
	}
	public String getBatchSerialNo() {
		return batchSerialNo;
	}
	public void setBatchSerialNo(String batchSerialNo) {
		this.batchSerialNo = batchSerialNo;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getInventoryVolume() {
		return inventoryVolume;
	}
	public void setInventoryVolume(String inventoryVolume) {
		this.inventoryVolume = inventoryVolume;
	}
	public String getPurchaseVolume() {
		return purchaseVolume;
	}
	public void setPurchaseVolume(String purchaseVolume) {
		this.purchaseVolume = purchaseVolume;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public Integer getSampleMethodId() {
		return sampleMethodId;
	}
	public void setSampleMethodId(Integer sampleMethodId) {
		this.sampleMethodId = sampleMethodId;
	}
	public String getProductionDate() {
		return productionDate;
	}
	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public TBatchSample() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TBatchSample(Long id, String name, Integer abstractSampleId,
			Integer sheetId, Integer dataCoverageId, Integer productId,
			Integer backupStoragePlaceId, Integer districtId,
			Integer valueUnitId, Integer providerId, Integer icbureauOroductId,
			String volumeForTest, String volumeForBackup, String claimedBrand,
			String level, String type, String format, String regularity,
			String batchSerialNo, String serial, String status, String price,
			String inventoryVolume, String purchaseVolume, String barcode,
			String notes, Integer sampleMethodId, String productionDate,
			Long version, String data) {
		super();
		this.id = id;
		this.name = name;
		this.abstractSampleId = abstractSampleId;
		this.sheetId = sheetId;
		this.dataCoverageId = dataCoverageId;
		this.productId = productId;
		this.backupStoragePlaceId = backupStoragePlaceId;
		this.districtId = districtId;
		this.valueUnitId = valueUnitId;
		this.providerId = providerId;
		this.icbureauOroductId = icbureauOroductId;
		this.volumeForTest = volumeForTest;
		this.volumeForBackup = volumeForBackup;
		this.claimedBrand = claimedBrand;
		this.level = level;
		this.type = type;
		this.format = format;
		this.regularity = regularity;
		this.batchSerialNo = batchSerialNo;
		this.serial = serial;
		this.status = status;
		this.price = price;
		this.inventoryVolume = inventoryVolume;
		this.purchaseVolume = purchaseVolume;
		this.barcode = barcode;
		this.notes = notes;
		this.sampleMethodId = sampleMethodId;
		this.productionDate = productionDate;
		this.version = version;
		this.data = data;
	}
	@Override
	public String toString() {
		return "TBatchSample [id=" + id + ", name=" + name
				+ ", abstractSampleId=" + abstractSampleId + ", sheetId="
				+ sheetId + ", dataCoverageId=" + dataCoverageId
				+ ", productId=" + productId + ", backupStoragePlaceId="
				+ backupStoragePlaceId + ", districtId=" + districtId
				+ ", valueUnitId=" + valueUnitId + ", providerId=" + providerId
				+ ", icbureauOroductId=" + icbureauOroductId
				+ ", volumeForTest=" + volumeForTest + ", volumeForBackup="
				+ volumeForBackup + ", claimedBrand=" + claimedBrand
				+ ", level=" + level + ", type=" + type + ", format=" + format
				+ ", regularity=" + regularity + ", batchSerialNo="
				+ batchSerialNo + ", serial=" + serial + ", status=" + status
				+ ", price=" + price + ", inventoryVolume=" + inventoryVolume
				+ ", purchaseVolume=" + purchaseVolume + ", barcode=" + barcode
				+ ", notes=" + notes + ", sampleMethodId=" + sampleMethodId
				+ ", productionDate=" + productionDate + ", version=" + version
				+ ", data=" + data + "]";
	}
	
	
	
	
}
