package com.MC.controller;

import com.github.pagehelper.PageInfo;
import com.MC.entity.Msg;
import com.MC.entity.PostDetai;
import com.MC.entity.ReportDeatil;
import com.MC.entity.User;
import com.MC.service.AdminService;
import com.MC.service.PostService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 后台接口
 */
@RequestMapping("/admin")
@CrossOrigin
@RestController
public class AdminController {
//    @RequiresRoles("admin")
//    @GetMapping("/admin/test")
//    public Msg test(){
//        return Msg.success("admin");
//    }

    @Autowired
    AdminService adminService;
    @Autowired
    PostService postService;

    /**
     * 查找出所有用户
     * @param page
     * @param size
     * @return
     */
    @RequiresRoles("admin")
    @GetMapping("/getUser")
    public Msg getUser( @RequestParam(name = "pagenum",defaultValue = "1")Integer page,
                        @RequestParam(name ="pagesize",defaultValue = "5")Integer size){
        PageInfo all = adminService.findAll(page, size);
        return Msg.success().add("userlist",all);
    }

    /**
     * 通过用户ID 搜索用户
     * @param id
     * @return
     */
    @RequiresRoles("admin")
    @GetMapping("/getOne/{id}")
    public Msg findOne(@PathVariable Integer id){
        User user= adminService.findById(id);
        List<User> userList=new ArrayList<>();
        if(user!=null)
            userList.add(user);
        return Msg.success().add("userlist",userList);
    }

    /**
     * 修改用户状态
     * @param id
     * @return
     */
    @RequiresRoles("admin")
    @GetMapping("/changeStatus/{id}")
    public Msg changeStatus(@PathVariable Integer id){
        boolean b = adminService.changeStatus(id);
        return b?Msg.success("修改成功"):Msg.fail("修改失败");
    }

    /**
     * 找出所有文章
     * @param page
     * @param size
     * @return
     */
    @RequiresRoles("admin")
    @GetMapping("/getPost")
    public Msg getPost(@RequestParam(name = "pagenum",defaultValue = "1")Integer page,
                       @RequestParam(name ="pagesize",defaultValue = "5")Integer size){
        return Msg.success("").add("postlist",postService.findAll(page, size));
    }

    /**
     * 通过文章ID进行查找
     * @param id
     * @return
     */
    @RequiresRoles("admin")
    @GetMapping("/findOne/{id}")
    public Msg getOne(@PathVariable Integer id){
        PostDetai post = postService.findPostById(id);
        List<PostDetai> postList=new ArrayList<>();
        if(post!=null)
            postList.add(post);
        return Msg.success().add("postlist",postList);
    }

    /**
     * 修改文章的状态
     * @param id
     * @return
     */
    @RequiresRoles("admin")
    @GetMapping("/changePostStatus/{id}")
    public Msg changePostStatus(@PathVariable Integer id){
        Boolean b=postService.changeStatus(id);
        return Msg.success();
    }

    /**
     * 找出所有未处理的举报
     * @return
     */
    @RequiresRoles("admin")
    @GetMapping("/findAllReport")
    public Msg findAllReport(){
        List<ReportDeatil> allReport = adminService.findAllReport();
        return Msg.success().add("reportList",allReport);
    }

    /**
     * 把举报标记为已处理
     * @param id
     * @return
     */
    @RequiresRoles("admin")
    @PutMapping("/solve/{id}")
    public Msg solve(@PathVariable Integer id){
        adminService.solve(id);
        return Msg.success("success");
    }


    @RequiresRoles("admin")
    @PutMapping("/deleteSolve/{id}")
    public Msg deleteSolve(@PathVariable Integer id){
        adminService.deleteSolve(id);
        return Msg.success("success");
    }


}
