package com.space.service;

/*
в данном интерфейсе будут прописаны все необходимые методы для сущности Ship
 */

import com.space.model.EntityShip;
import com.space.model.ShipType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface ShipService {
    /*
    Должны быть реализованы следующие возможности:
        1. получать список всех существующих кораблей;
        2. создавать новый корабль;
        3. редактировать характеристики существующего корабля;
        4. удалять корабль;
        5. получать корабль по id;
        6. получать отфильтрованный список кораблей в соответствии с переданными
        фильтрами;
        7. получать количество кораблей, которые соответствуют фильтрам
     */
    Page<EntityShip> getShipsList(Specification<EntityShip> specification, Pageable sortedBy);
    Integer getShipsCount(Specification<EntityShip> specification);
    EntityShip createShip(EntityShip entityShip);
    EntityShip getShip(Long id);
    Long checkId(String id);
    EntityShip updateShip(Long id, EntityShip ship);
    void deleteShip(Long id);

    Specification<EntityShip> selectByName(String name);
    Specification<EntityShip> selectByPlanet(String planet);
    Specification<EntityShip> selectByShipType(ShipType shipType);
    Specification<EntityShip> selectByProdDate(Long after, Long before);
    Specification<EntityShip> selectByUse(Boolean isUsed);
    Specification<EntityShip> selectBySpeed(Double minSpeed, Double maxSpeed);
    Specification<EntityShip> selectByCrewSize(Integer minCrewSize, Integer maxCrewSize);
    Specification<EntityShip> selectByRating(Double minRating, Double maxRating);

}
