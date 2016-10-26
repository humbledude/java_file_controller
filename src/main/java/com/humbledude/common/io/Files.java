package com.humbledude.common.io;

import com.humbledude.common.etc.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by keunhui.park on 2016. 10. 26..
 */
public class Files {

  private static final int FLAG_APPEND = 0x1;
  private static final int FLAG_FORCE = 0x2;

  private static final int OK = 0;
  private static final int ERROR = -1;
  private static final int ERROR_FILE_ALREADY_EXIST = -2;
  private static final int ERROR_FILE_NOT_EXIST = -3;

  private static final int BUF_SIZE = 4096;

  private String rootPath = null;

  public Files(String rootPath) {
//    this.rootPath = Utils.checkEndDirSeparator(rootPath);
    File root = new File(rootPath);
    String absolute = root.getAbsolutePath();
    this.rootPath = absolute.substring(0, absolute.length()-1);
  }

  public int write(String path, String contents) {
    return write(path, contents, 0);
  }

  public int append(String path, String contents) {
    return write(path, contents, FLAG_APPEND);
  }

  public int copy(String from, String to) {
    return copy(from, to, 0);
  }

  public int copyForce(String from, String to) {
    return copy(from, to, FLAG_FORCE);
  }

  public int delete(String path) {
//    path = Utils.checkDirSeparator(path);

    File file = new File(rootPath + path);
    if (!file.exists()) {
      return ERROR_FILE_NOT_EXIST;
    }
    if (!file.delete()) {
      return ERROR;
    }

    return OK;
  }

  public int move(String from, String to) {
    int ret = copy(from, to);
    if (ret != OK) {
      return ret;
    }
    return delete(from);
  }

  public int moveForce(String from, String to) {
    int ret = copyForce(from, to);
    if (ret != OK) {
      return ret;
    }
    return delete(from);
  }

  public int createFile(String path) {
//    path = Utils.checkDirSeparator(path);

    File file = new File(rootPath + path);
    if (file.exists()) {
      return ERROR_FILE_ALREADY_EXIST;
    }

    File parent = new File(file.getParent());
    if (!parent.exists()) {
      if (!parent.mkdirs()) {
        // mkdirs failed
        return ERROR;
      }
    }

    try {
      file.createNewFile();
    } catch (IOException e) {
      // create new file failed
      return ERROR;
    }

    return OK;
  }

  private int write(String path, String contents, int flag) {
//    path = Utils.checkDirSeparator(path);

    File file = new File(rootPath + path);

    if (!file.exists()) {
      createFile(path);
    }

    if (!file.setWritable(true)) {
      // setWritable error
      return ERROR;
    }

    try {
      byte[] tmp = contents.getBytes();
      BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
      // TODO : it limits file size to INTEGER!!
      int offset = 0;
      if ((flag & FLAG_APPEND) != 0) {
        offset = (int)file.length();
      }
      outputStream.write(tmp, offset, tmp.length);
      outputStream.close();
    } catch (FileNotFoundException e) {
      // file not found
      return ERROR_FILE_NOT_EXIST;
    } catch (IOException e) {
      // IO Exception
      return ERROR;
    }

    return OK;

  }

  private int copy(String from, String to, int flag) {
    //    from = Utils.checkDirSeparator(from);
    //    to = Utils.checkDirSeparator(to);

    File fromFile = new File(rootPath + from);
    if (!fromFile.exists()) {
      return ERROR_FILE_NOT_EXIST;
    }

    if ((flag & FLAG_FORCE) != 0) {
      delete(to);
    }

    if (createFile(to) < 0) {
      return ERROR_FILE_ALREADY_EXIST;
    }

    File toFile = new File(rootPath + to);
    if (!toFile.setWritable(true)) {
      return ERROR;
    }

    try {
      BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(fromFile));
      BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(toFile));
      byte[] buf = new byte[BUF_SIZE];
      // TODO : it limits file size to INTEGER!!
      int offset = 0;
      int read = 0;
      while((read = inputStream.read(buf)) >= 0) {
        outputStream.write(buf, offset, read);
        offset += read;
      }
      inputStream.close();
      outputStream.close();
    } catch (FileNotFoundException e) {
      // file not found
      return ERROR_FILE_NOT_EXIST;
    } catch (IOException e) {
      // IO Exception
      return ERROR;
    }

    return OK;
  }
}
