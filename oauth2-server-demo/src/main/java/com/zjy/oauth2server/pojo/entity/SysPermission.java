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
 * @since 2021-07-21 15:29 
 */
@Data
@TableName(value ="sys_permission")
public class SysPermission  implements Serializable {

	private static final long serialVersionUID =  8835707452491036203L;

   	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 父权限
	 */
	private Integer parentId;

	/**
	 * 权限名称
	 */
	private String name;

	/**
	 * 权限英文名称
	 */
	private String enname;

	/**
	 * 授权路径
	 */
	private String url;

	/**
	 * 备注
	 */
	private String description;

	private Date created;

	private Date updated;

}
