package com.zyh.mapper;

import com.zyh.entity.Type;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeMapper {

    List<Type> findTypeList();

    List<Integer> findTypeId();

    @Select("select * from type")
    @Results(value = {
            @Result(id = true,column = "id",property = "id"),
            @Result(property = "name",column = "name"),
            @Result(property = "type_num",column = "id",one = @One(select = "com.zyh.mapper.TypeMapper.findOneTypeNums"))
    })
    List<Type> findTypeListNums();

    @Select("select count(*) as type_num from post where type_id=#{id} and post.status!=0")
    Integer findOneTypeNums(int id);

    @Select("select id from type where name=#{name}")
    Integer findTypeIdByName(String name);
}
