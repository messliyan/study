package com.ruoyi.common.log.service;

import com.ruoyi.system.api.RemoteLogService;
import com.ruoyi.system.api.domain.SysOperLog;
import javax.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异步调用日志服务
 *
 * @author ruoyi
 */
@Service
public class AsyncLogService {

  @Resource
  private RemoteLogService remoteLogService;

  /**
   * 保存系统日志记录
   */
  @Async
  public void saveSysLog(SysOperLog sysOperLog) {
    remoteLogService.saveLog(sysOperLog);
  }
}
