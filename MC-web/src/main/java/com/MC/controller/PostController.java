package com.MC.controller;


import com.MC.entity.Msg;
import com.MC.entity.Post;
import com.MC.entity.PostDetai;
import com.MC.service.PostService;
import com.MC.service.TypeService;
import com.MC.sysLog.Logweb;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 文章相关的接口
 */
@RequestMapping("/post")
@RestController
public class PostController {
    @Autowired
    PostService postService;

    @Autowired
    TypeService typeService;

    @Logweb("添加文章")
    @ApiOperation(value = "添加文章")
    @PostMapping("/add")
    public Msg add(@RequestBody Post post){
        postService.addPost(post);
        return Msg.success("添加成功").add("id",post.getId());
    }

    /**
     * 查出所有的文章
     * 分页相关的参数
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/findAll")
    public Msg findAll(@RequestParam(name = "pagenum",defaultValue = "1")Integer page,
                       @RequestParam(name ="pagesize",defaultValue = "5")Integer size){
        return Msg.success().add("postList",postService.findAll(page, size));
    }

    /**
     * 通过文章的id查找除文章的内容
     * @param id
     * @return
     */
    @GetMapping("/findPostById/{id}")
    public Msg findPostById(@PathVariable Integer id){
        PostDetai postDetai=postService.findPostById(id);
        if(postDetai==null)
            return Msg.fail("文章不存在或删除");
        return Msg.success().add("data",postDetai);
    }

    /**
     * 这个也是通过文章的id查找出内容
     * 不过是修改的时候，上面是查看
     * @param id
     * @return
     */
    @GetMapping("/findPostByIds/{id}")
    public Msg findPostByIds(@PathVariable Integer id){
        PostDetai postDetai=postService.findPostByIds(id);
        return Msg.success().add("data",postDetai);
    }

    /**
     * 查找一个类型的文章
     * @param name 文章类型的名字
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/findType/{name}")
    public Msg findTypeById(@PathVariable String name,
                            @RequestParam(name = "pagenum",defaultValue = "1")Integer page,
                            @RequestParam(name ="pagesize",defaultValue = "5")Integer size){
        Integer id=typeService.findTypeIdByName(name);
        if(id==null) return Msg.fail("不存在这个类型");
        return Msg.success().add("postList",postService.findTypeById(id,page, size));
    }

    /**
     * 修改文章的内容
     * @param post
     * @return
     */
    @Logweb("修改文章")
    @PutMapping("/update")
    public Msg update(@RequestBody Post post){
        postService.update(post);
        return Msg.success("修改成功");
    }
    @GetMapping("/rePost/{id}")
    public Msg rePost(@PathVariable Integer id){
        int ids = postService.rePost(id);
        return Msg.success("转发成功").add("id",ids);
    }

}
