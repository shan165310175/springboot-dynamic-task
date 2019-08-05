package com.daily.taskcenter.config.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * swagger文档简单配置
 *
 * @author z
 */
@ConfigurationProperties(prefix = "swagger", ignoreInvalidFields = true)
@Data
public class SwaggerProperties {
	// 是否启用(默认不启用)
	private boolean enabled = true;

	private String title = "接口文档";
	private String description;
	private String termsOfServiceUrl;
	private String version;
	// 联系人
	private String contactName;
	private String contactUrl;
	private String contactEmail;

	//
	private String basePackages = "com.daily";
	private String[] excludePath = new String[]{ "/error", "/actuator/**", "/oauth/**" };

	private Boolean enableOauth2 = false;
	// 多个用逗号分隔
	private String scopes = "all";
	private String accessTokenUri = "http://localhost:8080/oauth/token";


	private List<GlobalParams> globalParams = new ArrayList<>();

	@Data
	public static class GlobalParams {
		// 参数名称
		private String name;
		// 参数类型
		private String parameterType = "string";
		// 描述
		private String description;
		// 位置类型： header, cookie, body, query
		private String type = "header";
		//
		private Boolean required = false;
	}
}