package cn.updev.Message.Template;

import java.util.Date;

/**
 * Created by hypo on 15-11-27.
 */
public class RegisterTemplate {

    private String userNickName;
    private String title;
    private String content;
    private String url;

    public RegisterTemplate(String userNickName ,String url) {
        this.userNickName = userNickName;
        this.url = url;
        this.title = "欢迎" + userNickName + "注册";
    }

    private String getContent() {
        this.content = "<p>尊敬的 " + userNickName + ":</p>" +
                "<p>您好！\n 欢迎注册到SpongeTime。请点击下面的链接完成邮箱验证：</p>" +
                "<p><a href=\"" + url + "\" target=\"_blank\">" + url + "</a></p>" +
                "<p>如果不能打开，请把链接复制到浏览器的地址栏中访问。_(:з」∠)_</p>" +
                "<p>不会下班的SpongeTime用户管理小哥敬上</p>" +
                "<p>" + (new Date()) + "</p>";
        return content;
    }

    private String getTitle() {
        return title;
    }

    public BasicTemplate getTemplate() {
        return new BasicTemplate(getTitle(),getContent());
    }
}
