package cn.updev.Events.Event;

import cn.updev.Events.Static.EventWeight;
import cn.updev.Events.Static.IEvent;

import java.util.Date;

/**
 * Created by hypo on 15-9-29.
 */
public class EventFactory {

    private String eventTitle;
    private Date createTime;
    private Date expectTime;
    private EventWeight weight;
    private Integer ownerId;
    private Integer doerId;
    private Integer enderId;
    private Integer groupId;

    //个人事件添加
    public EventFactory(String eventTitle, Date expectTime,int ownerId, EventWeight weight) {
        this.eventTitle = eventTitle;
        this.expectTime = new Date(expectTime.getTime());
        this.groupId = null;
        this.ownerId = ownerId;
        this.weight = weight;
    }

    //团队事件添加
    public EventFactory(String eventTitle, Date expectTime, int groupId, int ownerId, EventWeight weight) {
        this.eventTitle = eventTitle;
        this.expectTime = new Date(expectTime.getTime());
        this.groupId = groupId;
        this.ownerId = ownerId;
        this.weight = weight;
    }

    //立马生成创建事件
    public Date getCreateTime() {
        createTime = new Date(new Date().getTime());
        return createTime;
    }

    //默认正在做这件事的人为创建者
    private Integer getDoerId() {
        doerId = ownerId;
        return doerId;
    }

    //事件没有完成，完成者为空
    private Integer getEnderId() {
        enderId = null;
        return enderId;
    }

    private String getEventTitle() {

        //限制事件Title长度，超出则截取
        if(eventTitle.length() > 20){
            eventTitle = eventTitle.substring(0,20);
        }

        return eventTitle;
    }

    //事件预期完成时间
    private Date getExpectTime() {

        return expectTime;
    }

    //如果不是团队事件，将返回null
    private Integer getGroupId() {
        return groupId;
    }

    //返回创建者的ID
    private Integer getOwnerId() {
        return ownerId;
    }

    //权重
    private EventWeight getWeight() {
        return weight;
    }

    //创建一个事件，通过数据持久化获得ID，并返回这个事件对象
    public IEvent getEvent(){

        IEvent event = new Event(getCreateTime(),getDoerId(),getEnderId(),
                null,getEventTitle(),getExpectTime(),null,getGroupId(),getOwnerId(),getWeight());


        return event;
    }
}
