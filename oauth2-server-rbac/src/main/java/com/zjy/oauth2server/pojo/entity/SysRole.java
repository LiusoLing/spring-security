package com.zjy.oauth2server.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Desc  
 * @author  liugenlai
 * @since 2021-07-21 15:25 
 */
@Data
@TableName(value ="sys_role")
public class SysRole  implements Serializable {

	private static final long serialVersionUID =  5978382683868618671L;

   	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 父角色
	 */
	private Integer parentId;

	/**
	 * 角色名称
	 */
	private String name;

	/**
	 * 角色英文名称
	 */
	private String enname;

	/**
	 * 备注
	 */
	private String description;

	private Date created;

	private Date updated;

}
