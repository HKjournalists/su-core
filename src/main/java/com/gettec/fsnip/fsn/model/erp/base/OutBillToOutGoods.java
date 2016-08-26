package com.gettec.fsnip.fsn.model.erp.base;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * OutBillToOutGoods Entity<br>
 * @author Administrator
 */
@Entity(name="T_META_OUTBILL_TO_OUTGOODS")
public class OutBillToOutGoods extends Model {
	private static final long serialVersionUID = 1475173414012284405L;

	@EmbeddedId
	private OutBillToOutGoodsPK id;
	
	@Column(name="IS_DIRECT")
	private boolean isDirect;
	
	public OutBillToOutGoods() {
		super();
	}
	
	public OutBillToOutGoods(OutBillToOutGoodsPK id) {
		this.id = id;
	}
	
	public OutBillToOutGoods(OutBillToOutGoodsPK id, boolean isDirect) {
		this.id = id;
		this.isDirect = isDirect;
	}

	public OutBillToOutGoodsPK getId() {
		return id;
	}

	public void setId(OutBillToOutGoodsPK id) {
		this.id = id;
	}

	public boolean isDirect() {
		return isDirect;
	}

	public void setDirect(boolean isDirect) {
		this.isDirect = isDirect;
	}
}
