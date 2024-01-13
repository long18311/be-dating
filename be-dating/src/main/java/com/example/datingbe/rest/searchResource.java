package com.example.datingbe.rest;

import com.example.datingbe.dto.CombinedData;
import com.example.datingbe.dto.UserSearchPercent;
import com.example.datingbe.entity.InformationOption;
import com.example.datingbe.entity.User;
import com.example.datingbe.repository.InformationOptionRepository;
import com.example.datingbe.service.SearchService;
import com.example.datingbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
public class searchResource {

    @Autowired
    private SearchService service;
    @Autowired
    private InformationOptionRepository informationOptionRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/propose")
    public ResponseEntity<List<UserSearchPercent>> getUserSearchPercentByOppositeSex() {
        User userLogin = userService.getUserWithAuthority();
        return ResponseEntity.ok(service.getUserSearchPercentByOppositeSex(userLogin));
    }

    @PostMapping("/users-by-gender-age-location")
    public ResponseEntity<?> getUsersByGenderAndAge(
            @RequestParam Long id,
            @RequestParam String gender,
            @RequestParam int ageMin,
            @RequestParam int ageMax,
            @RequestParam float heightMin,
            @RequestParam float heightMax,
            @RequestParam float weightMin,
            @RequestParam float weightMax,
            @RequestParam Double locationMin,
            @RequestParam Double locationMax,
            @RequestParam("latitude") String latitude,
            @RequestParam("longitude") String longitude,
            @RequestParam("city") String city,
            @RequestParam("ward") String ward,
            @RequestParam("district") String district,
            @RequestBody List<Long> informationOptionIDs

    ) {

    try {
        if(informationOptionIDs != null){
            informationOptionIDs.forEach(info -> System.out.println(info));
        }
        List<CombinedData> newList = new ArrayList<>();

        User userLogged = userService.getOne(id);

        System.out.println(city);
        System.out.println(ward);
        System.out.println(district);

        userLogged.setLatitude(Double.parseDouble(latitude));
        userLogged.setLongitude(Double.parseDouble(longitude));
        userLogged = userService.save(userLogged);
        List<User> usersFind = service.findUsersByGenderAgeHeightWeightCityWardDistrict(gender.trim(), ageMin,  ageMax, heightMin, heightMax, weightMin, weightMax, city, ward, district);
        List<User> usersFinds2 = new ArrayList<User>();

        Set<Long> optionIDs = new HashSet<>();
        for (Long informationOptionid : informationOptionIDs) {
            optionIDs.add(informationOptionid);
        }

        for (User user : usersFind) {
            Set<Long> userOptionIDs = user.getInformationOptions()
                    .stream()
                    .map(InformationOption::getId)
                    .collect(Collectors.toSet());

            if (userOptionIDs.containsAll(optionIDs)) {
                usersFinds2.add(user);
            }
        }
        for (User o : usersFinds2) {
            double kms = userService.getDistanceBetweenPointsNew(userLogged.getLatitude(), userLogged.getLongitude(), o.getLatitude(), o.getLongitude(), "kilometers");
           if (kms >= locationMin && kms <= locationMax) {
                newList.add(new CombinedData(userLogged, o, kms));
            }
        }
        return ResponseEntity.ok(newList);

    }catch (Exception e){
        return ResponseEntity.status(500).body("Lỗi Server: locationMin hoặc locationMax là null");
    }
    }

}