package com.gaohw.entity;

import lombok.Data;

import java.util.Date;

/**
 * FileName: User
 * Author:   gaohuiwu
 * Date:     8/28/18 10:50 AM
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
public class User {
    private int id;
    private String name;
    private Date birth;
}
