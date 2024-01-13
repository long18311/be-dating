package com.example.datingbe.service;

import com.example.datingbe.dto.UserSearchPercent;
import com.example.datingbe.entity.InformationOption;
import com.example.datingbe.entity.User;
import com.example.datingbe.repository.FriendRequestRepository;
import com.example.datingbe.repository.InformationOptionRepository;
import com.example.datingbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SearchService {

    @Autowired
    private InformationOptionRepository informationOptionRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendRequestRepository friendRequestRepository;

    public List<InformationOption> getInformationOption() {
        return informationOptionRepository.findAll();
    }
    public List<User> findUsersByGenderAndAgeRange(String gender, int ageMin, int ageMax){
        return userRepository.findUsersByGenderAndAgeRange( gender,  ageMin,  ageMax);
    }public List<User> findUsersByGenderAgeHeightWeightCityWardDistrict(String gender, int ageMin, int ageMax,float heightMin, float heightMax, float weightMin, float weightMax, String city, String ward, String district){
        return userRepository.findUsersByGenderAgeHeightWeightCityWardDistrict( gender,  ageMin,  ageMax, heightMin, heightMax, weightMin, weightMax, city, ward, district);
    }

    public List<UserSearchPercent> getUserSearchPercentByOppositeSex(User userLogin) {
        // Lấy thông tin của userLogin
        List<InformationOption> InforLogin = informationOptionRepository.getInformationUserId(userLogin.getId());

        // Lấy danh sách người dùng với giới tính ngược lại\
        List<User> userList = new ArrayList<>();
        if(userLogin.getSex().equals("Khác")){
            userList = userRepository.findAll();
        }else {
            String oppositeSex = userLogin.getSex().equals("Nam") ? "Nữ" : "Nam";

            userList = userRepository.findBySex(oppositeSex);
        }


        List<UserSearchPercent> userSearchPercents = new ArrayList<>();

        userList.forEach((user) -> {
            List<InformationOption> infors = informationOptionRepository.getInformationUserId(user.getId());
            Set<InformationOption> informationOptionSet = new HashSet<>(infors);
            InforLogin.forEach(informationOptionSet::add);

            int total = (2 * (InforLogin.size() + infors.size() - informationOptionSet.size()) * 100) / (InforLogin.size() + infors.size());
            if(total>=60) {
                userSearchPercents.add(new UserSearchPercent(user,friendRequestRepository.findFriendsForUser(user).stream().count(), total));
            }
        });

        return userSearchPercents;
    }
}