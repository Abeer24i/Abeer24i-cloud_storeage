package com.cloudstorage.mapper;

import com.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {


    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User findUser(String username);

    @Select("SELECT * FROM USERS WHERE userid = #{userId}")
    User getUserById(Integer userId);

    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) VALUES(#{username}, #{salt}, #{password}, #{firstname}, #{lastname})")
    @Options(useGeneratedKeys = true, keyProperty = "userid")
    int insert(User user);

    @Update("UPDATE USERS SET username = #{username}, salt = #{salt}, password = #{password}, firstname = #{firstname}, lastname = #{lastname} WHERE userid = #{userid}")
    void update(User user);

    @Delete("DELETE FROM USERS WHERE userid = #{userid}")
    void delete(Integer userid);

}
