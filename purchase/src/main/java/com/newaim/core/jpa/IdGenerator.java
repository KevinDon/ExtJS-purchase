package com.newaim.core.jpa;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Properties;
import java.util.Random;

/**
 * Created by Mark on 2017/9/5.
 */
public class IdGenerator implements Configurable, IdentifierGenerator {

    private String prefix;

    @Override
    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {
        this.prefix = properties.getProperty("prefix");
    }

    @Override
    public Serializable generate(SessionImplementor sessionImplementor, Object o) throws HibernateException {

        String idStr = prefix;

        Calendar now = Calendar.getInstance();
        String year = "00" + now.get(Calendar.YEAR);
        String month = "0" + now.get(Calendar.MONTH);
        String day = "0" + now.get(Calendar.DAY_OF_MONTH);
        String hour = "0" + now.get(Calendar.HOUR_OF_DAY);
        String minute = "0" + now.get(Calendar.MINUTE);
        String second = "0" + now.get(Calendar.SECOND);

        idStr += year.substring(year.length()-4);
        idStr += month.substring(month.length()-2);
        idStr += day.substring(day.length()-2);
        idStr += hour.substring(hour.length()-2);
        idStr += minute.substring(minute.length()-2);
        idStr += second.substring(second.length()-2);

        int max=9999999;
        int min=1000000;

        if(prefix.length() == 6){
            max=9999;
            min=1000;
        }else if(prefix.length() == 5){
            max=99999;
            min=10000;
        }else if(prefix.length() == 4){
            max=999999;
            min=100000;
        }else if(prefix.length() == 3){
            max=9999999;
            min=1000000;
        }else if(prefix.length() == 2){
            max=99999999;
            min=10000000;
        }

        Random random = new Random();
        idStr += random.nextInt(max)%(max-min+1) + min;

        System.out.println();
        return idStr;
    }
}
