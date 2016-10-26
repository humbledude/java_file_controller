package com.humbledude.common;

import com.humbledude.common.io.Files;

/**
 * Created by keunhui.park on 2016. 10. 26..
 */
public class App {

  public static void main(String args[]) {
    System.out.println("hello world");

    Files files = new Files("./");

    int ret = files.write("new file", "contents");
    System.out.println("write " + ret);

    ret = files.copyForce("new file", "new file2");
    System.out.println("copyForce " + ret);

  }
}
