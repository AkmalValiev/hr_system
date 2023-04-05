package uz.pdp.lesson51hr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.lesson51hr.entity.Role;
import uz.pdp.lesson51hr.entity.User;
import uz.pdp.lesson51hr.entity.enums.RoleName;
import uz.pdp.lesson51hr.payload.ApiResponse;
import uz.pdp.lesson51hr.payload.LoginDto;
import uz.pdp.lesson51hr.payload.RegisterDto;
import uz.pdp.lesson51hr.payload.UserDto;
import uz.pdp.lesson51hr.repository.RoleRepository;
import uz.pdp.lesson51hr.repository.UserRepository;
import uz.pdp.lesson51hr.security.JwtProvider;

import java.util.*;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User topilmadi!"));
    }

    public ApiResponse register(RegisterDto registerDto) {
        boolean existsByEmail = userRepository.existsByEmail(registerDto.getEmail());
        if (existsByEmail)
            return new ApiResponse("Bunday email mavjud!", false);

        User user = new User();
        user.setEmail(registerDto.getEmail());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_USER)));
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setEmailCode(UUID.randomUUID().toString());
        userRepository.save(user);

        sendEmail(user.getEmail(), user.getEmailCode());
        return new ApiResponse("Muvaffaqiyatli ro'yxatdan o'tdingiz! Akkauntingizni aktivlashtirish uchun emailni tasdiqlang!", true);
    }

    public boolean sendEmail(String sendingEmail, String emailCode){
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("Test@pdp.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Akkauntni tasdiqlash");
            mailMessage.setText("<a href='http://localhost:8080/api/auth/verifyEmail?emailCode=" + emailCode + "&email=" + sendingEmail + "'>Tasdiqlash</a>");
            javaMailSender.send(mailMessage);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public ApiResponse verifyEmail(String emailCode, String email) {
        Optional<User> optionalUser = userRepository.findByEmailAndEmailCode(email, emailCode);
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            userRepository.save(user);
            return new ApiResponse("Akkaunt tasdiqlandi!", true);
        }
        return new ApiResponse("Akkaunt allaqachon tasdiqlangan!", false);
    }

    public ApiResponse login(LoginDto loginDto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(),
                    loginDto.getPassword()));
            User user = (User) authenticate.getPrincipal();
            String token = jwtProvider.generateToken(loginDto.getUsername(), user.getRoles());
            return new ApiResponse(token, true);
        }catch (BadCredentialsException e){
            return new ApiResponse("Parol yoki login xato!", false);
        }
    }

    public ApiResponse editUser(String username, UserDto userDto) {
        Optional<User> optionalUser = userRepository.findByEmail(username);
        if (!optionalUser.isPresent())
            return new ApiResponse("User topilmadi!", false);
        Set<Role> roles = new HashSet<>();
        if (userDto.getIds()!=null) {
            List<Role> roleList = roleRepository.findAll();
            for (Role role : roleList) {
                for (Integer id : userDto.getIds()) {
                    if (role.getId() == id) {
                        roles.add(role);
                    }
                }
            }
        }else {
            return new ApiResponse("Roleni id bo'yicha kiriting!", false);
        }
        if (roles==null)
            return new ApiResponse("Kiritilgan id bo'yicha role topilmadi!", false);
        User user = optionalUser.get();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setRoles(roles);
        user.setEnabled(userDto.isEnabled());
        userRepository.save(user);
        return new ApiResponse("User taxrirlandi!", true);
    }


    public ApiResponse delete(String username) {
        Optional<User> optionalUser = userRepository.findByEmail(username);
        if (!optionalUser.isPresent())
            return new ApiResponse("Bunday user topilmadi!", false);
        try {
            userRepository.delete(optionalUser.get());
            return new ApiResponse("User o'chirildi!", true);
        }catch (Exception e){
            return new ApiResponse("Xatolik!", false);
        }
    }
}
