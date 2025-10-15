package com.lu.schoolproject.service.Imp;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lu.schoolproject.entitys.LoginUser;
import com.lu.schoolproject.entitys.User;
import com.lu.schoolproject.entitys.properties.SaTokenProperties;
import com.lu.schoolproject.mapper.UserMapper;
import com.lu.schoolproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImp extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private final SaTokenProperties saTokenProperties;
    /**
     * 登录接口
     * @param user
     * @return
     */

    @Override
    public SaResult login(LoginUser user){

        return null;
    }
    @Override
    public SaResult selectUser(Long id){
        User user = userMapper.selectById(id);

        if(user==null)return SaResult.error("查询不出此用户");

        user.setPassword(null);
        return SaResult.data(user);
    }

    @Override
    public SaResult updataUser(User user){
        User user1 = userMapper.selectById(user.getId());
        if(user1==null)return SaResult.error("查无此用户");
        if(user.getPassword()!= null){
            String s = SaSecureUtil.aesEncrypt(saTokenProperties.getVerifykey(),user.getPassword());
            user.setPassword(s);
        }
        return userMapper.updateById(user)>0?SaResult.ok("修改成功"):SaResult.error("修改失败");
    }
}
