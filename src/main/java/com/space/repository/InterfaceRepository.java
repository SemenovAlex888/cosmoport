package com.space.repository;

/*
 данный интерфейс (InterfaceRepository) необходим для взаимодействия приложения с БД
 Этот интерфейс будет наследоать JPA репозиторий
 Java Persistence API (JPA) — спецификация API Java EE, предоставляет возможность сохранять в удобном виде Java-объекты в базе данных

 */

import com.space.model.EntityShip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InterfaceRepository extends JpaRepository<EntityShip, Long>, JpaSpecificationExecutor<EntityShip> {
    // <EntityShip, Long> - Long в данном случае - это тип айдишника сущности EntityShip

    /*
    Интерфейс JpaSpecificationExecutor<T> определяет несколько методов:

    T FindOne(Predicate) — возвращает один объект, соответствующий условия
    Iterable<T> findAll(Predicate) — возвращает несколько объектов, соответствующих условию. Обратите внимание, что возвращается всегда Iterable<T>, без возможности уточнить тип
    long count(Predicate) — возвращает количество объектов в базе данных, соответствующих условию
    boolean exists(Predicate) — сообщает, есть ли в базе данных объект соответствующий условию

     */
}
