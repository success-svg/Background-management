package com.example.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.Constants;
import com.example.controller.dto.UserDto;
import com.example.entity.Menu;
import com.example.entity.User;
import com.example.exception.ServiceException;
import com.example.mapper.RoleMapper;
import com.example.mapper.RoleMenuMapper;
import com.example.mapper.UserMapper;
import com.example.service.IMenuService;
import com.example.service.IUserService;
import com.example.util.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 超哥
 * @since 2022-06-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final Log LOG = Log.get();

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private IMenuService menuService;

    @Override
    public UserDto login(UserDto userdto) {
        User one=getUserInfo(userdto);
        if (one != null) {
            BeanUtil.copyProperties(one, userdto, true);
            //设置token
            String token=TokenUtils.genToken(one.getId().toString(),one.getPassword());
            userdto.setToken(token);

            String role=one.getRole();//ROLE_ADMIN

            // 设置用户的菜单列表
            List<Menu> roleMenus = getRoleMenus(role);
            userdto.setMenus(roleMenus);
            return userdto;
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }
    @Override
    public User register(UserDto userDto){
        User one=getUserInfo(userDto);
        if(one == null){
            one=new User();
            BeanUtil.copyProperties(userDto,one,true);
            save(one);//把copy完之后的用户对象存储到数据库
        }else{
            throw new ServiceException(Constants.CODE_600,"用户已存在");
        }
        return one;
    }
    private User getUserInfo(UserDto userdto){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userdto.getUsername());
        queryWrapper.eq("password", userdto.getPassword());
        User one;
        try {
            one = getOne(queryWrapper);//从数据库查询用户信息

        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        return one;
    }
    /**
     * 获取当前角色的菜单列表
     * @param roleFlag
     * @return
     */
    private List<Menu> getRoleMenus(String roleFlag) {
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        // 当前角色的所有菜单id集合
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);

        // 查出系统所有的菜单(树形)
        List<Menu> menus = menuService.findMenus("");
        // new一个最后筛选完成之后的list
        List<Menu> roleMenus = new ArrayList<>();
        // 筛选当前用户角色的菜单
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            // removeIf()  移除 children 里面不在 menuIds集合中的 元素
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }

}
