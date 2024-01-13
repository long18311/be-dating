package com.example.datingbe.service;

import com.example.datingbe.dto.ImagesDto;
import com.example.datingbe.dto.UserProfileDto;
import com.example.datingbe.entity.*;
import com.example.datingbe.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserProfileServce {
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InformationFieldRepository informationFieldRepository;
    @Autowired
    private InformationOptionRepository informationOptionRepository;
    @Autowired
    private ImagesRepository imagesRepository;
    public UserProfileDto getUserProfile(Long userId) {
        User user = userRepository.getOne(userId);

        List<User> friends = friendRequestRepository.findFriendsForUser(user);
    return  new UserProfileDto(user,friends.stream().count());
    }

    public void deleteImage(Images image) {
        imagesRepository.deleteById(image.getId());
    }

    public Images getImageById(Long imgId) {
        return imagesRepository.getReferenceById(imgId);
    }

    public List<InformationField> getInformationFields(){
        return informationFieldRepository.findAll();
    }
    public List<InformationOption> getinformationOptions(List<Long> informationOptionIds){
        List<InformationOption> informationOptions = new ArrayList<>();
        for (Long o: informationOptionIds ) {
            informationOptions.add(informationOptionRepository.getReferenceById(o));
        }
        return informationOptions;
    }
    public UserProfileDto updateUserProfile(Long userId, UserProfileDto userProfile){
        User user = userRepository.getOne(userId);
//        user.setFirstname(userProfile.getFirstname());
//        user.setLastname(userProfile.getLastname());
        user.setDistrict(userProfile.getDistrict());
        user.setCity(userProfile.getCity());
        user.setWard(userProfile.getWard());
        user.setAbout(userProfile.getAbout());
//        user.setSex(userProfile.getSex());
        user.setLatitude(userProfile.getLatitude());
        user.setLongitude(userProfile.getLongitude());
        user.setNickname(userProfile.getNickname());
        user.setMaritalstatus(userProfile.getMaritalstatus());
//        user.setBirthday(userProfile.getBirthday());
        user.setWeight(userProfile.getWeight());
        user.setHeight(userProfile.getHeight());
        Set<InformationOption> informationOptionsUser = getinformationOptions(userProfile.getInformationOptions().stream().map(obj -> obj.getId()).collect(Collectors.toList())).stream().collect(Collectors.toSet());
        user.setInformationOptions(informationOptionsUser);
        user = userRepository.save(user);
        List<User> friends = friendRequestRepository.findFriendsForUser(user);
        userProfile = new UserProfileDto(user,friends.stream().count());
        return userProfile;
    }
    public User saveImage (User user,String url) {
        Images image = new Images();
        image.setUrl(url);
        image.setUser(user);
        image = imagesRepository.save(image);
        return userRepository.getOne(user.getId());
    }
    public int deleteImage (User user,Long imageId) {
        Optional<Images> image = imagesRepository.findById(imageId);
        if (image.isPresent()) {

            if (image.get().getUser().getId() == user.getId()) {
                imagesRepository.deleteById(imageId);
                return 1; // Xóa thành công
            } else {
                return 2; // Không có quyền xóa
            }}
        return 0;  // Không tìm thấy ảnh
    }
}
