package com.example.datingbe.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.datingbe.entity.FriendStatus;
import com.example.datingbe.repository.FriendRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.datingbe.entity.InformationOption;
import com.example.datingbe.entity.User;
import com.example.datingbe.repository.FriendsRepository;
import com.example.datingbe.repository.UserRepository;

@Service
public class GraphService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private  UserRepository userRepository;
    private long[][] connectedUserGraph;

    public GraphService (){}

    public static final int commonThreshold = 3;
    public static final int radiusThreshold = 300;
    public long countCommonValues(List<Long> list1, List<Long> list2) {
        // Using Java streams to find the common values
        List<Long> commonValues = list1.stream()
                .distinct()
                .filter(list2::contains)
                .collect(Collectors.toList());

        return commonValues.size();
    }


    // build graph users
    public void buildGraphUsers (){
        long numberUsers = userRepository.count();
        List<User> users = userRepository.findAll();
        System.out.println("Initialize Graph: " + numberUsers + " x " + numberUsers);
        connectedUserGraph = new long[150][150];
        for (User outerUser : users) {
            StringBuilder line = new StringBuilder();
            for (User innerUser : users) {
                if (outerUser.getId() == innerUser.getId()) {
                    int id = outerUser.getId().intValue();
                    connectedUserGraph[id][id] = 1; // same user
                    line.append(connectedUserGraph[id][id] + " ");
                } else {
                    int outerUserId = outerUser.getId().intValue();
                    int innerUserId = innerUser.getId().intValue();
                    int numberOfCommons = 0;

                    // check city
                    if (outerUser.getCity() != null && innerUser.getCity() != null) {
                        if (outerUser.getCity().equals(innerUser.getCity())) {
                            numberOfCommons++;
                        }
                    }
                    Set<InformationOption> informationOptionSet = outerUser.getInformationOptions();
                    // get list of all infomation id of outer user
                    List<InformationOption> informationOptions = new ArrayList<>(informationOptionSet);

                    List<Long> outerInformationId = new ArrayList<>();
                    for (InformationOption informationOption : informationOptions) {
                        outerInformationId.add(informationOption.getId());
                    }
                    // get list of all information id of inner user
                    List<InformationOption> informationOptions2 = new ArrayList<>(
                            innerUser.getInformationOptions());
                    List<Long> innerInformationId = new ArrayList<>();
                    for (InformationOption informationOption : informationOptions2) {
                        innerInformationId.add(informationOption.getId());
                    }
                    numberOfCommons += countCommonValues(outerInformationId, innerInformationId);
                    connectedUserGraph[outerUserId][innerUserId] = numberOfCommons;
                    connectedUserGraph[innerUserId][outerUserId] = numberOfCommons;
                    line.append(connectedUserGraph[outerUserId][innerUserId] + " ");
                }
            }
            System.out.println(line);
        }
    }


    // find users suggestion (pending request)
    public List<User> buildPendingRequestGraphUsers ( User user){
        List<User> usersPending = friendRequestRepository.findUsersWithPendingFriendRequestsBySenderUserId(user.getId());
        List<User> getSuggestionUsers = new ArrayList<>();
        for (User o: usersPending) {
            List<User> relatedUsers = findTopRelatedUsers(o, 5);
                    for (User o2: relatedUsers) {
                        getSuggestionUsers.add(o2);
                    }
        }

        return getSuggestionUsers;
    }
    public  List<User> findTopRelatedUsers(User startingUser, int topCount) {
        int startingUserId = startingUser.getId().intValue();
        PriorityQueue<RelatedUser> priorityQueue = new PriorityQueue<>(Comparator.reverseOrder());
        Set<Integer> visited = new HashSet<>();
        List<User> topRelatedUsers = new ArrayList<>();

        // Add starting user to the priority queue
        priorityQueue.offer(new RelatedUser(startingUserId, 0));

        // BFS traversal
        while (!priorityQueue.isEmpty()) {
            RelatedUser currentUser = priorityQueue.poll();
            int userId = currentUser.getUserId();

            if (!visited.contains(userId)) {
                visited.add(userId);

                // Enqueue neighbors with weights and within radiusThreshold
                for (int neighborId = 0; neighborId < connectedUserGraph.length; neighborId++) {
                    if (connectedUserGraph[userId][neighborId] > commonThreshold &&
                            !visited.contains(neighborId)) {
                        priorityQueue.offer(new RelatedUser(neighborId, (int) connectedUserGraph[userId][neighborId]));
                    }
                }

                // Process the user or store the related user
                // Here, you can store the user in the list
                User relatedUser = userRepository.findById((long) userId).orElse(null);
                if (relatedUser != null) {
                    topRelatedUsers.add(relatedUser);
                }
            }
        }

        // Sort the result by weight in descending order
        topRelatedUsers.sort(
                Comparator.comparingInt(user -> -(int) connectedUserGraph[startingUserId][user.getId().intValue()]));

        // Get the top topCount users
        return topRelatedUsers.stream().limit(topCount).collect(Collectors.toList());
    }
    private static class RelatedUser implements Comparable<RelatedUser> {
        private final int userId;
        private final int weight;

        public RelatedUser(int userId, int weight) {
            this.userId = userId;
            this.weight = weight;
        }

        public int getUserId() {
            return userId;
        }

        public int getWeight() {
            return weight;
        }

        @Override
        public int compareTo(RelatedUser other) {
            // Compare in reverse order to get max weight first
            return Integer.compare(other.weight, this.weight);
        }
    }


}