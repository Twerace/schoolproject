package com.lu.schoolproject.service.Imp;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
        if (user.getPhone() == null || user.getPassword() == null) {
            return SaResult.error("手机号和密码不能为空");
        }
        
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, user.getPhone());
        User dbUser = userMapper.selectOne(wrapper);
        
        if (dbUser == null) {
            return SaResult.error("用户不存在");
        }
        
        // 验证密码
        String encryptedPassword = SaSecureUtil.aesEncrypt(saTokenProperties.getVerifyKey(), user.getPassword());
        if (!encryptedPassword.equals(dbUser.getPassword())) {
            return SaResult.error("密码错误");
        }
        
        // 登录成功，生成token
        StpUtil.login(dbUser.getId());
        return SaResult.ok("登录成功").setData(StpUtil.getTokenValue());
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
        if(user.getPassword()!=null && !user.getPassword().trim().isEmpty()){
            String s = SaSecureUtil.aesEncrypt(saTokenProperties.getVerifyKey(),user.getPassword());
            user.setPassword(s);
        }
        return userMapper.updateById(user)>0?SaResult.ok("修改成功"):SaResult.error("修改失败");
    }
    
    @Override
    public SaResult savaUser(User user) {
        if (user.getPhone() == null || user.getPassword() == null) {
            return SaResult.error("手机号和密码不能为空");
        }
        
        // 检查手机号是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, user.getPhone());
        User existUser = userMapper.selectOne(wrapper);
        if (existUser != null) {
            return SaResult.error("手机号已存在");
        }
        
        // 加密密码
        String encryptedPassword = SaSecureUtil.aesEncrypt(saTokenProperties.getVerifyKey(), user.getPassword());
        user.setPassword(encryptedPassword);
        
        return userMapper.insert(user) > 0 ? SaResult.ok("保存成功") : SaResult.error("保存失败");
    }
}
