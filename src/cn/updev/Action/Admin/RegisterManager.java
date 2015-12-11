package cn.updev.Action.Admin;

import cn.updev.Message.Email.MailSenderInfo;
import cn.updev.Message.Email.ThreadSenter;
import cn.updev.Message.Template.BasicTemplate;
import cn.updev.Message.Template.RegisterTemplate;
import cn.updev.Users.Static.EnumeRule.UserRule;
import cn.updev.Users.Static.FuctionClass.Login;
import cn.updev.Users.Static.FuctionClass.Register;
import cn.updev.Users.Static.UserOrGroupDAO.UserOrGroupQuery;
import cn.updev.Users.Static.UserOrGroupInterface.IUser;
import cn.updev.Users.User.UserFactory;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hypo on 15-11-28.
 */
public class RegisterManager extends ActionSupport {
    private Map json;
    private String email;
    private String password;
    private String remember;
    private String userName;
    private String nickName;
    private String rePassword;
    private String url;

    private String VC;
    private String code;

    /**
     *  运行日志
     */
    private Logger logger = Logger.getLogger(RegisterManager.class);

    public String execute() throws Exception {
        Login login = new Login();
        if(!login.isNotLogined()){
            logger.debug("用户未登录");
            return SUCCESS;
        }

        HttpServletRequest request = ServletActionContext.getRequest();
        String error;

        if(this.userName == null || this.nickName == null || this.email == null || this.password == null){
            logger.debug("用户提交了一份信息不健全的表单");
            return INPUT;
        }

        if(!this.password.equals(this.rePassword)){
            error = "您两次填写的密码不一致！";
            request.setAttribute("error", error);
            return INPUT;
        }

        if(this.code == null || !this.code.equals("ayanami")){
            error = "内测码错误！请联系hhHypo：i@ihypo.net。";
            request.setAttribute("error", error);
            logger.info("用户内测码错误");
            return INPUT;
        }
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_-]*$");
        Matcher matcher = pattern.matcher(this.userName);
        if(matcher.matches()){

            if(this.url == null){
                this.url = "";
            }

            Register register = new Register(this.userName, this.nickName, this.email, this.password ,this.url);
            IUser user = register.saveUserInfo();
            logger.info("用户 " + user.getUserId() + ":" + user.getNickName() + " 注册成功，准备向用户邮箱 "
                    + user.geteMail() + "发送邮件");
            userVerify(user.getNickName(), user.geteMail(), user.getPassWord());
            String mailURL = user.geteMail().split("@")[1];
            String info = "注册信息提交成功，已将激活链接发往您的邮箱：" + user.geteMail() +
                    "，请激活注册邮箱以完成注册！ " +
                    "<a target=\"_blank\" href=\"http://mail." + mailURL + "\" >登录邮箱</a>";
            request.setAttribute("error", info);
            return LOGIN;
        }else {
            error = "用户名不合法！";
            request.setAttribute("error", error);
            return INPUT;
        }
    }

    public String judgeMailName(){
        Map<String, Object> map = new HashMap<String, Object>();

        if(userName != null){

            if(userName.length() == 0){

                map.put("userName", false);
                map.put("message", "请输入用户名！");
            }else {
                Pattern pattern = Pattern.compile("^[a-zA-Z0-9_-]*$");
                Matcher matcher = pattern.matcher(userName);

                if(!matcher.matches() || "admin".equals(userName) || "root".equals(userName)){
                    map.put("userName", false);
                    map.put("message", "用户名格式不合法！");
                }else{

                    if(new UserOrGroupQuery().queryUserByName(userName) == null){
                        map.put("userName", true);
                    }else{
                        map.put("userName", false);
                        map.put("message", "用户名已存在");
                    }
                }
            }

        }
        if(email != null){
            Pattern pattern = Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
            Matcher matcher = pattern.matcher(email);

            if(!matcher.matches()){
                map.put("email", false);
                map.put("message", "邮箱格式不合法！");
            }else{
                if(new UserOrGroupQuery().queryUserByEMail(email) == null){
                    map.put("email", true);
                }else{
                    map.put("email", false);
                    map.put("message", "该邮箱已被注册！");
                }
            }
        }
        this.setJson(map);
        return SUCCESS;
    }

    public String makeVerify(){

        String verifyCode = getFromBase64(VC);

        if(verifyCode == null){
            return ERROR;
        }

        String[] userInfo = verifyCode.split(" ");

        UserOrGroupQuery userDao = new UserOrGroupQuery();
        IUser user = userDao.queryUserByEMail(userInfo[0]);

        if(user != null && user.getPassWord().equals(userInfo[1])){
            user.setRule(UserRule.User);
            UserFactory factory = new UserFactory();
            factory.updateUser(user);
            Login login = new Login();
            login.setUser(user);

            return SUCCESS;
        }

        return LOGIN;

    }

    private void userVerify(String userName, String email, String pass){

        logger.info("用户 " + userName + "邮箱验证中");
        MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setToAddress(email);
        String verifyCode = getBase64(email + " " + pass);
        if(verifyCode == null){
            logger.error("用户 " + userName + "邮箱验证失败：加密验证码获取失败");
            return;
        }
        String url = "http://todo.updev.cn/verify?VC=" + verifyCode;
        BasicTemplate template = new RegisterTemplate(userName, url).getTemplate();
        mailInfo.setSubject(template.getTitle());
        mailInfo.setContent(template.getContent());
        //这个类主要来发送邮件
        new Thread(new ThreadSenter(mailInfo)).start();
        logger.info("用户" + userName + "邮箱验证码 :" + verifyCode);
        logger.info("用户" + userName + "邮箱验证邮件发送完毕。");

    }

    // 加密
    private static String getBase64(String str) {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = new BASE64Encoder().encode(b);
        }

        if(s != null){
            char[] chars = s.toCharArray();

            for(int i = 0;i < chars.length;i++){
                if(chars[i] == '+'){
                    chars[i] = '-';
                }else if(chars[i] == '/'){
                    chars[i] = '_';
                }else if(chars[i] == '='){
                    chars[i] = '*';
                }
            }
            return new String(chars);
        }
        return null;
    }

    // 解密
    private static String getFromBase64(String s) {

        char[] chars = s.toCharArray();
        for(int i = 0;i < chars.length;i++){
            if(chars[i] == '-'){
                chars[i] = '+';
            }else if(chars[i] == '_'){
                chars[i] = '/';
            }else if(chars[i] == '*'){
                chars[i] = '=';
            }
        }

        s = new String(chars);

        byte[] b = null;
        String result = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                b = decoder.decodeBuffer(s);
                result = new String(b, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public Map getJson() {
        return json;
    }

    public void setJson(Map json) {
        this.json = json;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemember() {
        return remember;
    }

    public void setRemember(String remember) {
        this.remember = remember;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVC() {
        return VC;
    }

    public void setVC(String VC) {
        this.VC = VC;
    }
}
