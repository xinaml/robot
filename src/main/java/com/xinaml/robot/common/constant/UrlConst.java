package com.xinaml.robot.common.constant;

/**
 * @Author: [lgq]
 * @Date: [19-6-13 上午10:59]
 * @Description:
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
public class UrlConst {

    public static  final String ACCOUNT_INFO="/api/account/v3/wallet";
    /**
     * k线
     */
    public static  final  String K_LINE="/api/futures/v3/instruments" ;
    /**
     * 最后成交价
     */
    public static  final  String LAST="/api/futures/v3/instruments" ;

    /**
     * 提交单子
     */
    public static  final  String COMMIT_ORDER="/api/futures/v3/order" ;
    /**
     * 撤销单子
     */
    public static  final  String CANCEL_ORDER="/api/futures/v3/cancel_order" ;
    /**
     * 订单信息
     */
    public static  final  String ORDER_INFO="/api/futures/v3/orders" ;
    /**
     * 合约持仓信息
     */
    public static  final  String HOLD_INFO="/api/futures/v3/position" ;
}
