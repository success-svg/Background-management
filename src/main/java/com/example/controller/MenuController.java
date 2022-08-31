package com.example.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Constants;
import com.example.common.Result;
import com.example.entity.Dict;
import com.example.mapper.DictMapper;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.service.IMenuService;
import com.example.entity.Menu;

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
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private IMenuService menuService;

    @Resource
    private DictMapper dictMapper;
    //新增或者更新
    @PostMapping
    public boolean save(@RequestBody Menu menu){
        System.out.println("================"+menu);
        return menuService.saveOrUpdate(menu);
        }
    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id){
        return menuService.removeById(id);
        }
    @DeleteMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return Result.success(menuService.removeByIds(ids));
        }
    @GetMapping
    public Result findAll(@RequestParam(defaultValue = "") String name){
        return Result.success(menuService.findMenus(name));
        }
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id){
        return Result.success(menuService.getById(id));
        }
@GetMapping("/page")
public Result findPage(@RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize,
                                 @RequestParam(defaultValue="") String name
                                  ){

    QueryWrapper<Menu> queryWrapper=new QueryWrapper<>();
    queryWrapper.orderByDesc("id");
    queryWrapper.like("name",name);
        return Result.success(menuService.page(new Page<>(pageNum,pageSize),queryWrapper));
        }
    @GetMapping("/icons")
    public Result getIcons()
    {
        QueryWrapper<Dict> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("type", Constants.DICT_TYPE_ICON);
        return Result.success(dictMapper.selectList(null));
    }
        }
