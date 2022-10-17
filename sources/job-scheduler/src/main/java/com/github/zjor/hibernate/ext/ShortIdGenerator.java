package com.github.zjor.hibernate.ext;

import lombok.extern.slf4j.Slf4j;
import org.hashids.Hashids;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;
import java.util.Random;

@Slf4j
public class ShortIdGenerator implements IdentifierGenerator, Configurable {

    public static final int MIN_LENGTH = 12;

    private Hashids hashids;
    private Random random;

    @Override
    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {
        long now = System.currentTimeMillis();
        random = new Random(now);
        hashids = new Hashids(String.valueOf(now), MIN_LENGTH);
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return hashids.encode(System.currentTimeMillis() + Math.abs(random.nextLong()) % 10000);
    }
}
