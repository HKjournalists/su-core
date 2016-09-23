package com.gettec.fsnip.fsn.model.video;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import com.gettec.fsnip.fsn.model.common.Model;

/**
 * Enterprise_video Entity<br>
 * @author litg
 */

@Entity(name = "enterprise_video")
public class Enterprise_video extends Model{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "org_id")
	private String org_id;     // 企业id
	
	@Column(name = "video_url")
	private String video_url;	 //视频地址
	
	@Column(name = "video_type")
	private String video_type;     //视频类别（1：实时视频、2：文件视频）
	
	@Column(name = "video_desc")
	private String video_desc;     // 视频简短描述
	
	@Column(name = "sort")
	private int sort;        // 排序
	
	@Column(name = "is_show")
	private String is_show;  // 是否展示（0：不展示、1：展示）
	
	@Column(name = "create_user")
	private String create_user;     // 创建者
	
	@Column(name = "create_time")
	private String create_time;      // 创建时间
	
	@Column(name = "last_modify_user")
	private String last_modify_user;   //最后更新者
	
	@Column(name = "last_modify_time")
	private String last_modify_time;      // 最后更新时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrg_id() {
		return org_id;
	}

	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}

	public String getVideo_url() {
		return video_url;
	}

	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}

	public String getVideo_type() {
		return video_type;
	}

	public void setVideo_type(String video_type) {
		this.video_type = video_type;
	}

	public String getVideo_desc() {
		return video_desc;
	}

	public void setVideo_desc(String video_desc) {
		this.video_desc = video_desc;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getIs_show() {
		return is_show;
	}

	public void setIs_show(String is_show) {
		this.is_show = is_show;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getLast_modify_user() {
		return last_modify_user;
	}

	public void setLast_modify_user(String last_modify_user) {
		this.last_modify_user = last_modify_user;
	}

	public String getLast_modify_time() {
		return last_modify_time;
	}

	public void setLast_modify_time(String last_modify_time) {
		this.last_modify_time = last_modify_time;
	}
}