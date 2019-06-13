/**
 * 条件类型
 *
 * @author lgq
 * @date 2018/4/15
 **/
package com.xinaml.robot.base.dto;

import java.io.Serializable;

public enum RestrictType implements Serializable {
    /**
     * 相等
     */
    EQ,

    /**
     * 在什么之间
     */
    BETWEEN,
    /**
     * 模糊
     */
    LIKE,
    /**
     * 在什么范围之间
     */
    IN,

    /**
     * 大于
     */
    GT,
    /**
     * 小于
     */
    LT,
    /**
     * 或者
     */
    OR,
    /**
     * 不等于
     */
    NE,
    /**
     * 大于等于
     */
    GTEQ,
    /**
     * 小于等于
     */
    LTEQ,
    /**
     * 为空
     */
    ISNULL,
    /**
     * 不为空
     */
    ISNOTNULL,
    /**
     * 不在xx范围
     */
    NOTIN;


    public static RestrictType valueOf(Object val) {
        String value = String.valueOf(val);
        RestrictType type = null;
        switch (value) {
            case "EQ":
                type = RestrictType.EQ;
                break;
            case "BETWEEN":
                type = RestrictType.BETWEEN;
                break;
            case "LIKE":
                type = RestrictType.LIKE;
                break;
            case "IN":
                type = RestrictType.IN;
                break;
            case "GT":
                type = RestrictType.GT;
                break;
            case "LT":
                type = RestrictType.LT;
                break;
            case "GTEQ":
                type = RestrictType.GTEQ;
                break;
            case "LTEQ":
                type = RestrictType.LTEQ;
                break;
            case "OR":
                type = RestrictType.OR;
                break;
            case "NE":
                type = RestrictType.NE;
                break;
            case "ISNULL":
                type = RestrictType.ISNULL;
                break;
            case "ISNOTNULL":
                type = RestrictType.ISNOTNULL;
                break;
            case "NOTIN":
                type = RestrictType.NOTIN;
                break;
            default:
                type = RestrictType.EQ;
                break;
        }
        return type;
    }

    public static String getRestrict(RestrictType type) {
        String typeStr;
        switch (type) {
            case EQ:
                typeStr = "equal";
                break;
            case BETWEEN:
                typeStr = "between";
                break;
            case LIKE:
                typeStr = "like";
                break;
            case IN:
                typeStr = "in";
                break;
            case GT:
                typeStr = "greaterThan";
                break;
            case LT:
                typeStr = "lessThan";
                break;
            case GTEQ:
                typeStr = "greaterThanOrEqualTo";
                break;
            case LTEQ:
                typeStr = "lessThanOrEqualTo";
                break;
            case OR:
                typeStr = "or";
                break;
            case NE:
                typeStr = "notEqual";
                break;
            case ISNULL:
                typeStr = "isNull";
                break;
            case ISNOTNULL:
                typeStr = "isNotNull";
                break;
            default:
                typeStr = "equal";
                break;
        }
        return typeStr;
    }
}
