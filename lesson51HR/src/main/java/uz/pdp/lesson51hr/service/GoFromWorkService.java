package uz.pdp.lesson51hr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.lesson51hr.entity.GoFromWork;
import uz.pdp.lesson51hr.entity.User;
import uz.pdp.lesson51hr.payload.ApiResponse;
import uz.pdp.lesson51hr.payload.GoFromWorkDto;
import uz.pdp.lesson51hr.repository.GoFromWorkRepository;
import uz.pdp.lesson51hr.repository.UserRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class GoFromWorkService {

    @Autowired
    GoFromWorkRepository goFromWorkRepository;
    @Autowired
    UserRepository userRepository;

    public List<GoFromWork> getGoFromWorks() {
        return goFromWorkRepository.findAll();
    }


    public ApiResponse addGoFromWork(GoFromWorkDto goFromWorkDto) {
        Optional<User> optionalUser = userRepository.findByEmail(goFromWorkDto.getUsername());
        if (!optionalUser.isPresent())
            return new ApiResponse("User topilmadi!", false);
        User user = optionalUser.get();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        GoFromWork goFromWork = new GoFromWork();
        goFromWork.setTimeFromWork(timestamp);
        goFromWork.setUser(user);
        goFromWorkRepository.save(goFromWork);
        return new ApiResponse(user.getFirstName()+" ishdan ketdi!", true);
    }
}
