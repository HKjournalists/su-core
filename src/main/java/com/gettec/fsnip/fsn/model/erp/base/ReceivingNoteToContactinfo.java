package com.gettec.fsnip.fsn.model.erp.base;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import com.gettec.fsnip.fsn.model.common.Model;

/**
 * ReceivingNoteToContactinfo Entity<br>
 * 收货单-联系人关系表
 * @author Administrator
 */
@Entity(name="t_meta_receivingnote_to_contact")
public class ReceivingNoteToContactinfo extends Model{
	private static final long serialVersionUID = -8302551202107786996L;
	
	@EmbeddedId
	private ReceivingNoteToContactinfoPK id;
	
	
	@Column(name="IS_DIRECT")
	private boolean is_direct;


	public ReceivingNoteToContactinfoPK getId() {
		return id;
	}


	public void setId(ReceivingNoteToContactinfoPK id) {
		this.id = id;
	}


	public boolean isIs_direct() {
		return is_direct;
	}


	public void setIs_direct(boolean is_direct) {
		this.is_direct = is_direct;
	}
	
	public ReceivingNoteToContactinfo(){
		super();
	}
	
	public ReceivingNoteToContactinfo(boolean is_direct){
		super();
		this.is_direct = is_direct;
	}
	/**
	 * @param id
	 */
	public ReceivingNoteToContactinfo(ReceivingNoteToContactinfoPK id){
		super();
		this.id = id;
	}
	
}
