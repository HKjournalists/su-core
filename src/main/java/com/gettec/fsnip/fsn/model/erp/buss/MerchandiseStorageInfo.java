package com.gettec.fsnip.fsn.model.erp.buss;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Transient;
import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.erp.base.MerchandiseInstance;
import com.gettec.fsnip.fsn.model.erp.base.StorageInfo;

/**
 * MerchandiseStorageInfo Entity<br>
 * 商品库存
 * @author Administrator
 */
@Entity(name="T_BUSS_MERCHANDISE_STORAGE_INFO")
public class MerchandiseStorageInfo extends Model implements Serializable {
	private static final long serialVersionUID = -2238591507546106456L;

	@EmbeddedId
	private MerchandiseStorageInfoPK id;
	
	@Column(name="SYS_COUNT")
	private int count;//系统库存
	
	@Column(name="reserves")
	private int reserves;//储备数量
	
	@Column(name="organization")
	private Long organization;

	
	@Transient
	private MerchandiseInstance instance;
	@Transient
	private StorageInfo storage;
	
	public StorageInfo getStorage() {
		return storage;
	}

	public void setStorage(StorageInfo storage) {
		this.storage = storage;
	}

	public MerchandiseInstance getInstance() {
		return instance;
	}

	public Long getOrganization() {
		return organization;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
	}

	public void setInstance(MerchandiseInstance instance) {
		this.instance = instance;
	}

	public MerchandiseStorageInfoPK getId() {
		return id;
	}

	public void setId(MerchandiseStorageInfoPK id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public int getReserves() {
		return reserves;
	}
	
	public void setReserves(int reserves) {
		this.reserves = reserves;
	}

	/**
	 * @param id
	 * @param count
	 */
	public MerchandiseStorageInfo(MerchandiseStorageInfoPK id, int count) {
		super();
		this.id = id;
		this.count = count;
	}

	/**
	 * 
	 */
	public MerchandiseStorageInfo() {
		super();
	}
	
	
	
}
