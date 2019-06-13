/**
 * jpa 通用持久化接口
 *
 * @author lgq
 * @date 2018/4/15
 **/
package com.xinaml.robot.base.rep;

import com.xinaml.robot.base.dto.BaseDTO;
import com.xinaml.robot.base.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * JPA查询接口
 *
 * @param <BE>
 * @param <BD>
 */
public interface JapRep<BE extends BaseEntity, BD extends BaseDTO> extends JpaRepository<BE, String>
        , JpaSpecificationExecutor<BE> {
}
