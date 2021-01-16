package com.space.controller;

// в данном классе необходимо описать REST-методы

import com.space.model.EntityShip;
import com.space.model.ShipType;
import com.space.service.ShipServiceImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class ShipController {

    private final ShipServiceImpl shipService;

    public ShipController(ShipServiceImpl shipService) {
        this.shipService = shipService;
    }

    // Аннотация @RequestMapping предназначена для того, чтобы задать методам вашего
    // контроллера адреса, по которым они будут доступны на клиенте.
    /*
    У этой аннотации можно задать некоторые параметры:
    value - предназначен для указания адреса. Его обычно применяют, если задаётся более, чем один параметр.
    method - определяет метод доступа. Варианты - RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT и другие.
    consumes - определяет тип содержимого тела запроса. например, consumes="application/json" определяет, что Content-Type запроса, который отправил клиент должен быть "application/json".
    produces - определяет формат возвращаемого методом значения. Если на клиенте в header'ах не указан заголовок Accept, то не имеет значение, что установлено в produces. Если же заголовок Accept установлен, то значение produces должно совпадать с ним для успешного возвращения результата клиенту.
    params - позволяет отфильтровать запросы по наличию/отсутствию определённого параметра в запросе или по его значению. params="myParam=myValue", params="!myParam=myValue", params="myParam", params="!myParam".
    headers - позволяет отфильтровать запросы по наличию/отсутствию определённого заголовка в запросе или по его значению. headers="myHeader=myValue", headers="!myHeader=myValue", headers="myHeader", headers="!myHeader".
     */

      // метод возвращения листа всех экземпляров (Get ships list)
    /*
    РЕАЛИЗОВАТЬ ТРЕБОВАНИЯ ТЗ:
    Поиск по полям name и planet происходить по частичному
    соответствию. Например, если в БД есть корабль с именем
    «Левиафан», а параметр name задан как «иа» - такой корабль
    должен отображаться в результатах (Левиафан).
    pageNumber – параметр, который отвечает за номер
    отображаемой страницы при использовании пейджинга.
    Нумерация начинается с нуля
    pageSize – параметр, который отвечает за количество
    результатов на одной странице при пейджинге
     */

    @GetMapping("/ships")
    public ResponseEntity<List<EntityShip>> findAll(@RequestParam(value = "name", required = false) String name,
                                                    @RequestParam(value = "planet", required = false) String planet,
                                                    @RequestParam(value = "shipType", required = false) ShipType shipType,
                                                    @RequestParam(value = "after", required = false) Long after,
                                                    @RequestParam(value = "before", required = false) Long before,
                                                    @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                                    @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                                                    @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                                    @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                                                    @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                                    @RequestParam(value = "minRating", required = false) Double minRating,
                                                    @RequestParam(value = "maxRating", required = false) Double maxRating,
                                                    @RequestParam(value = "order", required = false, defaultValue = "ID") ShipOrder order,
                                                    @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                                    @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize) {
            // аннотация @RequestParam используется для привязки параметров запроса к параметру метода в контроллере.
            // Параметры, использующие эту аннотацию, являются обязательными по умолчанию, но вы можете указать, что параметр является необязательным, установив для атрибута required @ RequestParam значение false (например, @RequestParam (value = "id", required = false)).
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));

        Specification<EntityShip> specification = Specification.where(shipService.selectByName(name)
                .and(shipService.selectByPlanet(planet))
                .and(shipService.selectByShipType(shipType))
                .and(shipService.selectByProdDate(after, before))
                .and(shipService.selectByUse(isUsed))
                .and(shipService.selectBySpeed(minSpeed, maxSpeed))
                .and(shipService.selectByCrewSize(minCrewSize, maxCrewSize))
                .and(shipService.selectByRating(minRating, maxRating)));

        return new ResponseEntity<>(shipService.getShipsList(specification, pageable).getContent(), HttpStatus.OK);
    }

    // метод получения количества экземпляров (Get ships count)
    @GetMapping("/ships/count")
    public ResponseEntity<Integer> getCount(@RequestParam(value = "name", required = false) String name,
                                            @RequestParam(value = "planet", required = false) String planet,
                                            @RequestParam(value = "shipType", required = false) ShipType shipType,
                                            @RequestParam(value = "after", required = false) Long after,
                                            @RequestParam(value = "before", required = false) Long before,
                                            @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                            @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                                            @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                            @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                                            @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                            @RequestParam(value = "minRating", required = false) Double minRating,
                                            @RequestParam(value = "maxRating", required = false) Double maxRating) {

        Specification<EntityShip> specification = Specification.where(shipService.selectByName(name)
                .and(shipService.selectByPlanet(planet))
                .and(shipService.selectByShipType(shipType))
                .and(shipService.selectByProdDate(after, before))
                .and(shipService.selectByUse(isUsed))
                .and(shipService.selectBySpeed(minSpeed, maxSpeed))
                .and(shipService.selectByCrewSize(minCrewSize, maxCrewSize))
                .and(shipService.selectByRating(minRating, maxRating)));

        return new ResponseEntity<>(shipService.getShipsCount(specification), HttpStatus.OK);
    }

    // метод создания экземпляра (Create ship)
    @PostMapping("/ships")
    public ResponseEntity<EntityShip> createShip(@RequestBody EntityShip entityShip) {
        /*
        HTTP-запрос кроме заголовков и параметров имеет также основную часть - тело запроса. Её содержимое также может быть распознано как параметр в методе контроллера. Для того, чтобы это произошло, необходимо указать @RequestBody в объявлении этого параметра
         */
        EntityShip responseShip;

        responseShip = shipService.createShip(entityShip);

        return new ResponseEntity<>(responseShip, HttpStatus.OK);
    }

    // метод получения экземпляра по id (Get ship)
    @GetMapping("/ships/{id}")
    public ResponseEntity<EntityShip> getShipById(@PathVariable String id) {
        EntityShip responseShip;

        Long longId = shipService.checkId(id);
        responseShip = shipService.getShip(longId);

        return new ResponseEntity<>(responseShip, HttpStatus.OK);
    }

    // метод обновления экземпляра (Update ship)
    @PostMapping("/ships/{id}")
    public ResponseEntity<EntityShip> updateShip(@PathVariable String id,
                                           @RequestBody EntityShip entityShip) {
        EntityShip responseShip;

        Long longId = shipService.checkId(id);
        responseShip = this.shipService.updateShip(longId, entityShip);

        return new ResponseEntity<>(responseShip, HttpStatus.OK);
    }

    // метод удаления экземпляра по id (Delete ship)
    @DeleteMapping("/ships/{id}")
    public ResponseEntity<?> deleteShip(@PathVariable String id) {
        Long longId = shipService.checkId(id);

        shipService.deleteShip(longId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
