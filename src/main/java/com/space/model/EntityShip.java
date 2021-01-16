package com.space.model;

//

import javax.persistence.*;
import java.util.Date;

@Entity     // данной анотацией указываем, что этот класс будет описывать сущность
@Table(name = "ship")   // здесь указываем ссылку на название таблицы из БД "cosmoport"
public class EntityShip {
    @Id     // Аннотация @Id наследуется от javax.persistence.Id , указывая, что поле элемента ниже является первичным ключом текущей сущности.
    @Column(name = "id") // аннотирование используется для указания соответствия между атрибутом базовой сущности (Entity класса) и столбцом таблицы базы данных.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Аннотация @GeneratedValue предназначена для настройки способа приращения указанного столбца (поля). Например, при использовании Mysql можно указать auto_increment в определении таблицы, чтобы сделать ее самоинкрементной
    private Long id;

    @Column(name = "name")
    private String name;     //Название корабля (до 50 знаков включительно)

    @Column(name = "planet")
    private String planet;  // Планета пребывания (до 50 знаков включительно)

    @Enumerated(EnumType.STRING)    // Для облегчения использования перечислений JPA предоставляет аннотацию @Enumerated, которая указывает, что свойство должно обрабатываться как перечисление. EnumType.STRING указывает на то, что значение перечисления должно быть сохранено в БД строкой
    @Column(name = "shipType")
    private ShipType shipType;  // Тип корабля

    @Column(name = "prodDate")
    private Date prodDate;  // Дата выпуска. Диапазон значений года 2800..3019 включительно

    @Column(name = "isUsed")
    private Boolean isUsed; // Использованный / новый

    @Column(name = "speed")
    private Double speed;   // Максимальная скорость корабля. Диапазон значений 0,01..0,99 включительно. Используй математическое округление до сотых.

    @Column(name = "crewSize")
    private Integer crewSize;   // Количество членов экипажа. Диапазон значений 1..9999 включительно.

    @Column(name = "rating")
    private Double rating;  // Рейтинг корабля. Используй математическое округление до сотых.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public Date getProdDate() {
        return prodDate;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "EntityShip{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", planet='" + planet + '\'' +
                ", shipType=" + shipType +
                ", prodDate=" + prodDate +
                ", isUsed=" + isUsed +
                ", speed=" + speed +
                ", crewSize=" + crewSize +
                ", rating=" + rating +
                '}';
    }
}
