package com.example.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.controller.dto.UserDto;
import com.example.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 超哥
 * @since 2022-06-14
 */
public interface IUserService extends IService<User> {

    UserDto login(UserDto userdto);

    User register(UserDto userDto);

}
