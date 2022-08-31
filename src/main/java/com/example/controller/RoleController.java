package com.example.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.service.IRoleService;
import com.example.entity.Role;

import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 超哥
 * @since 2022-08-27
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private IRoleService roleService;

    //新增或者更新
    @PostMapping
    public boolean save(@RequestBody Role role){
        return roleService.saveOrUpdate(role);
        }
    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id){
        return roleService.removeById(id);
        }
    @DeleteMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        return roleService.removeByIds(ids);
        }
    @GetMapping
    public Result findAll() {
        return Result.success(roleService.list());
    }
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id){
        return Result.success(roleService.getById(id));
        }
    @GetMapping("/page")
    public Result findPage(@RequestParam String name,
                       @RequestParam Integer pageNum,
                       @RequestParam Integer pageSize){

    QueryWrapper<Role> queryWrapper=new QueryWrapper<>();
    queryWrapper.like("name",name);
    queryWrapper.orderByDesc("id");
        return Result.success(roleService.page(new Page<>(pageNum,pageSize),queryWrapper));
        }

    @PostMapping("/roleMenu/{roleId}")
    public Result roleMenu(@PathVariable Integer roleId,@RequestBody List<Integer> menuIds){
        roleService.setRoleMenu(roleId,menuIds);
        return Result.success();
    }

    @GetMapping("/roleMenu/{roleId}")
    public Result getRoleMenu(@PathVariable Integer roleId){
        return Result.success(roleService.getRoleMenu(roleId));
    }


        }
