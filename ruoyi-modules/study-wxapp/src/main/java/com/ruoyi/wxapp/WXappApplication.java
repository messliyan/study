package com.ruoyi.wxapp;

import com.ruoyi.common.security.annotation.EnableCustomConfig;
import com.ruoyi.common.security.annotation.EnableRyFeignClients;
import com.ruoyi.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * 专题管理模块
 *
 * @author Li Jiabin
 */
@EnableCustomConfig
@EnableCustomSwagger2
@SpringCloudApplication
@EnableRyFeignClients
@SpringBootApplication
public class WXappApplication {

  public static void main(String[] args) {
    SpringApplication.run(WXappApplication.class, args);
    System.out.println("(♥◠‿◠)ﾉﾞ  小程序业务模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
        " .-------.       ____     __        \n" +
        " |  _ _   \\      \\   \\   /  /    \n" +
        " | ( ' )  |       \\  _. /  '       \n" +
        " |(_ o _) /        _( )_ .'         \n" +
        " | (_,_).' __  ___(_ o _)'          \n" +
        " |  |\\ \\  |  ||   |(_,_)'         \n" +
        " |  | \\ `'   /|   `-'  /           \n" +
        " |  |  \\    /  \\      /           \n" +
        " ''-'   `'-'    `-..-'              ");
  }
}
