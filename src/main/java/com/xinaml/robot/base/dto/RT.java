/**
 * 条件构建
 *
 * @author lgq
 * @date 2018/4/15
 **/
package com.xinaml.robot.base.dto;


import javax.persistence.criteria.JoinType;
import java.io.Serializable;

public class RT implements Serializable {
    /**
     * field（字段） 包含 "." 则默认会设置成左连接，左连接set 集合 命名必须未Set结束
     * 如：Set<User>userSet List<User>userList
     */

    private RT() {

    }

    public static Restrict eq(String filed, Object value) {
        return new Restrict(filed, value, RestrictType.EQ);
    }

    public static Restrict eq(String filed, Object value, JoinType join) {
        return new Restrict(filed, value, RestrictType.EQ, join);
    }

    public static Restrict between(String filed, Object value) {
        return new Restrict(filed, value, RestrictType.BETWEEN);
    }

    public static Restrict between(String filed, Object value, JoinType join) {
        return new Restrict(filed, value, RestrictType.BETWEEN, join);
    }

    public static Restrict like(String filed, Object value) {
        return new Restrict(filed, value, RestrictType.LIKE);
    }

    public static Restrict like(String filed, Object value, JoinType join) {
        return new Restrict(filed, value, RestrictType.LIKE, join);
    }

    public static Restrict in(String filed, Object value) {
        return new Restrict(filed, value, RestrictType.IN);
    }

    public static Restrict in(String filed, Object value, JoinType join) {
        return new Restrict(filed, value, RestrictType.IN, join);
    }

    public static Restrict gt(String filed, Object value) {
        return new Restrict(filed, value, RestrictType.GT);
    }

    public static Restrict gt(String filed, Object value, JoinType join) {
        return new Restrict(filed, value, RestrictType.GT, join);
    }

    public static Restrict lt(String filed, Object value) {
        return new Restrict(filed, value, RestrictType.LT);
    }

    public static Restrict lt(String filed, Object value, JoinType join) {
        return new Restrict(filed, value, RestrictType.LT, join);
    }

    public static Restrict gtEq(String filed, Object value) {
        return new Restrict(filed, value, RestrictType.GTEQ);
    }

    public static Restrict gtEq(String filed, Object value, JoinType join) {
        return new Restrict(filed, value, RestrictType.GTEQ, join);
    }

    public static Restrict ltEq(String filed, Object value) {
        return new Restrict(filed, value, RestrictType.LTEQ);
    }

    public static Restrict ltEq(String filed, Object value, JoinType join) {
        return new Restrict(filed, value, RestrictType.LTEQ, join);
    }

    public static Restrict or(String filed, Object value) {
        return new Restrict(filed, value, RestrictType.OR);
    }

    public static Restrict or(String filed, Object value, JoinType join) {
        return new Restrict(filed, value, RestrictType.OR, join);
    }

    public static Restrict ne(String filed, Object value) {
        return new Restrict(filed, value, RestrictType.NE);
    }

    public static Restrict ne(String filed, Object value, JoinType join) {
        return new Restrict(filed, value, RestrictType.NE, join);
    }

    public static Restrict isNull(String filed) {
        return new Restrict(filed, null, RestrictType.ISNULL);
    }

    public static Restrict isNull(String filed, JoinType join) {
        return new Restrict(filed, null, RestrictType.ISNULL, join);
    }

    public static Restrict isNotNull(String filed) {
        return new Restrict(filed, null, RestrictType.ISNOTNULL);
    }

    public static Restrict isNotNull(String filed, JoinType join) {
        return new Restrict(filed, null, RestrictType.ISNOTNULL, join);
    }

    public static Restrict notIn(String field, Object[] values) {
        return new Restrict(field, values, RestrictType.NOTIN);
    }

    public static Restrict notIn(String field, Object[] values, JoinType join) {
        return new Restrict(field, values, RestrictType.NOTIN, join);
    }

}
