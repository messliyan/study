package com.ruoyi.common.core.utils.poi;

import com.ruoyi.common.core.annotation.Excel;
import java.lang.reflect.Field;

/**
 * @author jiabin
 * @version 1.0
 * @date 2021-04-02 10:38
 */

public class ImportExcel {

  public Field field;
  public Excel excel;


  public Field getField() {
    return field;
  }

  public void setField(Field field) {
    this.field = field;
  }

  public Excel getExcel() {
    return excel;
  }

  public void setExcel(Excel excel) {
    this.excel = excel;
  }
}
