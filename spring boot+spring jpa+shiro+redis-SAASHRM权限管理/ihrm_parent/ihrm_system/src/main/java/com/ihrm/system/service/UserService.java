package com.ihrm.system.service;

import com.ihrm.common.utils.IdWorker;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.User;
import com.ihrm.system.dao.RoleDao;
import com.ihrm.system.dao.UserDao;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;


@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RoleDao roleDao;


    public void save(User user)
    {
        String id = idWorker.nextId()+"";
        user.setId(id);
        String password = new Md5Hash("123456",user.getMobile(),3).toString();  //1.密码，盐，加密次数
        user.setLevel("user");
        user.setPassword(password);
        user.setEnableState(1);
        userDao.save(user);
    }

    public void update(User user)
    {
        User tar=userDao.findById(user.getId()).get();
        tar.setUsername(user.getUsername());
        tar.setPassword(user.getPassword());
        tar.setDepartmentId(user.getDepartmentId());
        tar.setDepartmentName(user.getDepartmentName());
        System.out.println(user.getEnableState());
        System.out.println();
        System.out.println();
        tar.setEnableState(user.getEnableState());
        userDao.save(tar);


    }

    public User findById(String id)
    {
        return userDao.findById(id).get();
    }

    public Page findAll(Map<String,Object> map,int page, int size) {
        //1.需要查询条件
        Specification<User> spec = new Specification<User>() {
            /**
             * 动态拼接查询条件
             */
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                //根据请求的companyId是否为空构造查询条件
                if(!StringUtils.isEmpty(map.get("companyId"))) {
                    list.add(criteriaBuilder.equal(root.get("companyId").as(String.class),(String)map.get("companyId")));
                }
                //根据请求的部门id构造查询条件
                if(!StringUtils.isEmpty(map.get("departmentId"))) {
                    list.add(criteriaBuilder.equal(root.get("departmentId").as(String.class),(String)map.get("departmentId")));
                }
                if(!StringUtils.isEmpty(map.get("hasDept"))) {
                    //根据请求的hasDept判断  是否分配部门 0未分配（departmentId = null），1 已分配 （departmentId ！= null）
                    if("0".equals((String) map.get("hasDept"))) {
                        list.add(criteriaBuilder.isNull(root.get("departmentId")));
                    }else {
                        list.add(criteriaBuilder.isNotNull(root.get("departmentId")));
                    }
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };

        Page<User> pageUser = userDao.findAll(spec, new PageRequest(page-1, size));
        return pageUser;
    }

    public void deleteById(String id)
    {
        userDao.deleteById(id);
    }

    public void assignRole(String id, List<String> list) {
        User user = userDao.findById(id).get();
        Set<Role> set=new HashSet<>();

        for(String role: list)
        {
            Role role1 = roleDao.findById(role).get();
            set.add(role1);
        }
        user.setRoles(set);
        userDao.save(user);
    }


    public User findByMobile(String mobile)
    {
        return userDao.findByMobile(mobile);
    }
}
