package com.ruoyi.extra;

import com.ruoyi.common.security.annotation.EnableCustomConfig;
import com.ruoyi.common.security.annotation.EnableRyFeignClients;
import com.ruoyi.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * 附加功能模块
 *
 * @author Li Jiabin
 */
@EnableCustomConfig
@EnableCustomSwagger2
@SpringCloudApplication
@EnableRyFeignClients
@SpringBootApplication
public class ExtraApplication {

  public static void main(String[] args) {
    SpringApplication.run(ExtraApplication.class, args);
    System.out.println("(♥◠‿◠)ﾉﾞ  小程序附加功能启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
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
