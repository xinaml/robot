/**
 * 基础业务
 *
 * @author lgq
 * @date 2018/4/15
 **/
package com.xinaml.robot.base.ser;

import com.xinaml.robot.base.dto.BaseDTO;
import com.xinaml.robot.base.entity.BaseEntity;
import com.xinaml.robot.base.rep.JapRep;
import com.xinaml.robot.base.rep.JpaSpec;
import com.xinaml.robot.common.custom.exception.RepException;
import com.xinaml.robot.common.custom.exception.SerException;
import com.xinaml.robot.common.utils.ClazzTypeUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Table;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ServiceImpl<BE extends BaseEntity, BD extends BaseDTO> implements Ser<BE, BD>, Serializable {
    @Autowired(required = false)
    protected JapRep<BE, BD> rep;
    @Autowired
    protected EntityManager entityManager;


    @Override
    public List<BE> findAll() throws SerException {
        try {
            return rep.findAll();
        } catch (Exception e) {
            throw repExceptionHandler(new RepException(e.getCause()));
        }

    }

    @Override
    public Map<String, Object> findByPage(BD dto) throws SerException {
        try {
            JpaSpec jpaSpec = new JpaSpec<BE, BD>(dto);
            PageRequest page = jpaSpec.getPageRequest(dto);
            Page<BE> rs = rep.findAll(jpaSpec, page);
            Map map = new HashMap<String, Object>(2);
            map.put("rows", rs.getContent());
            map.put("total", rs.getTotalElements());
            return map;
        } catch (Exception e) {
            throw repExceptionHandler(new RepException(e.getCause()));
        }

    }

    @Override
    public Long count(BD dto) throws SerException {
        try {
            JpaSpec jpaSpec = new JpaSpec<BE, BD>(dto);
            return rep.count(jpaSpec);
        } catch (Exception e) {
            throw repExceptionHandler(new RepException(e.getCause()));
        }

    }

    @Override
    public BE findOne(BD dto) throws SerException {
        try {
            JpaSpec jpaSpec = new JpaSpec<BE, BD>(dto);
            Optional<BE> op = rep.findOne(jpaSpec);
            if (op.isPresent()) {
                return op.get();
            }
            return null;
        } catch (Exception e) {
            throw repExceptionHandler(new RepException(e.getCause()));
        }
    }


    @Override
    public List<BE> findByRTS(BD dto) throws SerException {

        try {
            JpaSpec jpaSpec = new JpaSpec<BE, BD>(dto);
            if (dto.getSorts().size() > 0) {
                return rep.findAll(jpaSpec, jpaSpec.getSort(dto.getSorts()));
            } else {
                return rep.findAll(jpaSpec);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw repExceptionHandler(new RepException(e.getCause()));
        }
    }

    @Override
    public BE findById(String id) throws SerException {
        if (StringUtils.isNotBlank(id)) {
            try {
                return rep.findById(id).get();
            } catch (NoSuchElementException e) {
                throw new SerException("id为" + id + "的数据不存在！");
            }
        } else {
            throw new SerException("查询 id 不能为空！");
        }

    }

    @Override
    public void save(BE... entities) throws SerException {
        try {
            rep.saveAll(Arrays.asList(entities));
        } catch (Exception e) {
            throw repExceptionHandler(new RepException(e.getCause()));
        }
    }

    @Override
    public void remove(String... ids) throws SerException {

        if (null != ids && ids.length > 0) {
            try {
                for (String id : ids) {
                    rep.deleteById(id);
                }
            } catch (Exception e) {
                throw repExceptionHandler(new RepException(e.getCause()));
            }
        } else {
            throw new SerException("删除 id 不能为空！");
        }


    }

    @Override
    public void remove(BE... entities) throws SerException {
        try {
            rep.deleteAll(Arrays.asList(entities));
        } catch (Exception e) {
            throw repExceptionHandler(new RepException(e.getCause()));
        }
    }

    @Override
    public void update(List<BE> entities) throws SerException {
        rep.saveAll(entities);
    }

    @Override
    public void update(BE... entities) throws SerException {
        try {
            for (BE be : entities) {
                rep.save(be);
            }
        } catch (Exception e) {
            throw repExceptionHandler(new RepException(e.getCause()));
        }
    }

    @Override
    public Boolean exists(String id) throws SerException {
        try {
            return rep.existsById(id);
        } catch (Exception e) {
            throw repExceptionHandler(new RepException(e.getCause()));
        }
    }


    @Override
    public List<Object> findBySql(String sql) throws SerException {
        try {
            Query nativeQuery = entityManager.createNativeQuery(sql);
            return nativeQuery.getResultList();
        } catch (Exception e) {
            throw repExceptionHandler(new RepException(e.getCause()));
        }

    }

    @Override
    public <T> T findOneBySql(String sql, Class clazz, String... fields) throws SerException {
        List<T> objects = findBySql(sql, clazz, fields);
        if (objects.size() > 0) {
            return objects.get(0);
        } else {
            return  null;
        }
    }

    @Override
    public <T> List<T> findBySql(String sql, Class clazz, String... fields) throws SerException {
        List<Field> all_fields = new ArrayList<>(); //源类属性列表
        Class temp_clazz = clazz;
        while (null != temp_clazz) { //数据源类所有属性（包括父类）
            all_fields.addAll(Arrays.asList(temp_clazz.getDeclaredFields()));
            temp_clazz = temp_clazz.getSuperclass();
            if (Object.class.equals(temp_clazz) || null == temp_clazz) {
                break;
            }
        }
        Query nativeQuery = entityManager.createNativeQuery(sql);
        List<Object> resultList = nativeQuery.getResultList();
        List<T> list = new ArrayList<>(resultList.size());

        //解析查询结果
        try {
            for (int i = 0; i < resultList.size(); i++) {
                Object[] arr_obj;
                if (fields.length > 1) {
                    arr_obj = (Object[]) resultList.get(i);
                } else {
                    arr_obj = new Object[]{resultList.get(i)};
                }
                Object obj = clazz.newInstance();
                for (int j = 0; j < fields.length; j++) {
                    for (Field field : all_fields) {
                        if (field.getName().equals(fields[j])) {
                            field.setAccessible(true);
                            if (!field.getType().isEnum()) { //忽略枚举类型
                                field.set(obj, ClazzTypeUtil.convertDataType(field.getType().getSimpleName(), arr_obj[j]));
                            } else {
                                Field[] enumFields = field.getType().getFields();
                                for (int k = 0; k < enumFields.length; k++) {
                                    Integer val = null;
                                    if (null != arr_obj[j]) {
                                        val = Integer.parseInt(arr_obj[j].toString());
                                    }
                                    if (null != val && val == k) {
                                        String name = enumFields[k].getName();
                                        field.set(obj, field.getType().getField(name).get(name));
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
                list.add((T) obj);
            }
        } catch (Exception e) {
            throw repExceptionHandler(new RepException(e.getCause()));
        }

        return list;
    }

    @Override
    public void executeSql(String sql) throws SerException {
        try {
            Query query = entityManager.createNativeQuery(sql);
            query.executeUpdate();
        } catch (Exception e) {
            throw repExceptionHandler(new RepException(e.getCause()));
        }

    }

    @Override
    public String getTableName(Class clazz) throws SerException {
        try {
            if (clazz.isAnnotationPresent(Table.class)) {
                Annotation annotation = clazz.getAnnotation(Table.class);
                Method[] methods = annotation.annotationType().getMethods();
                for (Method method : methods) {
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }
                    if ("name".equals(method.getName())) {
                        Object invoke = method.invoke(annotation);
                        return invoke.toString();
                    }
                }
            }
        } catch (Exception e) {
            throw new SerException(e.getMessage());
        }
        String msg = "解析表名错误！";
        throw new SerException(msg);
    }


    protected SerException repExceptionHandler(RepException e) {
        e.printStackTrace();
        String msg = null;
        Throwable throwable = e.getThrowable();
        if (throwable instanceof ConstraintViolationException) { //唯一约束异常
            ConstraintViolationException ex = ((ConstraintViolationException) throwable);
            msg = ex.getCause().getMessage();
            msg = StringUtils.substringAfter(msg, "Duplicate entry '");
            if (StringUtils.isNotBlank(msg)) {
                msg = "[" + StringUtils.substringBefore(msg, "' for key") + "]已存在！";
            } else {
                msg = ex.getCause().getMessage();
            }
            if (msg.indexOf("cannot be null") != -1) {
                msg = StringUtils.substringAfter(msg, "'");
                msg = StringUtils.substringBefore(msg, "'").toLowerCase() + "不能为空！";
            }
        } else if (throwable instanceof DataException) {
            DataException ex = ((DataException) throwable);
            msg = ex.getCause().getMessage();
            msg = StringUtils.substringAfter(msg, "Data too long for column '");
            if (StringUtils.isNotBlank(msg)) {
                msg = "[" + StringUtils.substringBefore(msg, "' at row") + "]数据超出长度！";
            } else {
                msg = ex.getCause().getMessage();
            }
        } else if (throwable instanceof QueryTimeoutException) {
            msg = "查询超时！";
        }
        if (StringUtils.isBlank(msg)) {
            msg = e.getMessage();
        }
        return new SerException(msg);

    }
}
