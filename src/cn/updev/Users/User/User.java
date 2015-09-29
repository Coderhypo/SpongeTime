package cn.updev.Users.User;

import cn.updev.Users.Static.IUser;
import cn.updev.Users.Static.UserRule;

/**
 * Created by hypo on 15-9-28.
 */
public class User implements IUser{
    private int userId;
    private String userName;
    private String nickName;
    private String eMail;
    private String url;
    private UserRule rule;

    public User(String eMail, String nickName, UserRule rule, String url, int userId, String userName) {
        this.eMail = eMail;
        this.nickName = nickName;
        this.rule = rule;
        this.url = url;
        this.userId = userId;
        this.userName = userName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public UserRule getRule() {
        return rule;
    }

    public void setRule(UserRule rule) {
        this.rule = rule;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isAdmin(){
        return rule.isAdmin();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return getUserId() == user.getUserId();

    }

    @Override
    public int hashCode() {
        return getUserId();
    }
}
