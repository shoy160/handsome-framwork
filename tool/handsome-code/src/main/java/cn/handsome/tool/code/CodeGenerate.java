package cn.handsome.tool.code;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author shoy
 * @date 2022/1/13
 */
public class CodeGenerate {
    public static void main(String[] args) {
        String url = "jdbc:mysql://10.0.0.18:32406/db_risk?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true";
        String name = "root";
        String password = "authing1129";
        FastAutoGenerator.create(url, name, password)
                // 全局配置
                .globalConfig((scanner, builder) -> {
                    builder
                            .author(scanner.apply("请输入作者名称？"))
//                            .outputDir(scanner.apply("请输入目录地址？"))
                            .fileOverride();
                })
                // 包配置
                .packageConfig((scanner, builder) -> {
                    builder.parent(scanner.apply("请输入包名？"));
                })
                // 策略配置
                .strategyConfig((scanner, builder) -> {
                    List<String> tables = getTables(scanner.apply("请输入表名，多个英文逗号分隔？所有输入 all"));
                    builder
                            .addInclude(tables)
                            .controllerBuilder()
                            .enableRestStyle()
                            .enableHyphenStyle()
                            .entityBuilder()
                            .enableLombok()
                            .addTableFills(
                                    new Column("create_time", FieldFill.INSERT)
                            ).build();
                })
                /*
                    模板引擎配置，默认 Velocity 可选模板引擎 Beetl 或 Freemarker
                   .templateEngine(new BeetlTemplateEngine())
                   .templateEngine(new FreemarkerTemplateEngine())
                 */
                .execute();
    }

    /**
     * 处理 all 情况
     *
     * @param tables
     * @return
     */
    private static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }
}
