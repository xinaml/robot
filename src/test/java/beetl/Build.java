package beetl;

import beetl.utils.ParseBean;

public class Build {


    public static void main(String[] args)  {
        System.out.println("create class ...");
        //action,dto,entity,rep,ser,serImp,to
        String[] excludes = new String[]{}; //过滤创建的文件
        ParseBean.excludes(excludes);
        ParseBean.build();
    }


}
