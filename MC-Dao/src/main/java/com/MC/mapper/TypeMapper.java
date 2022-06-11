package com.MC.mapper;

import com.MC.entity.Type;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
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
            @Result(property = "type_num",column = "id",one = @One(select = "com.MC.mapper.TypeMapper.findOneTypeNums"))
    })
    List<Type> findTypeListNums();

    @Select("select count(*) as type_num from post where type_id=#{id} and post.status!=0")
    Integer findOneTypeNums(int id);

    @Select("select id from type where name=#{name}")
    Integer findTypeIdByName(String name);
}
