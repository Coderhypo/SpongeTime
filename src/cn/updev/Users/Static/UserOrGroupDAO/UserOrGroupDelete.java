package cn.updev.Users.Static.UserOrGroupDAO;

import cn.updev.Database.HibernateSessionFactory;
import cn.updev.Users.NotificationPush.NotificationInfo;
import cn.updev.Users.Static.UserOrGroupInterface.*;
import cn.updev.Users.User.GroupUser;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Iterator;
import java.util.List;

/**
 * Created by blf2 on 15-11-26.
 */
public class UserOrGroupDelete {
    public boolean deleteGroupMemberByName(String userName,Integer groupId){
        Session session = HibernateSessionFactory.currentSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("from User user where user.userName='"+userName+"'");
        if(query.list().size() == 0){
            return  false;
        }
        IUser iUser = (IUser)query.list().get(0);
        query = session.createQuery("from GroupUser groupUser where groupUser.userId="+iUser.getUserId()+
                " and groupUser.groupId="+groupId);
        if(query.list().size() == 0)
            return false;
        IGroupUser iGroupUser = (IGroupUser)query.list().get(0);
        session.delete(iGroupUser);
        transaction.commit();
        HibernateSessionFactory.closeSession();
        return true;
    }

    public boolean deleteGroupMemberById(Integer userId,Integer groupId){
        Session session = HibernateSessionFactory.currentSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("from GroupUser groupUser where groupUser.userId="+userId+
                " and groupUser.groupId="+groupId);
        if(query.list().size() == 0)
            return false;
        IGroupUser iGroupUser = (IGroupUser)query.list().get(0);
        session.delete(iGroupUser);
        transaction.commit();
        HibernateSessionFactory.closeSession();
        return true;
    }


    public boolean deleteGroupInfoById(Integer groupId){
        deleteGroupMemberAllById(groupId);
        Session session = HibernateSessionFactory.currentSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("from  GroupInfo groupInfo where groupInfo.groupId = " + groupId);
        if(query.list().size() == 0)
            return false;
        IGroupInfo iGroupInfo = (IGroupInfo)query.list().get(0);
        session.delete(iGroupInfo);
        transaction.commit();
        HibernateSessionFactory.closeSession();
        return true;
    }
    private boolean deleteGroupMemberAllById(Integer groupId){//用于解散一个团队
        Session session = HibernateSessionFactory.currentSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("from  GroupUser groupUser where groupUser.groupId = " + groupId);
        List<GroupUser> list = query.list();
        Iterator<GroupUser>iter = list.iterator();
        while(iter.hasNext()) {
            session.delete(iter.next());
        }
        transaction.commit();
        HibernateSessionFactory.closeSession();
        return true;
    }
    private boolean deleteUserAllGroup(Integer userId){
        Session session = HibernateSessionFactory.currentSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("from  GroupUser groupUser where groupUser.userId = " + userId);
        List<GroupUser>list = query.list();
        Iterator<GroupUser> iter = list.iterator();
        while(iter.hasNext()){
            session.delete(iter.next());
        }
        transaction.commit();
        HibernateSessionFactory.closeSession();
        return true;
    }
    public boolean deleteUserById(Integer userId){
        deleteUserAllGroup(userId);
        Session session = HibernateSessionFactory.currentSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("from User user where user.userId=" + userId);
        if(query.list().size() == 0)
            return  false;
        IUser iUser = (IUser)query.list().get(0);
        session.delete(iUser);
        transaction.commit();
        HibernateSessionFactory.closeSession();
        return true;
    }
    public boolean deleteUserByEMail(String eMail){
        Session session = HibernateSessionFactory.currentSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("from User user where user.eMail='" + eMail +"'");
        if(query.list().size() == 0)
            return false;
        IUser iUser = (IUser)query.list().get(0);
        deleteUserAllGroup(iUser.getUserId());
        session.delete(iUser);
        transaction.commit();
        HibernateSessionFactory.closeSession();
        return true;
    }

    public boolean deleteGroupMemberInviteInfo(IGroupMemberInviteInfo iGroupMemberInviteInfo){
        Session session = HibernateSessionFactory.currentSession();
        Transaction transaction = session.beginTransaction();
        session.delete(iGroupMemberInviteInfo);
        transaction.commit();
        HibernateSessionFactory.closeSession();
        return true;
    }

    public boolean deleteNotificationInfo(INotificationInfo iNotificationInfo){
        Session session = HibernateSessionFactory.currentSession();
        Transaction transaction = session.beginTransaction();
        session.delete(iNotificationInfo);
        transaction.commit();
        HibernateSessionFactory.closeSession();
        return true;
    }

    public boolean deleteNotificationInfoByList(List<NotificationInfo>list){
        Session session = HibernateSessionFactory.currentSession();
        Transaction transaction = session.beginTransaction();
        Iterator<NotificationInfo>iter = list.iterator();
        while(iter.hasNext()) {
            session.delete(iter.next());
        }
        transaction.commit();
        HibernateSessionFactory.closeSession();
        return true;
    }
}
