package com.example.service;

import com.example.common.Result;
import com.example.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 超哥
 * @since 2022-08-27
 */
public interface IRoleService extends IService<Role> {

    void setRoleMenu(Integer roleId, List<Integer> menuIds);
    List<Integer> getRoleMenu(Integer roleId);
}
