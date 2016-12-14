package ru.levelp.dao;

import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import ru.levelp.Server.HibernateManager;
import ru.levelp.Server.Message;

import java.sql.Timestamp;
import java.util.List;

public class MessageService {
    Session session;

    public MessageService() {
        this.session = HibernateManager.getInstance().getSession();
    }

    public void addMessage(Message message) {


        session.beginTransaction();

        session.saveOrUpdate(message);

        session.getTransaction().commit();
    }

    public List<Message> getAllMessage(){
        List<Message> list = session.createCriteria(Message.class).list();
        return list;
    }



    public void deleteUser(long id){

        Message message = (Message)session.createCriteria(Message.class)
                .add(Restrictions.eq("id",id))
                .uniqueResult();

        if(message!=null){
            session.delete(message);
        }
    }
    //Метод получения истории сообщений, отсортированных по времени, в которых клиент является либо отправителем
    //либо получателем
    public List<Message> getHistory(String name){

        //Критерион c1 - проверка на равенство поля "sender" в базе данных присланному имени клиента
        Criterion c1 = Restrictions.eq("sender",name);
        //Критерион c1 - проверка на равенство поля "receiver" в базе данных присланному имени клиента
        Criterion c2 = Restrictions.eq("receiver",name);

        List<Message> list = session.createCriteria(Message.class)
                .add(Restrictions.or(c1,c2)).addOrder(Order.asc("timestamp")).list();

        if(list!=null){return list;}
        return null;

    }
    public void deleteAll(){

        List<Message> list = session.createCriteria(Message.class).list();

        for(Message message:list){ session.delete(message);}

    }
}
