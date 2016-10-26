package com.humbledude.common.etc;

/**
 * Created by keunhui.park on 2016. 10. 26..
 */
public class Utils {


  /**
   * add '/' at start and remove '/' at end
   * @param path
   * @return
   */
  public static String checkDirSeparator(String path) {
    return checkEndDirSeparator(checkStartDirSeparator(path));
  }

  /**
   * add '/' at start
   * @param path
   * @return
   */
  public static String checkStartDirSeparator(String path) {
    path = path.trim();

    if (path.startsWith("./")) {
      path = path.substring(1, path.length());
    }

    if (!path.startsWith("/")) {
      path = "/" + path;
    }

    return path;
  }

  /**
   * remove '/' at end
   * @param path
   * @return
   */
  public static String checkEndDirSeparator(String path) {
    path = path.trim();

    if (path.endsWith("/")) {
      path = path.substring(0, path.length()-1);
    }

    return path;
  }


}
