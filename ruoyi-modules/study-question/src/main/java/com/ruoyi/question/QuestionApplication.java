package com.ruoyi.question;

import com.ruoyi.common.security.annotation.EnableCustomConfig;
import com.ruoyi.common.security.annotation.EnableRyFeignClients;
import com.ruoyi.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * 题库管理模块
 *
 * @author ruoyi
 */
@EnableCustomConfig
@EnableCustomSwagger2
@SpringCloudApplication
@EnableRyFeignClients
@SpringBootApplication
public class QuestionApplication {

  public static void main(String[] args)
  {
    SpringApplication.run(QuestionApplication.class, args);
    System.out.println("(♥◠‿◠)ﾉﾞ  题库管理模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
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
