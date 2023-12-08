package com.example.blogsystem.controller;

import com.example.blogsystem.common.ResultAjax;
import com.example.blogsystem.common.SessionUtils;
import com.example.blogsystem.model.Articleinfo;
import com.example.blogsystem.model.Userinfo;
import com.example.blogsystem.model.vo.UserinfoVO;
import com.example.blogsystem.service.ArticleService;
import com.example.blogsystem.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @Qualifier("taskExecutor")
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private UserService userService;
    private static final int DESC_LEN = 120; // 文章简介长度
    /**
     * 得到文章列表
     * @return
     */
    @RequestMapping("/mylist")
    public ResultAjax myList(HttpServletRequest req) throws ExecutionException, InterruptedException {
        // 得到当前登录的用户
        Userinfo userinfo = SessionUtils.getUser(req);
        if (userinfo == null) {
            return ResultAjax.fail(-1,"请先登录");
        }
        // 否则请先登录

        int id = userinfo.getId();
        FutureTask<Userinfo> userTask =new FutureTask<>(()->{
            return userService.getUserById(id);
        });
        FutureTask<Integer> countTask = new FutureTask<>(()->{
            return articleService.artCount(id);
        });

        FutureTask<List> listTask = new FutureTask<>(()->{
            return articleService.getListByUid(userinfo.getId());
        });
        taskExecutor.submit(listTask);
        taskExecutor.submit(userTask);
        taskExecutor.submit(countTask);
        Userinfo userGet = userTask.get();
        int count = countTask.get();
        HashMap<String, Object> map = new HashMap<>();
        map.put("user",userGet);
        map.put("count",count);
        // 返回给前端
        List<Articleinfo> list = listTask.get();
        // 处理list的desc -> 将文章正文变成简介
        if (list != null && !list.isEmpty()) {
            list.stream().parallel().forEach((art)->{
                if (art.getContent().length() > 120) {
                    art.setContent(art.getContent().substring(0,DESC_LEN));
                }
            });
        }
        map.put("list",list);
        return ResultAjax.success(map);
    }

    /**
     * 删除文章
     * @param aid
     * @param req
     * @return
     */
    @RequestMapping("/del")
    public ResultAjax del(@Param("aid") Integer aid, HttpServletRequest req) {
        // 参数校验
        if (aid <= 0 || aid == null) {
            return ResultAjax.fail(-1,"参数错误");
        }
        // 获取用户权限
        Userinfo userinfo = SessionUtils.getUser(req);
        if (userinfo == null) {
            return ResultAjax.fail(-1,"请先登录");
        }

        // 判断文章的归属人
        // 删除操作.
        int result = articleService.del(aid, userinfo.getId());

        // 响应给前端.
        return ResultAjax.success(result);

    }

    /**
     * 添加文章
     * @param articleinfo
     * @return
     */
    @RequestMapping("/add")
    public ResultAjax add(Articleinfo articleinfo, HttpServletRequest req) {
        // 校验参数
        if (articleinfo == null || !StringUtils.hasLength(articleinfo.getTitle())
                || !StringUtils.hasLength(articleinfo.getContent())) {
                return ResultAjax.fail(-1,"非法参数");
        }

        // 获取用户id
        Userinfo userinfo = SessionUtils.getUser(req);
        if (userinfo == null) {
            return ResultAjax.fail(-2,"请先登录");
        }

        articleinfo.setUid(userinfo.getId());
        System.out.println(articleinfo);
        int ret = articleService.add(articleinfo);
        System.out.println(ret);
        return ResultAjax.success(ret);
    }


    /**
     * 获取文章id并验证user
     * @param aid
     * @param req
     * @return
     */
    @RequestMapping("/getArticle")
    public ResultAjax getArticleByid(Integer aid, HttpServletRequest req) {
        if (aid == null || aid <= 0) {
            return ResultAjax.fail(-1,"参数错误");
        }
        Userinfo user = SessionUtils.getUser(req);
        if (user == null) {
            return ResultAjax.fail(-2,"请先登录!");
        }
        Articleinfo articleinfo = articleService.getArticleByid(aid,user.getId());
        if (articleinfo == null || !StringUtils.hasLength(articleinfo.getTitle())
                || !StringUtils.hasLength(articleinfo.getContent())) {
            System.out.println("你无法通过此方法");
            return ResultAjax.fail(-1,"非法参数");
        }
        return ResultAjax.success(articleinfo);
    }

    /**
     * edit blog
     * @param articleinfo
     * @param req
     * @return
     */
    @RequestMapping("/update")
    public ResultAjax update(Articleinfo articleinfo, HttpServletRequest req) {
        // 校验参数
        System.out.println("进入update");
        System.out.println("传入的原始article:" + articleinfo);
        if (articleinfo == null
                || !StringUtils.hasLength(articleinfo.getTitle())
                || !StringUtils.hasLength(articleinfo.getContent())
                || articleinfo.getId() == 0) {
            return  ResultAjax.fail(-1,"非法参数");
        }
        // 校验用户
        Userinfo userinfo = SessionUtils.getUser(req);
        System.out.println("当前user:" + userinfo);
        if (userinfo == null) {
            return ResultAjax.fail(-2,"请登录!!");
        }

        // 设置归属人
        articleinfo.setUid(userinfo.getId());
        System.out.println( "开始打印article" + articleinfo);
        int ret = articleService.update(articleinfo);

        return ResultAjax.success(ret);
    }

    /**
     * 文章详情
     * @param id
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @RequestMapping("/detail")
    public ResultAjax detail(@Param("id") Integer id) throws ExecutionException, InterruptedException {
        // 参数校验
        System.out.println(id);
        if (id == null || id <= 0) {
            return ResultAjax.fail(-1,"非法参数1");
        }
        // 查询文章详情
        Articleinfo articleinfo = articleService.getArticleById(id);
        if (articleinfo == null || articleinfo.getId() <= 0) {
            return ResultAjax.fail(-1,"非法参数2");
        }
        FutureTask<UserinfoVO> taskUser = new FutureTask<>(()-> userService.getUserById(articleinfo.getUid()));
        taskExecutor.submit(taskUser);
        FutureTask<Integer> taskCcount = new FutureTask<>(()-> articleService.artCount(articleinfo.getUid()));
        taskExecutor.submit(taskCcount);

        // 等待任务, 线程池执行完成
        UserinfoVO userinfoVO = taskUser.get();
        int count = taskCcount.get();

        // 组装数据
        userinfoVO.setArtCount(count);
        HashMap<String, Object> ret = new HashMap<>();
        ret.put("user",userinfoVO);
        ret.put("art",articleinfo);
        return ResultAjax.success(ret);
    }

    /**
     * 文章访问量自增
     * @param id
     * @return
     */
    @RequestMapping("/increment")
    public ResultAjax increment(Integer id) {
        if (id == null || id <= 0) {
            return  ResultAjax.fail(-1,"参数非法");
        }
        int ret = articleService.increment(id);
        if (ret == 0) {
            return ResultAjax.fail(-1,"参数有误");
        }
        return ResultAjax.success(ret);
    }

    @RequestMapping("/getpage")
    public ResultAjax getListByPage(Integer psize, Integer pindex) throws ExecutionException, InterruptedException {
        // 加工参数
        if (pindex == null || pindex < 1) {
            pindex = 1;
        }
        if (psize == null || psize < 1) {
            psize = 2;
        }

        // 并发执行查询
        int finalPsize = psize;
        Integer finalPindex = pindex;
        FutureTask<List<Articleinfo>> listTask = new FutureTask<>(()->{
            // 查询分页的每一页的集合
            int off = finalPsize * (finalPindex - 1);
            return articleService.getListByPage(finalPsize, off);
        });

        FutureTask<Integer> countTask = new FutureTask<>(() -> {
            // 文章总数
            int total = articleService.getCount();
            // 计算总页数
            int countPage = total / finalPsize; 
            int mod = total % finalPsize;
            if (mod >= 1) {
                countPage = countPage + 1;
            }
            return countPage;
        });

        taskExecutor.submit(listTask);
        taskExecutor.submit(countTask);

        // 获取数据
        List<Articleinfo> list = listTask.get();
        int count = countTask.get();
        HashMap<String, Object> map = new HashMap<>();
        map.put("list",list);
        map.put("count",count);

        return ResultAjax.success(map);
    }
}
