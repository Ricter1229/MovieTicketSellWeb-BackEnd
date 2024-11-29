package tw.com.ispan.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.MemberBean;
import tw.com.ispan.repository.MemberRepository;

@Service
@Transactional
public class MemberService {
	@Autowired
	private MemberRepository memberRepository;

	public String register(MemberBean newMember) {
        // 帳號是否存在
        boolean exists = memberRepository.existsByAccountOrEmail(newMember.getAccount(), newMember.getEmail());
        if (exists) {
            return "用戶已存在";
        }

        // 加入新帳號
        memberRepository.save(newMember);
        return "Registration successful!";
    }

	public MemberBean login(String account, String password) {
		if (account != null) {
            Optional<MemberBean> optional = memberRepository.findById(account);

            if (optional.isPresent()) {
                MemberBean bean = optional.get();
                if (password != null && !password.isEmpty()) {
                    String pass = bean.getPassword();
                    if (pass.equals(password)) {
                        return bean;
                    }
                }
            }
        }

		return null;
	}

    public MemberBean findByUsername(String username) {
        Optional<MemberBean> memberOptional = memberRepository.findById(username);
        return memberOptional.orElse(null);
    }
    
    public void updatePassword(String email, String newPassword) {
        Optional<MemberBean> memberOptional = memberRepository.findByEmail(email);
        if (memberOptional.isPresent()) {
            MemberBean member = memberOptional.get();
            member.setPassword(newPassword);
            memberRepository.save(member);
        }
    }
}
