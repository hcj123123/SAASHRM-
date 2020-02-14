package com.ihrm.system.service;

import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;
import com.ihrm.common.utils.BeanMapUtils;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.common.utils.PermissionConstants;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.PermissionApi;
import com.ihrm.domain.system.PermissionMenu;
import com.ihrm.domain.system.PermissionPoint;
import com.ihrm.system.dao.*;
import com.ihrm.system.dao.PermissionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private PermissionMenuDao permissionMenuDao;

    @Autowired
    private PermissionApiDao permissionApiDao;

    @Autowired
    private PermissionPointDao permissionPointDao;

    @Autowired
    private IdWorker idWorker;


    public void save(Map<String,Object> map) throws Exception {
        String id = idWorker.nextId()+"";
       Permission perm= BeanMapUtils.mapToBean(map,Permission.class);
       perm.setId(id);
       int type=perm.getType();
       switch (type)
       {
           case PermissionConstants .PY_MENU:
               PermissionMenu permissionMenu = BeanMapUtils.mapToBean(map, PermissionMenu.class);
               permissionMenu.setId(id);
              permissionMenuDao.save(permissionMenu);
               break;
           case PermissionConstants.PY_POINT:
               PermissionPoint permissionPoint= BeanMapUtils.mapToBean(map, PermissionPoint.class);
               permissionPoint.setId(id);
             permissionPointDao.save(permissionPoint);
               break;
           case PermissionConstants.PY_API:
               PermissionApi permissionApi = BeanMapUtils.mapToBean(map, PermissionApi.class);
               permissionApi.setId(id);
               permissionApiDao.save(permissionApi);
               break;
               default:
                   throw  new CommonException(ResultCode.FAIL);
       }
        permissionDao.save(perm);
    }

    public void     update(Map<String,Object> map) throws Exception {
        Permission perm= BeanMapUtils.mapToBean(map,Permission.class);
        Permission permission=permissionDao.findById(perm.getId()).get();
        permission.setName(perm.getName());
        permission.setCode(perm.getCode());
        permission.setDescription(perm.getDescription());
        permission.setEnVisible(perm.getEnVisible());
        int type=perm.getType();
        switch (type)
        {
            case PermissionConstants .PY_MENU:
                PermissionMenu permissionMenu = BeanMapUtils.mapToBean(map, PermissionMenu.class);
                permissionMenu.setId(perm.getId());
                permissionMenuDao.save(permissionMenu);
                break;
            case PermissionConstants.PY_POINT:
                PermissionPoint permissionPoint= BeanMapUtils.mapToBean(map, PermissionPoint.class);
                permissionPoint.setId(perm.getId());
                permissionPointDao.save(permissionPoint);
                break;
            case PermissionConstants.PY_API:
                PermissionApi permissionApi = BeanMapUtils.mapToBean(map, PermissionApi.class);
                permissionApi.setId(perm.getId());
                permissionApiDao.save(permissionApi);
                break;
            default:
                throw  new CommonException(ResultCode.FAIL);
        }
        permissionDao.save(permission);
    }

    public Map<String,Object> findById(String id) throws Exception {
        Permission permission = permissionDao.findById(id).get();
        int type=permission.getType();
        Object object=null;
        if(type==PermissionConstants.PY_MENU)
            object=permissionMenuDao.findById(id).get();
        else if(type==PermissionConstants.PY_POINT)
            object=permissionPointDao.findById(id).get();
        else if(type==PermissionConstants.PY_API)
            object=permissionApiDao.findById(id).get();
        else throw  new CommonException(ResultCode.FAIL);
        Map<String, Object> map= BeanMapUtils.beanToMap(object);

        map.put("name",permission.getName());
        map.put("type",permission.getType());
        map.put("code",permission.getCode());
        map.put("description",permission.getDescription());
        map.put("pid",permission.getPid());
        map.put("enVisible",permission.getEnVisible());
        return map;
    }

    public List<Permission> findAll(Map<String,Object> map) {
        //1.需要查询条件
        Specification<Permission> spec = new Specification<Permission>() {
            /**
             * 动态拼接查询条件
             */
            public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                //根据请求的companyId是否为空构造查询条件
                if(!StringUtils.isEmpty(map.get("pid"))) {
                    list.add(criteriaBuilder.equal(root.get("pid").as(String.class),(String)map.get("pid")));
                }
                //根据请求的部门id构造查询条件
                if(!StringUtils.isEmpty(map.get("enVisible"))) {
                    list.add(criteriaBuilder.equal(root.get("enVisible").as(String.class),(String)map.get("enVisible")));
                }
                if(!StringUtils.isEmpty(map.get("type"))) {
                    String ty=(String)map.get("type");
                    CriteriaBuilder.In<Object> type = criteriaBuilder.in(root.get("type"));
                    if("0".equals(ty)) {
                     type.value(1).value(2);
                    }else {
                       type.value(Integer.parseInt(ty));
                    }
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };


        return permissionDao.findAll(spec);
    }

    public void deleteById(String id) throws Exception {
        Permission perm=permissionDao.findById(id).get();
        permissionDao.delete(perm);
        int type=perm.getType();
        switch (type)
        {
            case PermissionConstants .PY_MENU:
                permissionMenuDao.deleteById(id);
                break;
            case PermissionConstants.PY_POINT:
                permissionPointDao.deleteById(id);
                break;
            case PermissionConstants.PY_API:
                permissionApiDao.deleteById(id);
                break;
            default:
                throw  new CommonException(ResultCode.FAIL);
        }
    }
}
