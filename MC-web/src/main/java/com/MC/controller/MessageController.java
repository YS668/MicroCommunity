package com.MC.controller;

import com.MC.entity.Message;
import com.MC.entity.MessageType;
import com.MC.entity.Msg;
import com.MC.service.MessageService;
import com.MC.sysLog.Logweb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 消息相关的接口
 */
@RequestMapping("/message")
@RestController
public class MessageController {
    @Autowired
    MessageService messageService;

    /**
     * 查询是否有消息未查看的
     * @return
     */
    @GetMapping("/exists")
    public Msg exists(){
        return Msg.success().add("exists",messageService.exitMessage());
    }

    /**
     * 返回消息的所有类型
     * @return
     */
    @GetMapping("/findMessageTypeNum")
    public Msg findMessageTypeNum(){
        List<MessageType> messageTypeNum = messageService.findMessageTypeNum();
        return Msg.success().add("data",messageTypeNum);
    }

    /**
     * 查出某一类型的消息
     * @param id
     * @return
     */
    @GetMapping("/findMessageById/{id}")
    public Msg findMessageById(@PathVariable Integer id){
        List<Message> messageList = messageService.findMessageByTypeId(id);
        return Msg.success().add("list",messageList);
    }

    /**
     * 消息标记为已读
     * @param id 消息的id
     * @return
     */
    @Logweb("已读消息")
    @GetMapping("/read/{id}")
    public Msg read(@PathVariable Integer id){
        boolean up = messageService.update(id,2);
        return up?Msg.success("success"):Msg.fail("error");
    }

    /**
     * 删除消息的id
     * @param id
     * @return
     */
    @Logweb("删除消息")
    @GetMapping("/delete/{id}")
    public Msg delete(@PathVariable Integer id){
        boolean up = messageService.update(id,0);
        return up?Msg.success("success"):Msg.fail("error");
    }
}
