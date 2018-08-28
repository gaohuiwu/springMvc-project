package com.gaohw.Controller;

import com.gaohw.entity.Person;
import com.gaohw.entity.User;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.lang.annotation.Documented;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * FileName: MvcController
 * Author:   gaohuiwu
 * Date:     8/27/18 5:55 PM
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Controller
@RequestMapping("/mvc")
public class MvcController {

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    /**
     * 自动匹配参数
     *
     * @param name
     * @param age
     * @return
     */
    @RequestMapping("/person")
    public String toPerson(String name, double age) {
        System.out.println(name + " " + age);
        return "hello";
    }

    /**
     * 自动装箱
     * 1.有对应的person类
     *
     * @param p
     * @return
     */
    @RequestMapping("/person1")
    public String toPerson(Person p) {
        System.out.println(p.getName() + " " + p.getAge());
        return "hello";
    }


    /**
     * 使用InitBinder来处理Date类型的参数
     *
     * @param date
     * @return
     */
    @RequestMapping("/date")
    public String date(Date date) {
        System.out.println(date);
        return "hello";
    }

    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    /**
     * 向前台传递参数
     * 前台可在Request域中取到“p”
     *
     * @param map
     * @return
     */
    @RequestMapping("/show")
    public String showPerson(Map<String, Object> map) {
        Person p = new Person();
        map.put("p", p);
        p.setAge(20);
        p.setName("jay");
        return "show";
    }

    /**
     * 在controller中使用redirect方式处理请求
     *
     * @return
     */
    @RequestMapping("/redirect")
    public String redirect() {
        return "redirect:hello";
    }


    /**
     * 文件上传
     * 1加入commons-fileupload和commons-io依赖
     * 2springmvc配置CommonsMultipartResolver的bean
     * 3该方法
     * 4前端form表单
     * <form action="mvc/upload" method="post" enctype="multipart/form-data">
     * <input type="file" name="file"><br>
     * <input type="submit" value="submit">
     * </form>
     *
     * @param req
     * @return
     * @throws Exception
     */
    public String upload(HttpServletRequest req) throws Exception {
        MultipartHttpServletRequest mreq = (MultipartHttpServletRequest) req;
        MultipartFile file = mreq.getFile("file");
        String filename = file.getOriginalFilename();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        FileOutputStream fos = new FileOutputStream(
                req.getSession().getServletContext().getRealPath("/")
                        + "upload/" + sdf.format(new Date()) + filename.substring(filename.lastIndexOf(".")));
        fos.write(file.getBytes());
        fos.flush();
        fos.close();

        return "hello";
    }

    /**
     * 使用@RequsetParam注解指定参数的name
     *
     * @param id
     * @param name
     * @return
     */
    @RequestMapping("/param")
    public String testRequestParam(@RequestParam(value = "id") Integer id,
                                   @RequestParam(value = "name") String name) {
        System.out.println(id + "" + name);
        return "hello";
    }

    /**
     * RESTFul风格的StringMVC
     * 1web.xml配置过滤器 put和delete请求
     * 2前台代码
     * <form action="rest/user/1" method="post">
     * <input type="hidden" name="_method" value="PUT">
     * <input type="submit" value="put">
     * </form>
     *
     * <form action="rest/user/1" method="post">
     * <input type="submit" value="post">
     * </form>
     *
     * <form action="rest/user/1" method="get">
     * <input type="submit" value="get">
     * </form>
     *
     * <form action="rest/user/1" method="post">
     * <input type="hidden" name="_method" value="DELETE">
     * <input type="submit" value="delete">
     * </form>
     */
    @Controller
    @RequestMapping("/rest")
    class RestController {

        @RequestMapping(value = "user/{id}", method = RequestMethod.GET)
        public String get(@PathVariable("id") Integer id) {
            System.out.println("get" + id);
            return "/hello";
        }

        @RequestMapping(value = "/user/{id}", method = RequestMethod.POST)
        public String post(@PathVariable("id") Integer id) {
            System.out.println("post" + id);
            return "/hello";
        }

        @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
        public String put(@PathVariable("id") Integer id) {
            System.out.println("put" + id);
            return "/hello";
        }

        @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
        public String delete(@PathVariable("id") Integer id) {
            System.out.println("delete" + id);
            return "/hello";
        }
    }

    /**
     * 返回json格式的字符串
     * 1要先加入jackson的依赖
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/user")
    public User get() {
        User u = new User();
        u.setId(1);
        u.setName("jay");
        u.setBirth(new Date());
        return u;
    }


    /**
     * 处理局部异常(Controller内)
     *
     * @param ex
     * @return
     */
    @ExceptionHandler
    public ModelAndView exceptionHandler(Exception ex) {
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("exception", ex);
        System.out.println("in testExceptionHandler");
        return mv;
    }

    @RequestMapping("/error")
    public String error() {
        int i = 5 / 0;
        return "hello";
    }

    /**
     * 处理全局异常(所有Controller)
     */
    @ControllerAdvice
    class testControllerAdvice {
        @ExceptionHandler
        public ModelAndView exceptionHandler(Exception ex) {
            ModelAndView mv = new ModelAndView("error");
            mv.addObject("exception", ex);
            System.out.println("in testControllerAdvice");
            return mv;
        }
    }

    /**
     * 另一种处理全局异常的方法
     * 在springmvc配置文件中配置
     */

    /**
     * 自定义拦截器
     * 1写一个自定义的MyInterceptor实现HandlerInterceptor接口
     * 2在springmvc的配置文件中配置
     * 3拦截器的执行顺序
     * FirstInterceptor#preHandle
     * -->HandlerAdapter#handle
     * -->FirstInterceptor#postHandle
     * -->DispatcherServlet#render
     * -->FirstInterceptor#afterCompletion
     */




}
