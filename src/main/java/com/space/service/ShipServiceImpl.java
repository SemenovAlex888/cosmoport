package com.space.service;

// класс, реализующий соответствующие методы из интерфейса

import com.space.model.EntityShip;
import com.space.model.ShipType;
import com.space.repository.InterfaceRepository;
import exceptions.BadRequestException;
import exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Service // обозначили, что это сервисный класс
public class ShipServiceImpl implements ShipService {

    @Autowired
    InterfaceRepository interfaceRepository; // связываем наш сервисный класс с интерфейсом из репозиторного слоя (для возможности вызова в данном классе нужных методов)

    // Returns all entities matching the given Specification and Sort.
    @Override
    public Page<EntityShip> getShipsList(Specification<EntityShip> specification, Pageable sortedBy) {
        return interfaceRepository.findAll(specification, sortedBy);
    }

    @Override
    public Integer getShipsCount(Specification<EntityShip> specification) {
        return interfaceRepository.findAll(specification).size();
    }

    @Override
    public EntityShip createShip(EntityShip entityShip) {
        /*
        Мы не можем создать корабль, если:
            - указаны не все параметры из Data Params (кроме isUsed);
            В случае всего вышеперечисленного необходимо ответить
            ошибкой с кодом 400.
         */
        if (entityShip.getName() == null || entityShip.getPlanet() == null || entityShip.getShipType() == null || entityShip.getProdDate() == null
                || entityShip.getSpeed() == null || entityShip.getCrewSize() == null) {
            throw new BadRequestException();
        }
        /*
        Проверяем выполнение условий:
            Мы не можем создать корабль, если:
            - длина значения параметра “name” или “planet” превышает
            размер соответствующего поля в БД (50 символов);
            - значение параметра “name” или “planet” пустая строка;
            - скорость или размер команды находятся вне заданных
            пределов;
            - “prodDate”:[Long] < 0;
            - год производства находятся вне заданных пределов.
            В случае всего вышеперечисленного необходимо ответить
            ошибкой с кодом 400.
         */
        checkShipName(entityShip);
        checkShipPlanet(entityShip);
        checkShipProdDate(entityShip);
        checkShipSpeed(entityShip);
        checkShipCrewSize(entityShip);

        if (entityShip.getUsed() == null) {
            entityShip.setUsed(false);
        }

        Double rating = computeRating(entityShip);
        entityShip.setRating(rating);

        return interfaceRepository.save(entityShip);
    }

    private void checkShipName(EntityShip entityShip) {
        String name = entityShip.getName();
        if (name.length() < 1 || name.length() > 50) {
            throw new BadRequestException();
        }
    }

    private void checkShipPlanet(EntityShip entityShip) {
        String planet = entityShip.getPlanet();
        if (planet.length() < 1 || planet.length() > 50) {
            throw new BadRequestException();
        }
    }

    private void checkShipProdDate(EntityShip entityShip) {
        Date prodDate = entityShip.getProdDate();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(prodDate);
        int year = calendar.get(Calendar.YEAR);
        if (year < 2800 || year > 3019) {
            throw new BadRequestException();
        }
    }

    private void checkShipSpeed(EntityShip entityShip) {
        Double speed = entityShip.getSpeed();
        if (speed < 0.01 || speed > 0.99) {
            throw new BadRequestException();
        }
    }

    private void checkShipCrewSize(EntityShip entityShip) {
        Integer crewSize = entityShip.getCrewSize();
        if (crewSize < 1 || crewSize > 9999) {
            throw new BadRequestException();
        }
    }

    private Double computeRating(EntityShip entityShip) {
        double k = entityShip.getUsed() ? 0.5 : 1;
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(entityShip.getProdDate());
        int prodYear = calendar.get(Calendar.YEAR);
        BigDecimal rating = BigDecimal.valueOf((80 * entityShip.getSpeed() * k) / (3019 - prodYear + 1)).setScale(2, RoundingMode.HALF_UP);
        return rating.doubleValue();
    }

    @Override
    public EntityShip getShip(Long id) {
        if(!interfaceRepository.existsById(id)) {
            throw new NotFoundException();
        }
        // Optional<T> - Контейнерный объект, который может содержать или не содержать ненулевое значение. Если значение присутствует, isPresent () вернет true, а get () вернет значение.
        return interfaceRepository.findById(id).get();
    }

    @Override
    public void deleteShip(Long id) {
        if(!interfaceRepository.existsById(id)) {
            throw new NotFoundException();
        }

        interfaceRepository.deleteById(id);
    }

    @Override
    public Long checkId(String id) {
        Long longId = null;

        if (id == null || id.equals("") || id.equals("0")) {
            throw new BadRequestException();
        }

        try {
            longId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException();
        }

        if (longId < 0) {
            throw new BadRequestException();
        }

        return longId;
    }

