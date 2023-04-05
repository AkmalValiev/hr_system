package uz.pdp.lesson51hr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uz.pdp.lesson51hr.entity.Task;
import uz.pdp.lesson51hr.entity.User;
import uz.pdp.lesson51hr.entity.enums.TaskProcessName;
import uz.pdp.lesson51hr.payload.ApiResponse;
import uz.pdp.lesson51hr.payload.TaskDto;
import uz.pdp.lesson51hr.repository.TaskProcessRepository;
import uz.pdp.lesson51hr.repository.TaskRepository;
import uz.pdp.lesson51hr.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TaskProcessRepository taskProcessRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JavaMailSender javaMailSender;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTask(Integer id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        return optionalTask.orElse(null);
    }


    public ApiResponse addTask(TaskDto taskDto) {

        Optional<User> optionalUser1 = userRepository.findByEmail(taskDto.getFromUserEmail());
        if (!optionalUser1.isPresent())
            return new ApiResponse("Kiritilgan email bo'yicha manager topilmadi!", false);

        Optional<User> optionalUser = userRepository.findByEmail(taskDto.getUserEmail());
        if (!optionalUser.isPresent())
            return new ApiResponse("Kiritilgan email bo'yicha user topilmadi!", false);

        LocalDate date = LocalDate.parse(taskDto.getExpireDateOfTask());
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setExpireDateOfTask(date);
        task.setDescription(taskDto.getDescription());
        task.setTaskProcess(taskProcessRepository.findByTaskProcessName(TaskProcessName.YANGI));
        task.setUser(optionalUser.get());
        task.setFromUser(optionalUser1.get());
        Task save = taskRepository.save(task);
        String subject = "Yangi vazifa!";
        String text = "<a href='http://localhost:8080/api/task/verifyTask?email=" + taskDto.getUserEmail() + "&&taskId="+save.getId()+"'>Vasifani qabul qilish</a>";
        sendEmail(taskDto.getUserEmail(), subject, text);
        return new ApiResponse("Vazifa xodimga biriktirildi!", true);
    }

    public ApiResponse editByManager(Integer id, TaskDto taskDto) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent())
            return new ApiResponse("Task topilmadi!", false);
        Optional<User> optionalUser = userRepository.findByEmail(taskDto.getUserEmail());
        if (!optionalUser.isPresent())
            return new ApiResponse("User topilmadi!", false);
        Optional<User> optionalUser1 = userRepository.findByEmail(taskDto.getFromUserEmail());
        if (!optionalUser1.isPresent())
            return new ApiResponse("Manager topilmadi!", false);
        Task task = optionalTask.get();
        task.setName(taskDto.getName());
        LocalDate date = LocalDate.parse(taskDto.getExpireDateOfTask());
        task.setExpireDateOfTask(date);
        task.setDescription(taskDto.getDescription());
        task.setUser(optionalUser.get());
        task.setFromUser(optionalUser1.get());
        taskRepository.save(task);
        String subject = "Vazifa o'zgartirildi!";
        String text = "<a href='http://localhost:8080/api/task/verifyTask?email=" + taskDto.getUserEmail() + "&&taskId="+task.getId()+"'>Vasifani qabul qilish</a>";
        sendEmail(taskDto.getUserEmail(), subject, text);
        return new ApiResponse("Task taxrirlandi va userga qayta jo'natildi!", true);
    }



    public ApiResponse editByUser(Integer id, boolean done) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent())
            return new ApiResponse("Task topilmadi!", false);
        Task task = optionalTask.get();
        if (done){
            task.setDone(true);
            taskRepository.save(task);
            String subject = "Vazifa bajarildi!";
            String text = task.getUser().getEmail()+" xodim "+task.getName()+" vazifani bajardi!";
            sendEmail(task.getFromUser().getEmail(), subject, text);
            return new ApiResponse("Vazifa bajarilganligi haqida "+task.getFromUser().getEmail()+" ga xabar jonatildi!", true);
        }
        return new ApiResponse("Vazifa bajarilmadi!", false);
    }

    public boolean sendEmail(String email, String subject, String text){
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("test@pdp.com");
            mailMessage.setTo(email);
            mailMessage.setSubject(subject);
            mailMessage.setText(text);
            javaMailSender.send(mailMessage);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public ApiResponse verifyTask(String email, Integer taskId) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent())
            return new ApiResponse("User topilmadi!", false);
        User user = optionalUser.get();
        Optional<Task> optionalTask = taskRepository.findByUserIdAndId(user.getId(), taskId);
        if (!optionalTask.isPresent())
            return new ApiResponse("Task topilmadi!",false);

        Task task = optionalTask.get();
        task.setTaskProcess(taskProcessRepository.findByTaskProcessName(TaskProcessName.JARAYONDA));
        taskRepository.save(task);
        return new ApiResponse("Vazifani qabul qildingiz! Vazifaning mazmuni quyidagicha:\nVazifaning nomi: "+task.getName()+"\nVazifaning mazmuni: "+task.getDescription(), true);
    }
}
