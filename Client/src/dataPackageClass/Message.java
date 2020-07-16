package dataPackageClass;
/*
序列化记得包名要相同否则会出现找不到类的错
 */
import javax.swing.text.SimpleAttributeSet;
import java.io.Serializable;

/**
 * 发送的消息类
 * @author Florence
 *
 */
public class Message implements Serializable{
    private static final long serialVersionUID = -778880864897981502L;
    private String MessageType;
    private String Content;
    private String Time;
    private String Sender;
    private String Getter;
    private User user;
    private String messageTypeDetail;
    private SimpleAttributeSet attributeSet;
    private String friendsName;
    public String getFriendsName() { return friendsName; }

    public void setFriendsName(String friendsName) { this.friendsName = friendsName; }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Message(){ }

    public Message(String MessageType,String Content,String Time,String Sender,String Getter){
        this.MessageType = MessageType;
        this.Content = Content;
        this.Time = Time;
        this.Sender = Sender;
        this.Getter = Getter;
    }

    public String getMessageType() { return MessageType; }

    public void setMessageType(String messageType) {
        MessageType = messageType;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getTime() { return Time; }

    public void setTime(String time) {
        Time = time;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getGetter() {
        return Getter;
    }

    public void setGetter(String getter) {
        Getter = getter;
    }

    public String getMessageTypeDetail() {return messageTypeDetail; }

    public void setMessageTypeDetail(String messageTypeDetail) { this.messageTypeDetail = messageTypeDetail; }

    public SimpleAttributeSet getAttributeSet() { return attributeSet; }

    public void setAttributeSet(SimpleAttributeSet attributeSet) { this.attributeSet = attributeSet; }
}
