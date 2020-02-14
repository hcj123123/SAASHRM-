package com.ihrm.company.service;


import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.company.dao.DepartmentDao;
import com.ihrm.domain.company.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService extends BaseService{

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private  IdWorker idWorker;


    public void save(Department department)
    {
        String id = idWorker.nextId()+"";
        department.setId(id);
        departmentDao.save(department);
    }

    public void update(Department department)
    {
        Department department1 = departmentDao.findById(department.getId()).get();
        department1.setCode(department.getCode());
        department1.setIntroduce(department.getIntroduce());
        department1.setName(department.getName());
        department1.setManager(department.getManager());

        departmentDao.save(department1);
    }

    public Department findById(String id)
    {
        return departmentDao.findById(id).get();
    }

    public List<Department> findAll(String companyId)
    {

//        Specification<Department> specification=new Specification<Department>() {
//            @Override
//            public Predicate toPredicate(Root<Department> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                return criteriaBuilder.equal(root.get("companyId").as(String.class),companyid);
//            }
//        };
        return departmentDao.findAll(getspc(companyId));
    }


    public void deleteById(String id)
    {
        departmentDao.deleteById(id);
    }
}
