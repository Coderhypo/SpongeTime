package cn.updev.Users.Group.GroupMemberRule;

/**
 * Created by blf2 on 15-10-8.
 */

import cn.updev.Users.Group.GroupInfo.*;
import cn.updev.Users.User.GroupUser;
import cn.updev.Users.User.GroupUserFactory;

public class PrimaryUserRule {
    public boolean createGroup(GroupInfoFactory gif,GroupUserFactory creater){//前端传来请求,包括
       String name = gif.getGroupName();
        //判断是否存在这个名字的团队
        //如果不存在则
        GroupInfo gi  = gif.getGroupInfo();
        //数据库持久化

        new GroupMemberInfoFactory().addGroupMemberInfo(creater.getGroupUser());
        return true;
    }
    public boolean inviteUser(GroupMemberInviteInfoFactory gmiif){
        return gmiif.saveGroupMemberInviteInfo();
    }
}
