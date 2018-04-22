package org.activiti.myExplorer.commonservice;

import java.io.File;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Service
public class ThirdVelocityEmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private VelocityEngine velocityEngine;

	private String systemEmail;

	public void sendEmail(final Map<String, Object> model,
			final String subject, final String vmfile, final String[] mailTo,
			final String[] files) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			// 注意MimeMessagePreparator接口只有这一个回调函数
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
						true, "utf-8");
				// 这是一个生成Mime邮件简单工具，如果不使用GBK这个，中文会出现乱码
				message.setTo(mailTo);// 设置接收方的email地址
				message.setSubject(subject);// 设置邮件主题
				message.setFrom(systemEmail);// 设置发送方地址
				String text = VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine, vmfile, "utf-8", model);
				// 从模板中加载要发送的内容，vmfile就是模板文件的名字
				// 注意模板中有中文要加GBK，model中存放的是要替换模板中字段的值
				message.setText(text, true);
				// 将发送的内容赋值给MimeMessageHelper,后面的true表示内容解析成html
				FileSystemResource file;
				for (String s : files)// 添加附件
				{
					file = new FileSystemResource(new File(s));// 读取附件
					message.addAttachment(s, file);// 向email中添加附件
				}
			}
		};

		javaMailSender.send(preparator);// 发送邮件
	}

	public String getSystemEmail() {
		return systemEmail;
	}

	public void setSystemEmail(String systemEmail) {
		this.systemEmail = systemEmail;
	}

}