    @Override
    public EntityShip updateShip(Long id, EntityShip entityShip) {
        EntityShip updatedShip = getShip(id);

        String name = entityShip.getName();
        if (name != null) {
            checkShipName(entityShip);
            updatedShip.setName(name);
        }

        String planet = entityShip.getPlanet();
        if (planet != null) {
            checkShipPlanet(entityShip);
            updatedShip.setPlanet(planet);
        }

        ShipType shipType = entityShip.getShipType();
        if (shipType != null) {
            updatedShip.setShipType(shipType);
        }

        Date prodDate = entityShip.getProdDate();
        if (prodDate != null) {
            checkShipProdDate(entityShip);
            updatedShip.setProdDate(prodDate);
        }

        Boolean isUsed = entityShip.getUsed();
        if (isUsed != null) {
            updatedShip.setUsed(isUsed);
        }

        Double speed = entityShip.getSpeed();
        if (speed != null) {
            checkShipSpeed(entityShip);
            updatedShip.setSpeed(speed);
        }

        Integer crewSize = entityShip.getCrewSize();
        if (crewSize != null) {
            checkShipCrewSize(entityShip);
            updatedShip.setCrewSize(crewSize);
        }

        Double rating = computeRating(updatedShip);
        updatedShip.setRating(rating);

        return interfaceRepository.save(updatedShip);
    }

    @Override
    public Specification<EntityShip> selectByName(String name) {
        return new Specification<EntityShip>() {
            @Override
            public Predicate toPredicate(Root<EntityShip> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (name == null) {
                    return null;
                }
                return criteriaBuilder.like(root.get("name"), "%" + name + "%");
            }
            // CriteriaBuilder - Используется для построения запросов критериев, составных выборок, выражений, предикатов, упорядочения.
        };
    }

    @Override
    public Specification<EntityShip> selectByPlanet(String planet) {
        return new Specification<EntityShip>() {
            @Override
            public Predicate toPredicate(Root<EntityShip> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (planet == null) {
                    return null;
                }
                return criteriaBuilder.like(root.get("planet"), "%" + planet + "%");
            }
        };
    }

    @Override
    public Specification<EntityShip> selectByShipType(ShipType shipType) {
        return new Specification<EntityShip>() {
            @Override
            public Predicate toPredicate(Root<EntityShip> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (shipType == null) {
                    return null;
                }
                return criteriaBuilder.equal(root.get("shipType"), shipType);
            }
        };
    }

    @Override
    public Specification<EntityShip> selectByProdDate(Long after, Long before) {
        return new Specification<EntityShip>() {
            @Override
            public Predicate toPredicate(Root<EntityShip> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (after == null && before == null) {
                    return null;
                }

                if (after == null) {
                    Date tempBefore = new Date(before-1);
                    return criteriaBuilder.lessThanOrEqualTo(root.get("prodDate"), tempBefore);
                }

                if (before == null) {
                    Date tempAfter = new Date(after);
                    return criteriaBuilder.greaterThanOrEqualTo(root.get("prodDate"), tempAfter);
                }

                Calendar beforeCalendar = new GregorianCalendar();
                beforeCalendar.setTime(new Date(before-1));
                beforeCalendar.set(Calendar.HOUR, 0);
                beforeCalendar.add(Calendar.MILLISECOND, -1);

                Date tempAfter = new Date(after);
                Date tempBefore = beforeCalendar.getTime();

                return criteriaBuilder.between(root.get("prodDate"), tempAfter, tempBefore);
            }
        };
    }

    @Override
    public Specification<EntityShip> selectByUse(Boolean isUsed) {
        return new Specification<EntityShip>() {
            @Override
            public Predicate toPredicate(Root<EntityShip> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (isUsed == null) {
                    return null;
                }
                if (isUsed) {
                    return criteriaBuilder.isTrue(root.get("isUsed"));
                } else {
                    return criteriaBuilder.isFalse(root.get("isUsed"));
                }
            }
        };
    }

    @Override
    public Specification<EntityShip> selectBySpeed(Double minSpeed, Double maxSpeed) {
        return new Specification<EntityShip>() {
            @Override
            public Predicate toPredicate(Root<EntityShip> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (minSpeed == null && maxSpeed == null) {
                    return null;
                }
                if (minSpeed == null) {
                    return criteriaBuilder.lessThanOrEqualTo(root.get("speed"), maxSpeed);
                }
                if (maxSpeed == null) {
                    return criteriaBuilder.greaterThanOrEqualTo(root.get("speed"), minSpeed);
                }
                return criteriaBuilder.between(root.get("speed"), minSpeed, maxSpeed);
            }
        };
    }

    @Override
    public Specification<EntityShip> selectByCrewSize(Integer minCrewSize, Integer maxCrewSize) {
        return new Specification<EntityShip>() {
            @Override
            public Predicate toPredicate(Root<EntityShip> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (minCrewSize == null && maxCrewSize == null) {
                    return null;
                }
                if (minCrewSize == null) {
                    return criteriaBuilder.lessThanOrEqualTo(root.get("crewSize"), maxCrewSize);
                }
                if (maxCrewSize == null) {
                    return criteriaBuilder.greaterThanOrEqualTo(root.get("crewSize"), minCrewSize);
                }
                return criteriaBuilder.between(root.get("crewSize"), minCrewSize, maxCrewSize);
            }
        };
    }

    @Override
    public Specification<EntityShip> selectByRating(Double minRating, Double maxRating) {
        return new Specification<EntityShip>() {
            @Override
            public Predicate toPredicate(Root<EntityShip> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (minRating == null && maxRating == null) {
                    return null;
                }
                if (minRating == null) {
                    return criteriaBuilder.lessThanOrEqualTo(root.get("rating"), maxRating);
                }
                if (maxRating == null) {
                    return criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), minRating);
                }
                return criteriaBuilder.between(root.get("rating"), minRating, maxRating);
            }
        };
    }
}
