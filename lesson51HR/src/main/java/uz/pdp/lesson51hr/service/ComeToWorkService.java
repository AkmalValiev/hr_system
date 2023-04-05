package uz.pdp.lesson51hr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.lesson51hr.entity.ComeToWork;
import uz.pdp.lesson51hr.entity.GoFromWork;
import uz.pdp.lesson51hr.entity.User;
import uz.pdp.lesson51hr.payload.ApiResponse;
import uz.pdp.lesson51hr.payload.ComeToWorkDto;
import uz.pdp.lesson51hr.payload.TwoTimePeriodsDto;
import uz.pdp.lesson51hr.repository.ComeToWorkRepository;
import uz.pdp.lesson51hr.repository.GoFromWorkRepository;
import uz.pdp.lesson51hr.repository.UserRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ComeToWorkService {
    @Autowired
    ComeToWorkRepository comeToWorkRepository;
    @Autowired
    GoFromWorkRepository goFromWorkRepository;
    @Autowired
    UserRepository userRepository;

    public List<ComeToWork> getComeToWorks() {
        return comeToWorkRepository.findAll();
    }

    public ApiResponse addComeToWork(ComeToWorkDto comeToWorkDto) {
        Optional<User> optionalUser = userRepository.findByEmail(comeToWorkDto.getUsername());
        if (!optionalUser.isPresent())
            return new ApiResponse("User topilmadi!", false);
        User user = optionalUser.get();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        ComeToWork comeToWork = new ComeToWork();
        comeToWork.setTimeToWork(timestamp);
        comeToWork.setUser(user);
        comeToWorkRepository.save(comeToWork);
        return new ApiResponse(user.getFirstName()+" ishxonaga keldi!", true);
    }

    public List<String> getTwoTimePeriods(String username, TwoTimePeriodsDto twoTimePeriodsDto) {
        List<String> periods = new ArrayList<>();
        Timestamp firstTime = Timestamp.valueOf(twoTimePeriodsDto.getFirstTime()+" 00:00:00");
        Timestamp secondTime = Timestamp.valueOf(twoTimePeriodsDto.getSecondTime()+" 00:00:00");

        List<ComeToWork> comeToWorkList = comeToWorkRepository.findAll();
        for (ComeToWork comeToWork : comeToWorkList) {
            if (comeToWork.getTimeToWork().getTime()>firstTime.getTime() &&comeToWork.getTimeToWork().getTime()<secondTime.getTime() ){
                periods.add("Ishga kelgan: "+comeToWork.getTimeToWork().getTime());
            }
        }

        List<GoFromWork> goFromWorkList = goFromWorkRepository.findAll();
        for (GoFromWork goFromWork : goFromWorkList) {
            if (goFromWork.getTimeFromWork().getTime()>firstTime.getTime() && goFromWork.getTimeFromWork().getTime()<secondTime.getTime()){
                periods.add("Ishdan ketgan vaqti: "+goFromWork.getTimeFromWork().getTime());
            }
        }
        return periods;
    }
}
