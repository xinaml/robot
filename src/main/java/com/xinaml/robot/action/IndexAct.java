/**
 * @author lgq
 * @date 2018/4/15
 **/
package com.xinaml.robot.action;

import com.xinaml.robot.common.custom.exception.SerException;
import com.xinaml.robot.common.webscoket.SocketServer;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 首页
 *
 * @author lgq
 */
@RestController
public class IndexAct {


    @GetMapping({"","index"})
    public ModelAndView index(String data) throws SerException {
        print(data);
        return new ModelAndView("index");
    }
    @GetMapping("start")
    public String start(String fileId) throws SerException {
        int count=100;
        int index=0;
        while (count>=index){
            try {
                Thread.sleep(50);
                SocketServer.sendProgress(fileId,index,count);
                index++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return "start";
    }

    private void print(String data) {
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        System.out.println("utf-8 测试,传递参数为：" + (data != null ? data : ""));
        StackTraceElement[] element = Thread.currentThread().getStackTrace();
        System.out.println("调用本方法 "+element[1].getMethodName()+" 的方法为： "+element[2].getMethodName());
    }
}
